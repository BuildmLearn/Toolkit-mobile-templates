package org.buildmlearn.videocollection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.buildmlearn.videocollection.NetworkUtils;
import org.buildmlearn.videocollection.R;
import org.buildmlearn.videocollection.fragment.MainActivityFragment;

/**
 * Created by Anupam (opticod) on 11/5/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(String videoId) {

        Intent intent = new Intent(this, DetailActivity.class)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, videoId);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            finish();
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Tap back once more to exit.", Toast.LENGTH_SHORT).show();
            isBackPressed = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 3000);
        }
    }
}
