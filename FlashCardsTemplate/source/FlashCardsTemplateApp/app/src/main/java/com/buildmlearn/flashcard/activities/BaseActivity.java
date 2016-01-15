package com.buildmlearn.flashcard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.Toast;

import com.buildmlearn.flashcard.R;

/**
 * Created by Ashish Goel on 1/14/2016.
 */
public class BaseActivity extends AppCompatActivity {

    Toast toast;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void makeToast(String text) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    void showInfoDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
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
    }
}
