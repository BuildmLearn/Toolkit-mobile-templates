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

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class WordInfoActivity extends SherlockActivity {

	private Intent spellingIntent;
	private boolean isCorrect;
	private int position;
	private TextView mTv_Result, mTv_enteredWord, mTv_word, mTv_description,
			mTv_Word_num;
	private DataManager mManager;
	private ArrayList<WordModel> mList;
	private Button mBtn_Next;
	private TextToSpeech textToSpeech;
	private String enteredText;

	@SuppressLint("DefaultLocale")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_info);
		mManager = DataManager.getInstance();
		mList = mManager.getList();

		spellingIntent = getIntent();
		isCorrect = spellingIntent.getBooleanExtra("result", false);
		position = spellingIntent.getIntExtra("index", 0);
		enteredText = spellingIntent.getStringExtra("word");
		mTv_Result = (TextView) findViewById(R.id.tv_result);
		mTv_Word_num = (TextView) findViewById(R.id.tv_word_num);
		mTv_word = (TextView) findViewById(R.id.tv_word);
		mTv_enteredWord = (TextView) findViewById(R.id.tv_input_word);

		mTv_description = (TextView) findViewById(R.id.tv_description);
		mBtn_Next = (Button) findViewById(R.id.btn_next);
		if (position == mList.size() - 1) {
			mBtn_Next.setText("Finish");
		}
		if (isCorrect) {
			mTv_Result.setText(getString(R.string.msg_successful));
			mTv_Result.setTextColor(Color.GREEN);
			// convertTextToSpeech(getString(R.string.msg_successful));
			mTv_enteredWord.setVisibility(View.GONE);
		} else {
			mTv_Result.setText(getString(R.string.msg_failure));
			mTv_Result.setTextColor(Color.RED);
			mTv_enteredWord.setText(getString(R.string.you_entered) + " "
					+ enteredText.toLowerCase());
			// convertTextToSpeech("Wrong");
		}
		textToSpeech = new TextToSpeech(this,
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int arg0) {
						if (arg0 == TextToSpeech.SUCCESS) {
							textToSpeech.setLanguage(Locale.US);
							if (isCorrect)
								convertTextToSpeech(getString(R.string.msg_successful));
							else
								convertTextToSpeech(getString(R.string.msg_failure));
						}
					}
				});
		mTv_Word_num.setText("Word #" + (position + 1) + " of " + mList.size());
		mTv_word.setText(mList.get(position).getWord().toLowerCase());
		mTv_description.setText(mList.get(position).getDescription());

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		textToSpeech.shutdown();
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			if (position < mList.size() - 1) {
				mManager.increaseCount();

				Intent spellingAgain = new Intent(this, SpellingActivity.class);
				startActivity(spellingAgain);
				finish();
			} else {

				Intent resultIntent = new Intent(this, ResultActivity.class);
				startActivity(resultIntent);
				finish();

			}
			break;
		}
	}

	private void convertTextToSpeech(String text) {

		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}/*
	 * 
	 * @Override public void onInit(int status) { if (status ==
	 * TextToSpeech.SUCCESS) { int result = textToSpeech.setLanguage(Locale.US);
	 * if (result == TextToSpeech.LANG_MISSING_DATA || result ==
	 * TextToSpeech.LANG_NOT_SUPPORTED) { Log.e("error",
	 * "This Language is not supported"); } } else { Log.e("error",
	 * "Initilization Failed!"); } }
	 */
}
