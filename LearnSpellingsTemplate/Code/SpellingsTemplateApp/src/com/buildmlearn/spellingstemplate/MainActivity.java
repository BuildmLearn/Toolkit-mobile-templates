package com.buildmlearn.spellingstemplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {

	private Button mBtn_Start;
	private DataManager mManager;
	private TextView mTv_Title,mTv_Author;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtn_Start = (Button) findViewById(R.id.btn_start);
		mTv_Title=(TextView) findViewById(R.id.tv_apptitle);
		mTv_Author=(TextView) findViewById(R.id.tv_author);
		
		mBtn_Start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent spellingsPage = new Intent(MainActivity.this,
						SpellingActivity.class);
				startActivity(spellingsPage);
				finish();
			}
		});
		
		mManager = DataManager.getInstance();
		mManager.readContent(this);
		mTv_Title.setText(mManager.getTitle());
		mTv_Author.setText(mManager.getAuthor());
		
	}
	
	

}
