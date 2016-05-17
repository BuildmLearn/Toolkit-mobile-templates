package org.buildmlearn.videocollection.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.buildmlearn.videocollection.Constants;
import org.buildmlearn.videocollection.NetworkUtils;
import org.buildmlearn.videocollection.R;
import org.buildmlearn.videocollection.data.FetchXMLTask;
import org.buildmlearn.videocollection.fragment.MainActivityFragment;

/**
 * Created by Anupam (opticod) on 11/5/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(Constants.firstrun, MODE_PRIVATE);

        if (NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
        }

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
    public void onItemSelected(String videoId) {

            Intent intent = new Intent(this, DetailActivity.class)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, videoId);
            startActivity(intent);

    }
}
