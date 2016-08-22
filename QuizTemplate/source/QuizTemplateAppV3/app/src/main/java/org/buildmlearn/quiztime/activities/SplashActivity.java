package org.buildmlearn.quiztime.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.buildmlearn.quiztime.Constants;
import org.buildmlearn.quiztime.R;
import org.buildmlearn.quiztime.data.DataUtils;
import org.buildmlearn.quiztime.data.FetchXMLTask;
import org.buildmlearn.quiztime.data.QuizDb;

/**
 * @brief Splash intro Activity for quiz template app.
 *
 * Created by Anupam (opticod) on 11/8/16.
 */
public class SplashActivity extends Activity {

    private SharedPreferences prefs = null;
    private QuizDb db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity mActivity = this;
        setContentView(R.layout.activity_splash);

        final String result[] = DataUtils.readTitleAuthor(this);
        TextView title = (TextView) findViewById(R.id.title);
        TextView author_name = (TextView) findViewById(R.id.author_name);

        title.setText(result[0]);
        author_name.setText(result[1]);

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, QuestionActivity.class)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, "1");

                startActivity(intent);
                finish();
            }
        });

        prefs = getSharedPreferences(Constants.firstrun, MODE_PRIVATE);

        db = new QuizDb(this);
        db.open();
        db.resetCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean(Constants.firstrun, true)) {
            FetchXMLTask xmlTask = new FetchXMLTask(this);
            xmlTask.execute(Constants.XMLFileName);
            prefs.edit().putBoolean(Constants.firstrun, false).apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
