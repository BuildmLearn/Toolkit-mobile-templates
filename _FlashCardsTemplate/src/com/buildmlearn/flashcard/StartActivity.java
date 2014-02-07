package com.buildmlearn.flashcard;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends Activity {
	GlobalData gd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_view);
		
		gd = GlobalData.getInstance();
		if (gd.iQuizList.size()==0)
		{
			gd.ReadContent(StartActivity.this);
		}
		
		TextView quizAuthor = (TextView) findViewById(R.id.tv_author);
		TextView quizTitle = (TextView) findViewById(R.id.tv_apptitle);
		
		quizAuthor.setText(gd.iQuizAuthor);
		quizTitle.setText(gd.iQuizTitle);
		
		Button startButton = (Button) findViewById(R.id.btn_start);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(arg0.getContext(),MainActivity.class);
				startActivityForResult(myIntent, 0);
				finish();
			}
		});

	}





}
