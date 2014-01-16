package com.TFT.Quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TFTQuizActivity extends Activity {
	GlobalData gd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_view);
		gd = GlobalData.getInstance();

		gd.ReadContent(TFTQuizActivity.this);
		
		TextView quizAuthor = (TextView) findViewById(R.id.quiz_author);
		TextView quizTitle = (TextView) findViewById(R.id.quiz_title);
		
		quizAuthor.setText(gd.iQuizAuthor);
		quizTitle.setText(gd.iQuizTitle);
		
		Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(arg0.getContext(),QuestionActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});

	}




}
