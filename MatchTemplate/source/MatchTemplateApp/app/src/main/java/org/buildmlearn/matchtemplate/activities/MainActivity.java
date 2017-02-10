package org.buildmlearn.matchtemplate.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.buildmlearn.matchtemplate.Constants;
import org.buildmlearn.matchtemplate.R;
import org.buildmlearn.matchtemplate.data.MatchDb;

/**
 * @brief Activity for the users to match column A with column B in match template's app.
 *
 * Created by Anupam (opticod) on 24/7/16.
 */
public class MainActivity extends AppCompatActivity {

    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MatchDb db = new MatchDb(this);
        db.open();

        Cursor meta = db.getMetaCursor();
        meta.moveToFirst();
        getSupportActionBar().setTitle(meta.getString(Constants.COL_TITLE_META));
        db.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.action_about:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setTitle(String.format("%1$s", getString(R.string.about_us)));
                builder.setMessage(getResources().getText(R.string.about_text));
                builder.setPositiveButton(getString(R.string.ok), null);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();
                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                break;
            default: //do nothing
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed){
            finish();
            super.onBackPressed();
        }
        else{
            Toast.makeText(this, "Tap back once more to exit.", Toast.LENGTH_SHORT).show();
            isBackPressed=true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    isBackPressed= false;
                }
            }, 3000);
        }
    }
}
