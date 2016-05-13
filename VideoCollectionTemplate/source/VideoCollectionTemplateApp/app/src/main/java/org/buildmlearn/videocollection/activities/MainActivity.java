package org.buildmlearn.videocollection.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.buildmlearn.videocollection.Constants;
import org.buildmlearn.videocollection.R;
import org.buildmlearn.videocollection.data.FetchXMLTask;
import org.buildmlearn.videocollection.fragment.MainActivityFragment;

/**
 * Created by Anupam (opticod) on 11/5/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        String title;
        if (extras != null) {
            title = extras.getString(Intent.EXTRA_TEXT);
            setTitle(title);
        }
        prefs = getSharedPreferences("firstRun", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            FetchXMLTask xmlTask = new FetchXMLTask(this);
            xmlTask.execute(Constants.XMLFileName);
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            //TODO: about dialog
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String videoId) {

            Intent intent = new Intent(this, DetailActivity.class)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, videoId);
            startActivity(intent);

    }
}
