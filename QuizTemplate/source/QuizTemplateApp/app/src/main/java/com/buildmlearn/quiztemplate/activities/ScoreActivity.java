/* Copyright (c) 2012, BuildmLearn Contributors listed at http://buildmlearn.org/people/
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the BuildmLearn nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */


package com.buildmlearn.quiztemplate.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.buildmlearn.quiztemplate.objects.GlobalData;
import com.buildmlearn.quiztemplate.R;

public class ScoreActivity extends BaseActivity {
    private GlobalData gd;
    private TextView mTv_correct, mTv_wrong, mTv_unanswered;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_view);
        gd = GlobalData.getInstance();

        mTv_correct = (TextView) findViewById(R.id.tv_correct);
        mTv_wrong = (TextView) findViewById(R.id.tv_wrong);
        mTv_unanswered = (TextView) findViewById(R.id.tv_unanswered);
        mTv_correct.setText("Total Correct: " + gd.getCorrect());
        mTv_wrong.setText("Total Wrong: " + gd.getWrong());
        int unanswered = gd.getTotal() - gd.getCorrect() - gd.getWrong();
        mTv_unanswered.setText("Unanswered: " + unanswered);

        Button startAgainButton = (Button) findViewById(R.id.start_again_button);
        startAgainButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(arg0.getContext(),
                        TFTQuizActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

        Button quitButton = (Button) findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // android.os.Process.killProcess(android.os.Process.myPid());
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
                    ScoreActivity.this);

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
