package org.buildmlearn.dictation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.buildmlearn.dictation.R;
import org.buildmlearn.dictation.fragment.MainActivityFragment;

/**
 * Created by Anupam (opticod) on 4/7/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemSelected(String infoId) {

        Intent intent = new Intent(this, org.buildmlearn.dictation.activities.DetailActivity.class)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, infoId);
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
