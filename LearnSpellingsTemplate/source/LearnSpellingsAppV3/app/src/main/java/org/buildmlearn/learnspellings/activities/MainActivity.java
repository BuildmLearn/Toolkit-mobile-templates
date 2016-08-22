package org.buildmlearn.learnspellings.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.buildmlearn.learnspellings.Constants;
import org.buildmlearn.learnspellings.R;
import org.buildmlearn.learnspellings.data.SpellDb;

import java.util.HashMap;
import java.util.Locale;

/**
 * @brief Activity for the users to test their spelling skills. spelling template's app.
 *
 * Created by Anupam (opticod) on 31/5/16.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final float MIN_SPEECH_RATE = 0.01f;
    private android.app.AlertDialog mAlert;
    private Context mContext;
    private Button mBtn_Spell, mBtn_Skip;
    private EditText mEt_Spelling;
    private SeekBar mSb_SpeechRate;
    private SpellDb db;
    private TextToSpeech tts;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        mContext = MainActivity.this;

        db = new SpellDb(this);
        db.open();

        Bundle extras = getIntent().getExtras();
        String spellId = "1";
        if (extras != null) {
            spellId = extras.getString(Intent.EXTRA_TEXT);
        }

        Menu m = navigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu("Spellings");
        long numQues = db.getCountSpellings();

        for (int i = 1; i <= numQues; i++) {
            topChannelMenu.add(String.format(Locale.getDefault(), "Spelling %1$d", i));
            topChannelMenu.getItem(i - 1).setIcon(R.drawable.ic_assignment_black_24dp);
            final int finalI = i;
            topChannelMenu.getItem(i - 1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(mContext, MainActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(finalI))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return false;
                }
            });
        }

        MenuItem mi = m.getItem(m.size() - 1);
        mi.setTitle(mi.getTitle());


        mBtn_Spell = (Button) findViewById(R.id.spell_it);
        mBtn_Skip = (Button) findViewById(R.id.skip);
        mSb_SpeechRate = (SeekBar) findViewById(R.id.seek_bar);
        TextView mTv_WordNumber = (TextView) findViewById(R.id.intro_number);

        mBtn_Spell.setEnabled(false);
        mBtn_Skip.setEnabled(false);
        mTv_WordNumber.setText(String.format(Locale.ENGLISH, "Word #%d of %d", Integer.parseInt(spellId), numQues));

        Cursor spell_cursor = db.getSpellingCursorById(Integer.parseInt(spellId));
        spell_cursor.moveToFirst();
        String word = spell_cursor.getString(Constants.COL_WORD);
        String meaning = spell_cursor.getString(Constants.COL_MEANING);
        String answered = spell_cursor.getString(Constants.COL_ANSWERED);

        setListeners(spellId, word, meaning, answered);

    }

    private void setListeners(final String spellId, final String word, final String meaning, final String answered) {

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long numColumns = db.getCountSpellings();

                long nextSpellId = Integer.parseInt(spellId) + 1;

                if (nextSpellId <= numColumns) {

                    Intent intent = new Intent(mContext, MainActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(nextSpellId))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(mContext, LastActivity.class)
                            .setType("text/plain");
                    startActivity(intent);
                    finish();
                }
            }
        });

        findViewById(R.id.speak_ico).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = new ProgressDialog(mContext);
                progress.setCancelable(false);
                progress.setMessage("Loading TTS Engine...");
                progress.show();


                float speechRate = getProgressValue(mSb_SpeechRate.getProgress());
                tts.setSpeechRate(speechRate);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String utteranceId = word.hashCode() + "";
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "dict");
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, map);
                }
                mBtn_Spell.setEnabled(true);
                mBtn_Skip.setEnabled(true);
                mBtn_Skip.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                mBtn_Spell.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            }
        });

        findViewById(R.id.spell_it).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(mContext);
                final View textEntryView = factory.inflate(
                        R.layout.spelling_dialog_spellinginput, null);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                mAlert = builder.create();
                mAlert.setCancelable(true);
                mAlert.setView(textEntryView, 10, 10, 10, 10);
                if (mAlert != null && !mAlert.isShowing()) {
                    mAlert.show();
                }
                mEt_Spelling = (EditText) mAlert.findViewById(R.id.et_spelling);
                mEt_Spelling.setText(answered);

                Button mBtn_Submit = (Button) mAlert.findViewById(R.id.btn_submit);
                mBtn_Submit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        submit(spellId);
                    }
                });
            }
        });

    }

    private void submit(String spell) {
        String input = mEt_Spelling.getText().toString().trim();
        if (input.length() == 0) {
            Toast.makeText(mContext, "Please enter the spelling",
                    Toast.LENGTH_SHORT).show();

        } else {
            mAlert.dismiss();

            String answered = mEt_Spelling.getText().toString().trim();
            db.markAnswered(Integer.parseInt(spell), answered);

            Intent intent = new Intent(mContext, ResponseActivity.class)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, String.valueOf(spell));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.action_about:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setTitle(String.format("%1$s", getString(R.string.about_us)));
                builder.setMessage(getResources().getText(R.string.about_text));
                builder.setPositiveButton(getString(R.string.ok), null);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();
                assert ((TextView) welcomeAlert.findViewById(android.R.id.message)) != null;
                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                break;
            default: //do nothing
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(mContext, "US English is not supported. Playing in device's default installed language.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Initialization Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                progress.dismiss();
            }

            @Override
            public void onDone(String utteranceId) {

            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    private float getProgressValue(int percent) {
        float temp = ((float) percent / 100);
        float speechRate = temp * 2;

        if (speechRate < MIN_SPEECH_RATE)
            speechRate = MIN_SPEECH_RATE;
        return speechRate;
    }

}
