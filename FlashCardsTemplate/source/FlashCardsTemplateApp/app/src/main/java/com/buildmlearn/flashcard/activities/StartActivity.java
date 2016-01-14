package com.buildmlearn.flashcard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.buildmlearn.flashcard.objects.GlobalData;
import com.buildmlearn.flashcard.R;

public class StartActivity extends BaseActivity {
    GlobalData gd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_view);

        gd = GlobalData.getInstance();
        gd.readXml(getApplicationContext(), "flash_content.xml");

        TextView quizAuthor = (TextView) findViewById(R.id.tv_author);
        TextView quizTitle = (TextView) findViewById(R.id.tv_apptitle);

        quizAuthor.setText(gd.getiQuizAuthor());
        quizTitle.setText(gd.getiQuizTitle());

        Button startButton = (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(arg0.getContext(),
                        MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StartActivity.this);

            // set title
            alertDialogBuilder.setTitle("About Us");

            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.about_us))
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
            TextView msg = (TextView) alertDialog
                    .findViewById(android.R.id.message);
            Linkify.addLinks(msg, Linkify.WEB_URLS);

            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
