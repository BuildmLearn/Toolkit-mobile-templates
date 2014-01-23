package com.buildmlearn.spellingstemplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class ResultActivity extends SherlockActivity {
	private TextView mTv_Correct, mTv_Wrong, mTv_Unanswered;
	private DataManager mDataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		mDataManager = DataManager.getInstance();
		mTv_Correct = (TextView) findViewById(R.id.tv_correct);
		mTv_Wrong = (TextView) findViewById(R.id.tv_wrong);
		mTv_Unanswered = (TextView) findViewById(R.id.tv_unanswered);
		int correct = mDataManager.getCorrect();
		int wrong = mDataManager.getWrong();
		int unanswered = mDataManager.getList().size() - correct - wrong;

		mTv_Correct.setText(getString(R.string.correct) + " " + correct);

		mTv_Wrong.setText(getString(R.string.wrong_spelled) + " " + wrong);
		mTv_Unanswered.setText(getString(R.string.unanswered) + " "
				+ unanswered);

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

}
