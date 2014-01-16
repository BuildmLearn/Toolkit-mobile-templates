package com.flashcardapp.main;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
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
		
		TextView quizAuthor = (TextView) findViewById(R.id.quiz_author);
		TextView quizTitle = (TextView) findViewById(R.id.quiz_title);
		
		quizAuthor.setText(gd.iQuizAuthor);
		quizTitle.setText(gd.iQuizTitle);
		
		Button startButton = (Button) findViewById(R.id.start_button);
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
