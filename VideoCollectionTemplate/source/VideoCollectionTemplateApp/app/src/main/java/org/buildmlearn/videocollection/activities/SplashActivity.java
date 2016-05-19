package org.buildmlearn.videocollection.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.buildmlearn.videocollection.R;
import org.buildmlearn.videocollection.data.DataUtils;

/**
 * Created by Anupam (opticod) on 11/5/16.
 */
public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity mActivity = this;
        setContentView(R.layout.activity_splash);

        final String result[] = DataUtils.read_Title_Author(this);
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
    }
}
