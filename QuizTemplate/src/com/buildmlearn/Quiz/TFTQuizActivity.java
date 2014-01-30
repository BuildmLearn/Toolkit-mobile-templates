package com.buildmlearn.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class TFTQuizActivity extends SherlockActivity {
	private GlobalData gd;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR); // Add this line

		setContentView(R.layout.start_view);

		gd = GlobalData.getInstance();
		reInitialize();
		
		gd.ReadContent(TFTQuizActivity.this);

		TextView quizAuthor = (TextView) findViewById(R.id.tv_author);
		TextView quizTitle = (TextView) findViewById(R.id.tv_apptitle);

		quizAuthor.setText(gd.iQuizAuthor);
		quizTitle.setText(gd.iQuizTitle);

		Button startButton = (Button) findViewById(R.id.btn_start);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
					Intent myIntent = new Intent(arg0.getContext(),
						QuestionActivity.class);
				startActivity(myIntent);
				finish();
			}
		});

	}

	private void reInitialize()
	{
		gd.total=0;
		gd.correct=0;
		gd.wrong=0;
		gd.iQuizList.clear();
	}
}
