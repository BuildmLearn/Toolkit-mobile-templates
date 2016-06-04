package org.buildmlearn.comprehension.activities;

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
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.buildmlearn.comprehension.Constants;
import org.buildmlearn.comprehension.R;
import org.buildmlearn.comprehension.data.ComprehensionDb;

import java.util.Locale;

/**
 * Created by Anupam (opticod) on 2/6/16.
 */
public class QuestionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        String questionId = null;
        if (extras != null) {
            questionId = extras.getString(Intent.EXTRA_TEXT);
        }

        final ComprehensionDb db = new ComprehensionDb(this);
        db.open();
        Cursor cursor = db.getQuestionCursorById(Integer.parseInt(questionId));
        cursor.moveToFirst();
        String question = cursor.getString(Constants.COL_QUESTION);
        String option_1 = cursor.getString(Constants.COL_OPTION_1);
        String option_2 = cursor.getString(Constants.COL_OPTION_2);
        String option_3 = cursor.getString(Constants.COL_OPTION_3);
        String option_4 = cursor.getString(Constants.COL_OPTION_4);
        final String correct_answer = cursor.getString(Constants.COL_CORRECT_ANSWER);
        int attempted = cursor.getInt(Constants.COL_ATTEMPTED);
        String answered;

        Cursor meta = db.getMetaCursor();
        meta.moveToFirst();
        String title = meta.getString(Constants.COL_TITLE);
        toolbar.setTitle(title);

        Menu m = navigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu("Questions");
        long numQues = db.getCountQuestions();

        final String finalQuestionId = questionId;

        final RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group);

        if (attempted == 1) {
            answered = cursor.getString(Constants.COL_ANSWERED);
            rg.check(rg.getChildAt(Integer.parseInt(answered)).getId());
        }
        for (int i = 1; i <= numQues; i++) {
            topChannelMenu.add(String.format(Locale.getDefault(), "Question %1$d", i));
            topChannelMenu.getItem(i - 1).setIcon(R.drawable.ic_assignment_black_24dp);
            final int finalI = i;
            topChannelMenu.getItem(i - 1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int radioButtonID = rg.getCheckedRadioButtonId();
                    View radioButton = rg.findViewById(radioButtonID);
                    int idx = rg.indexOfChild(radioButton);

                    if (!db.isOpen()) {
                        db.open();
                    }

                    if (idx == -1) {
                        db.markUnAnswered(Integer.parseInt(finalQuestionId));
                    } else {
                        db.markAnswered(Integer.parseInt(finalQuestionId), idx);
                    }
                    db.close();

                    Intent intent = new Intent(getApplicationContext(), QuestionActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(finalI));
                    startActivity(intent);
                    finish();

                    return false;
                }
            });
        }

        MenuItem mi = m.getItem(m.size() - 1);


        ((TextView) findViewById(R.id.question_title)).setText(String.format(Locale.getDefault(), "Question No : %1$s", questionId));
        ((TextView) findViewById(R.id.question)).setText(question);
        if (option_1 != null) {
            findViewById(R.id.radioButton1).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.radioButton1)).setText(option_1);
        }
        if (option_2 != null) {
            findViewById(R.id.radioButton2).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.radioButton2)).setText(option_2);
        }
        if (option_3 != null) {
            findViewById(R.id.radioButton3).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.radioButton3)).setText(option_3);
        }
        if (option_4 != null) {
            findViewById(R.id.radioButton4).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.radioButton4)).setText(option_4);
        }


        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonID = rg.getCheckedRadioButtonId();
                View radioButton = rg.findViewById(radioButtonID);
                int idx = rg.indexOfChild(radioButton);

                if (!db.isOpen()) {
                    db.open();
                }

                if (idx == -1) {
                    db.markUnAnswered(Integer.parseInt(finalQuestionId));
                } else {
                    db.markAnswered(Integer.parseInt(finalQuestionId), idx);
                }

                long numColumns = db.getCountQuestions();

                long nextQuesId = Integer.parseInt(finalQuestionId) + 1;

                db.close();
                if (nextQuesId <= numColumns) {

                    Intent intent = new Intent(getApplicationContext(), QuestionActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(nextQuesId));
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(getApplicationContext(), LastActivity.class)
                            .setType("text/plain");
                    startActivity(intent);
                    finish();
                }

            }
        });

        if (Integer.parseInt(questionId) == 1) {

            findViewById(R.id.previous).setVisibility(View.INVISIBLE);

        } else {

            findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int radioButtonID = rg.getCheckedRadioButtonId();
                    View radioButton = rg.findViewById(radioButtonID);
                    int idx = rg.indexOfChild(radioButton);

                    if (!db.isOpen()) {
                        db.open();
                    }

                    if (idx == -1) {
                        db.markUnAnswered(Integer.parseInt(finalQuestionId));
                    } else {
                        db.markAnswered(Integer.parseInt(finalQuestionId), idx);
                    }

                    db.close();

                    int prevQuesId = Integer.parseInt(finalQuestionId) - 1;

                    if (prevQuesId >= 1) {
                        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class)
                                .setType("text/plain")
                                .putExtra(Intent.EXTRA_TEXT, String.valueOf(prevQuesId));
                        startActivity(intent);
                    }
                }
            });
        }
        if (db.isOpen()) {
            db.close();
        }

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
                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
