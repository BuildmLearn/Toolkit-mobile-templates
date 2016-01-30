package com.buildmlearn.flashcard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buildmlearn.flashcard.R;
import com.buildmlearn.flashcard.objects.GlobalData;

public class StartActivity extends BaseActivity {

    GlobalData gd;
    CardView cardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cardView = (CardView) findViewById(R.id.cardviewquizmain);

        int minimumHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.4f);
        cardView.setMinimumHeight(minimumHeight);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        gd = GlobalData.getInstance();
        gd.readXml(getApplicationContext(), "flash_content.xml");

        TextView quizAuthor = (TextView) findViewById(R.id.tv_author);
        TextView quizTitle = (TextView) findViewById(R.id.tv_apptitle);

        quizAuthor.setText(gd.getiQuizAuthor());
        quizTitle.setText(gd.getiQuizTitle());

        LinearLayout startButton = (LinearLayout) findViewById(R.id.start_btn);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(arg0.getContext(),
                        MainActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.activity_enter_from_right_animation, R.anim.activity_exit_to_left_animation);
            }
        });

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

}
