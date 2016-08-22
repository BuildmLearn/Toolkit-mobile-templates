package org.buildmlearn.learnwithflashcards.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.buildmlearn.learnwithflashcards.Constants;
import org.buildmlearn.learnwithflashcards.R;
import org.buildmlearn.learnwithflashcards.data.DataUtils;
import org.buildmlearn.learnwithflashcards.data.FetchXMLTask;
import org.buildmlearn.learnwithflashcards.data.FlashDb;

/**
 * @brief Splash intro activity for flash card template's app.
 *
 * Created by Anupam (opticod) on 7/7/16.
 */
public class SplashActivity extends Activity {

    private SharedPreferences prefs = null;

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
                Intent intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FlashDb db = new FlashDb(this);
        db.open();
        db.close();

        prefs = getSharedPreferences(Constants.firstrun, MODE_PRIVATE);

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
}
