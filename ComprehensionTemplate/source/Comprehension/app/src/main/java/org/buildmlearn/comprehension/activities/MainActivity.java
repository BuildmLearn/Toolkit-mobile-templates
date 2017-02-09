package org.buildmlearn.comprehension.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import org.buildmlearn.comprehension.Constants;
import org.buildmlearn.comprehension.R;
import org.buildmlearn.comprehension.data.ComprehensionDb;

import java.util.Locale;

/**
 * @brief Main Fragment for comprehension template's app.
 *
 * Created by Anupam (opticod) on 31/5/16.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isBackPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ComprehensionDb db = new ComprehensionDb(this);
        db.open();
        db.resetCount();

        Cursor cursor = db.getMetaCursor();
        cursor.moveToFirst();
        String title = cursor.getString(Constants.COL_TITLE);
        getSupportActionBar().setTitle(title);
        String passage = cursor.getString(Constants.COL_PASSAGE);
        final long time = cursor.getLong(Constants.COL_TIME);
        final TextView timer = (TextView) findViewById(R.id.timer);
        assert timer != null;
        timer.setText(String.valueOf(time));
        final CountDownTimer countDownTimer = new CountDownTimer(time * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long min = millisUntilFinished / 60000;
                long sec = millisUntilFinished / 1000 - min * 60;
                timer.setText(String.format(Locale.getDefault(), "%1$d:%2$02d", min, sec));
            }

            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, "1")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }.start();

        Menu m = navigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu("Questions");
        long numQues = db.getCountQuestions();

        for (int i = 1; i <= numQues; i++) {
            topChannelMenu.add(String.format(Locale.getDefault(), "Question %1$d", i));
            topChannelMenu.getItem(i - 1).setIcon(R.drawable.ic_assignment_black_24dp);
            final int finalI = i;
            topChannelMenu.getItem(i - 1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getApplicationContext(), QuestionActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(finalI))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    countDownTimer.cancel();
                    finish();
                    return false;
                }
            });
        }

        MenuItem mi = m.getItem(m.size() - 1);
        mi.setTitle(mi.getTitle());
        db.close();

        ((TextView) findViewById(R.id.passage)).setText(passage);
        findViewById(R.id.go_to_ques).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, "1")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                countDownTimer.cancel();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed){
                finish();
                super.onBackPressed();
            }
            else{
                Toast.makeText(this, "Tap back once more to exit.", Toast.LENGTH_SHORT).show();
                isBackPressed=true;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        isBackPressed= false;
                    }
                }, 3000);
            }}
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
            default: //do nothing
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
