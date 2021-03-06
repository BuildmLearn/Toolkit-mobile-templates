package org.buildmlearn.matchtemplate.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.buildmlearn.matchtemplate.Constants;
import org.buildmlearn.matchtemplate.R;
import org.buildmlearn.matchtemplate.data.DataUtils;
import org.buildmlearn.matchtemplate.data.FetchXMLTask;

/**
 * @brief Splash intro Activity for match template's app.
 *
 * Created by Anupam (opticod) on 24/7/16.
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
