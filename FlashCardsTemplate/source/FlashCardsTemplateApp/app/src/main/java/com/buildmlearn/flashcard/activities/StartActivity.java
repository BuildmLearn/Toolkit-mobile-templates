package com.buildmlearn.flashcard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.buildmlearn.flashcard.R;
import com.buildmlearn.flashcard.objects.GlobalData;

public class StartActivity extends BaseActivity {
    GlobalData gd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_view);

        gd = GlobalData.getInstance();
        gd.readXml(getApplicationContext(), "flash_content.xml");

        TextView quizAuthor = (TextView) findViewById(R.id.tv_author);
        TextView quizTitle = (TextView) findViewById(R.id.tv_apptitle);

        quizAuthor.setText(gd.getiQuizAuthor());
        quizTitle.setText(gd.getiQuizTitle());

        Button startButton = (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(arg0.getContext(),
                        MainActivity.class);
                startActivity(myIntent);
                finish();
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
