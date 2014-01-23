package com.buildmlearn.spellingstemplate;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class SpellingActivity extends SherlockActivity implements
		TextToSpeech.OnInitListener {
	private TextToSpeech textToSpeech;
	private DataManager mManager;
	private ArrayList<WordModel> mWordList;
	private int count;
	private AlertDialog mAlert;
	private TextView mTv_WordNumber;
	private Button mBtn_Spell, mBtn_Skip;
	private EditText mEt_Spelling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spelling);
		mBtn_Spell = (Button) findViewById(R.id.btn_ready);

		mBtn_Skip = (Button) findViewById(R.id.btn_skip);
		mTv_WordNumber = (TextView) findViewById(R.id.tv_word_number);
		textToSpeech = new TextToSpeech(this, this);
		mManager = DataManager.getInstance();
		mWordList = mManager.getList();
		count = mManager.getActiveWordCount();
		mTv_WordNumber.setText("Word #" + (count + 1) + " of "
				+ mWordList.size());
	}

	public void click(View view) {
		switch (view.getId()) {
		case R.id.btn_skip:
			if (count < mWordList.size() - 1) {

				count++;
				mManager.increaseCount();
				mTv_WordNumber.setText("Word #" + (count + 1) + " of "
						+ mWordList.size());
				mBtn_Spell.setEnabled(false);
				mBtn_Skip.setEnabled(false);
				mBtn_Skip.setTextColor(Color.WHITE);
				mBtn_Spell.setTextColor(Color.WHITE);
			} else {
				Intent resultIntent = new Intent(this, ResultActivity.class);
				startActivity(resultIntent);
				finish();

			}
			break;

		case R.id.btn_speak:
			convertTextToSpeech(mWordList.get(count).getWord());
			mBtn_Spell.setEnabled(true);
			mBtn_Skip.setEnabled(true);
			mBtn_Skip.setTextColor(Color.RED);
			mBtn_Spell.setTextColor(Color.GREEN);
			// mBtn_Spell.setBackgroundColor(Color.GREEN);
			// mBtn_Skip.setBackgroundColor(Color.RED);
			break;
		case R.id.btn_ready:

			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.dialog_spellinginput, null);
			Builder builder = new Builder(this);
			mAlert = builder.create();
			mAlert.setCancelable(true);
			mAlert.setView(textEntryView, 10, 10, 10, 10);
			if (mAlert != null && !mAlert.isShowing()) {
				mAlert.show();
			}
			mEt_Spelling = (EditText) mAlert.findViewById(R.id.et_spelling);
			Button mBtn_Submit = (Button) mAlert.findViewById(R.id.btn_submit);
			mBtn_Submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					submit();
				}
			});

			break;

		}

	}

	/**
	 * Releases the resources used by the TextToSpeech engine. It is good
	 * practice for instance to call this method in the onDestroy() method of an
	 * Activity so the TextToSpeech engine can be cleanly stopped.
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		textToSpeech.shutdown();
	}

	/**
	 * Speaks the string using the specified queuing strategy and speech
	 * parameters.
	 */
	private void convertTextToSpeech(String text) {
		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = textToSpeech.setLanguage(Locale.US);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("error", "This Language is not supported");
			}
		} else {
			Log.e("error", "Initilization Failed!");
		}
	}

	public void submit() {
		String input = mEt_Spelling.getText().toString().trim();
		if (input == null || input.length() == 0) {
			Toast.makeText(SpellingActivity.this, "Please enter the spelling",
					Toast.LENGTH_SHORT).show();

		} else {
			mAlert.dismiss();
			boolean isCorrect = false;
			if (mEt_Spelling.getText().toString()
					.equalsIgnoreCase(mWordList.get(count).getWord())) {
				isCorrect = true;
				mManager.incrementCorrect();
			} else {
				mManager.incrementWrong();
			}
			Intent wordInfoIntent = new Intent(SpellingActivity.this,
					WordInfoActivity.class);
			wordInfoIntent.putExtra("result", isCorrect);
			wordInfoIntent.putExtra("index", count);
			wordInfoIntent.putExtra("word", input);

			startActivity(wordInfoIntent);
			finish();
		}
	}
}
