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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buildmlearn.quiztemplate.R;
import com.buildmlearn.quiztemplate.objects.GlobalData;

public class TFTQuizActivity extends BaseActivity {
    private GlobalData gd;
    CardView cardView;
    LinearLayout startButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        startButton = (LinearLayout) findViewById(R.id.start_btn);
        cardView = (CardView) findViewById(R.id.cardviewquizmain);

        int minimumHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.4f);
        cardView.setMinimumHeight(minimumHeight);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        gd = GlobalData.getInstance();
        reInitialize();

        // gd.ReadContent(TFTQuizActivity.this);
        gd.readXml(TFTQuizActivity.this, "quiz_content.xml");
        TextView quizAuthor = (TextView) findViewById(R.id.tv_author);
        TextView quizTitle = (TextView) findViewById(R.id.tv_apptitle);

        quizAuthor.setText(gd.getiQuizAuthor());
        quizTitle.setText(gd.getiQuizTitle());

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(arg0.getContext(),
                        QuestionActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.activity_enter_from_right_animation, R.anim.activity_exit_to_left_animation);
            }
        });

    }

    private void reInitialize() {
        gd.setTotal(0);
        gd.setCorrect(0);
        gd.setWrong(0);
        gd.getiQuizList().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            showDialofForAboutBuildmLearn();

            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
