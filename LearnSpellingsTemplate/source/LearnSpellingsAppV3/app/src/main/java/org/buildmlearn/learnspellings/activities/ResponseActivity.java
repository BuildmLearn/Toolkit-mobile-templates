package org.buildmlearn.learnspellings.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import org.buildmlearn.learnspellings.Constants;
import org.buildmlearn.learnspellings.R;
import org.buildmlearn.learnspellings.data.SpellDb;

import java.util.Locale;

/**
 * Created by Anupam (opticod) on 31/5/16.
 */

public class ResponseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private SpellDb db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
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

        mContext = ResponseActivity.this;

        db = new SpellDb(this);
        db.open();


        Bundle extras = getIntent().getExtras();
        String spellId = "1";
        if (extras != null) {
            spellId = extras.getString(Intent.EXTRA_TEXT);
        }

        Cursor spell_cursor = db.getSpellingCursorById(Integer.parseInt(spellId));
        spell_cursor.moveToFirst();
        String word = spell_cursor.getString(Constants.COL_WORD);
        String meaning = spell_cursor.getString(Constants.COL_MEANING);
        String answered = spell_cursor.getString(Constants.COL_ANSWERED);


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

        TextView mTv_WordNumber = (TextView) findViewById(R.id.intro_number);

        mTv_WordNumber.setText(String.format(Locale.ENGLISH, "Word #%d of %d", Integer.parseInt(spellId), numQues));

        String message;
        String word_text_view;
        if (word.trim().equalsIgnoreCase(answered)) {
            message = "Great! You got it right.";
            word_text_view = "Correct Spell: &nbsp<font color='green'>" + word + "</font>";
        } else {
            message = "Oops! You got it wrong.";
            word_text_view = "Correct Spell:&nbsp <font color=\"#7fe77f\">" + word + "</font> <br />You entered:&nbsp <font color=\"#ee9797\">" + answered + "</font>";
        }

        assert ((TextView) findViewById(R.id.intro_response)) != null;
        ((TextView) findViewById(R.id.intro_response)).setText(message);
        assert ((TextView) findViewById(R.id.word)) != null;
        ((TextView) findViewById(R.id.word)).setText(Html.fromHtml(word_text_view), TextView.BufferType.SPANNABLE);

        assert ((TextView) findViewById(R.id.meaning)) != null;
        ((TextView) findViewById(R.id.meaning)).setText(meaning);

        final String finalSpellId = spellId;
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long numColumns = db.getCountSpellings();

                long nextSpellId = Integer.parseInt(finalSpellId) + 1;

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

}
