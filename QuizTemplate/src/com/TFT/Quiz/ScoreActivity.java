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

public class ScoreActivity extends Activity {
	GlobalData gd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);
		gd = GlobalData.getInstance();

		TextView quizTitle = (TextView) findViewById(R.id.quiz_title);
		quizTitle.setText(gd.iQuizTitle);
		
		Button startAgainButton = (Button) findViewById(R.id.start_again_button);
		startAgainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(arg0.getContext(),TFTQuizActivity.class);
				startActivityForResult(myIntent, 0);
				finish();
			}
		});

		
		
		Button quitButton = (Button) findViewById(R.id.quit_button);
		quitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}




}
