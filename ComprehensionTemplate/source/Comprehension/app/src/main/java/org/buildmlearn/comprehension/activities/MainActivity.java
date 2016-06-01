package org.buildmlearn.comprehension.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.buildmlearn.comprehension.Constants;
import org.buildmlearn.comprehension.R;
import org.buildmlearn.comprehension.data.ComprehensionDb;

import java.util.Locale;

/**
 * Created by Anupam (opticod) on 31/5/16.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ComprehensionDb db = new ComprehensionDb(this);
        db.open();
        Cursor cursor = db.getMetaCursor();
        cursor.moveToFirst();
        String passage = cursor.getString(Constants.COL_PASSAGE);
        final long time = cursor.getLong(Constants.COL_TIME);
        final TextView timer = (TextView) findViewById(R.id.timer);
        assert timer != null;
        timer.setText(String.valueOf(time));
        new CountDownTimer(time * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long min = millisUntilFinished / 60000;
                long sec = millisUntilFinished / 1000 - min * 60;
                timer.setText(String.format(Locale.getDefault(), "%1$d:%2$02d", min, sec));
            }

            public void onFinish() {

            }
        }.start();

        db.close();
        ((TextView) findViewById(R.id.passage)).setText(passage);
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
