package com.buildmlearn.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class ScoreActivity extends SherlockActivity {
	private GlobalData gd;
	private TextView mTv_correct, mTv_wrong, mTv_unanswered;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);
		gd = GlobalData.getInstance();

		mTv_correct = (TextView) findViewById(R.id.tv_correct);
		mTv_wrong = (TextView) findViewById(R.id.tv_wrong);
		mTv_unanswered = (TextView) findViewById(R.id.tv_unanswered);
		mTv_correct.setText("Total Correct: " + gd.correct);
		mTv_wrong.setText("Total Wrong: " + gd.wrong);
		int unanswered = gd.total - gd.correct - gd.wrong;
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

}
