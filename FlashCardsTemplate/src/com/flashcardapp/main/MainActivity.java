/* Copyright (c) 2012, BuildmLearn Contributors listed at http://buildmlearn.org/people/
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the BuildmLearn nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.flashcardapp.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	FlipAnimation flipAnimation = null;
	Button flipButton;
	Button preButton;
	Button nextButton;
	boolean isFlipped = false;
	int iQuestionIndex =0;
	GlobalData gd = GlobalData.getInstance();
	String flashCardanswer;
	ImageView questionImage;
	TextView flashCardText;
	TextView questionText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final View questionView = (View)findViewById(R.id.questionInMain);
		final View answerView = (View)findViewById(R.id.answerInMain);
		
		iQuestionIndex = 0;
		
		 questionImage = (ImageView) findViewById(R.id.questionImage);
		flashCardText = (TextView) findViewById(R.id.flashCardText);
		questionText = (TextView) findViewById(R.id.questionText);
		
		flipAnimation = new FlipAnimation(questionView, answerView);
		
		questionView.setVisibility(View.VISIBLE);
		answerView.setVisibility(View.GONE);
		
		flipButton = (Button) findViewById(R.id.flip_button);
		flipButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView answerText = (TextView) findViewById(R.id.answerText);
				
				if (!isFlipped)
				{
					flipAnimation.forward();
					flipAnimation.setDuration(1000);
					questionView.startAnimation(flipAnimation);
					isFlipped = true;
					answerText.setText(flashCardanswer);
				}
				else
				{
					flipAnimation.reverse();
					flipAnimation.setDuration(1000);
					answerView.startAnimation(flipAnimation);
					isFlipped = false;
					answerText.setText("");
				}
			}
		});
		
		populateQuestion(iQuestionIndex);
		
		preButton = (Button) findViewById(R.id.pre_button);
		preButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(iQuestionIndex !=0)
				{
					iQuestionIndex--;
					if (isFlipped)
					{
						flipAnimation.reverse();
						flipAnimation.setDuration(0);
						answerView.startAnimation(flipAnimation);
						isFlipped = false;
						TextView answerText = (TextView) findViewById(R.id.answerText);
						answerText.setText("");
					}
					populateQuestion(iQuestionIndex);
				}
				
			}
		});
		
		nextButton = (Button) findViewById(R.id.next_button);
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (iQuestionIndex < gd.iQuizList.size()-1)
				{
					iQuestionIndex++;
					if (isFlipped)
					{
						flipAnimation.reverse();
						flipAnimation.setDuration(0);
						answerView.startAnimation(flipAnimation);
						isFlipped = false;
						TextView answerText = (TextView) findViewById(R.id.answerText);
						answerText.setText("");
					}
					populateQuestion(iQuestionIndex);	
				}
				else{ 
					Intent myIntent = new Intent(MainActivity.this,ScoreActivity.class);
					startActivityForResult(myIntent, 0);
					reInitialize();
					finish();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void populateQuestion(int index) {
		
		String[] dataLine = gd.iQuizList.get(index).split("__");
		questionImage.setVisibility(View.GONE);
		if (dataLine[0].equals("IMAGE"))
		{
			Resources r = getResources();
			int picId = r.getIdentifier("image"+String.valueOf(index), "drawable", "com.flashcardapp.main");
			questionImage.setBackgroundResource(picId);
			questionImage.setVisibility(View.VISIBLE);
		}
		
		
		
		String[] dataArray =dataLine[1].split("==");
//		TextView answerText = (TextView) findViewById(R.id.answerText);
//		answerText.setText(dataArray[1]);
		flashCardanswer = dataArray[1];
		
		flashCardText.setText(dataArray[0]);
		
		questionText.setVisibility(View.GONE);
		if (dataArray.length == 3)
		{
			questionText.setText(dataArray[2]);
			questionText.setVisibility(View.VISIBLE);
		}
		
	}

	public void reInitialize() {
		iQuestionIndex =0;
		gd.iQuizList.clear();
	}
}
