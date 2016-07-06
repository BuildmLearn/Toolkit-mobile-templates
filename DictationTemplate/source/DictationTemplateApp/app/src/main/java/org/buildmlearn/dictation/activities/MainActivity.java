package org.buildmlearn.dictation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.buildmlearn.dictation.R;
import org.buildmlearn.dictation.fragment.MainActivityFragment;

/**
 * Created by Anupam (opticod) on 4/7/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {


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
}
