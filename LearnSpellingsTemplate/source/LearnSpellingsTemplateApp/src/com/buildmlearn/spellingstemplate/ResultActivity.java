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

package com.buildmlearn.spellingstemplate;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ResultActivity extends SherlockActivity {
	private TextView mTv_Correct, mTv_Wrong, mTv_Unanswered;
	private DataManager mDataManager;
	private TextToSpeech textToSpeech;
	private int unanswered, wrong, correct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		mDataManager = DataManager.getInstance();
		mTv_Correct = (TextView) findViewById(R.id.tv_correct);
		mTv_Wrong = (TextView) findViewById(R.id.tv_wrong);
		mTv_Unanswered = (TextView) findViewById(R.id.tv_unanswered);
		correct = mDataManager.getCorrect();
		wrong = mDataManager.getWrong();
		unanswered = mDataManager.getList().size() - correct - wrong;

		mTv_Correct.setText(getString(R.string.correct) + " " + correct);

		mTv_Wrong.setText(getString(R.string.wrong_spelled) + " " + wrong);
		mTv_Unanswered.setText(getString(R.string.unanswered) + " "
				+ unanswered);
		textToSpeech = new TextToSpeech(this,
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int arg0) {
						if (arg0 == TextToSpeech.SUCCESS) {
							textToSpeech.setLanguage(Locale.US);
							/*
							 * String speechText = getString(R.string.correct)
									+ " " + correct
									+ getString(R.string.wrong_spelled) + " "
									+ wrong + getString(R.string.unanswered)
									+ " " + unanswered;
							*/
							String speechText =  (String) mTv_Correct.getText()+ "." + (String)mTv_Wrong.getText()+ "." + (String)mTv_Unanswered.getText();
							convertTextToSpeech(speechText);
						}
					}
				});

	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.btn_restart:
			mDataManager.reset();
			Intent restartApp = new Intent(ResultActivity.this,
					MainActivity.class);
			startActivity(restartApp);
			finish();
			break;
		case R.id.btn_exit:
			mDataManager.reset();
			finish();
			break;

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.shutdown();
	}

	private void convertTextToSpeech(String text) {

		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_info) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					ResultActivity.this);

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
