package com.buildmlearn.flashcard;

import android.app.Activity;
import android.content.Intent;
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
		setContentView(R.layout.activity_finish);
		gd = GlobalData.getInstance();
		TextView mCardQuizName=(TextView) findViewById(R.id.tv_lastcard);
		mCardQuizName.setText(gd.iQuizTitle);

		Button startAgainButton = (Button) findViewById(R.id.btn_restart);
		startAgainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(ScoreActivity.this,
						StartActivity.class);
				startActivity(myIntent);
				finish();
			}
		});

		Button quitButton = (Button) findViewById(R.id.btn_exit);
		quitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}

}
