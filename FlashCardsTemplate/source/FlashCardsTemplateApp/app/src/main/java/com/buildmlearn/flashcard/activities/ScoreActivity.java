package com.buildmlearn.flashcard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.buildmlearn.flashcard.R;
import com.buildmlearn.flashcard.objects.GlobalData;

public class ScoreActivity extends BaseActivity implements OnClickListener {
    GlobalData gd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        gd = GlobalData.getInstance();
        TextView mCardQuizName = (TextView) findViewById(R.id.tv_lastcard);
        mCardQuizName.setText(gd.getiQuizTitle());

        findViewById(R.id.quit_button).setOnClickListener(this);
        findViewById(R.id.start_again_button).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.quit_button) {
            moveTaskToBack(true);
            finish();
        } else if (v.getId() == R.id.start_again_button) {
            Intent myIntent = new Intent(ScoreActivity.this,
                    StartActivity.class);
            startActivity(myIntent);
            finish();
        }
    }
}
