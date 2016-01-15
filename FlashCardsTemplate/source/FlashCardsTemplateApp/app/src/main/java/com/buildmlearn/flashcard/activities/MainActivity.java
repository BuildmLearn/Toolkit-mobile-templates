package com.buildmlearn.flashcard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.buildmlearn.flashcard.R;
import com.buildmlearn.flashcard.objects.FlashModel;
import com.buildmlearn.flashcard.objects.GlobalData;

public class MainActivity extends BaseActivity implements
        AnimationListener {

    View answerView, questionView;
    Button flipButton;
    Button preButton;
    Button nextButton;
    boolean isFlipped = false;
    int iQuestionIndex = 0;
    GlobalData gd = GlobalData.getInstance();
    String flashCardanswer;
    ImageView questionImage;
    TextView flashCardText, flashcardNumber;
    TextView questionText;
    private Animation animation1;
    private Animation animation2;
    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);

        questionView = (View) findViewById(R.id.questionInMain);
        answerView = (View) findViewById(R.id.answerInMain);

        questionView.setVisibility(View.VISIBLE);
        answerView.setVisibility(View.GONE);
        iQuestionIndex = 0;

        questionImage = (ImageView) findViewById(R.id.questionImage);
        flashCardText = (TextView) findViewById(R.id.flashCardText);
        questionText = (TextView) findViewById(R.id.questionhint);
        flashcardNumber = (TextView) findViewById(R.id.flashCardNumber);

        flipButton = (Button) findViewById(R.id.flip_button);
        preButton = (Button) findViewById(R.id.pre_button);
        nextButton = (Button) findViewById(R.id.next_button);

        populateQuestion(iQuestionIndex);
        currentView = questionView;

        flipButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                currentView.clearAnimation();
                currentView.setAnimation(animation1);
                currentView.startAnimation(animation1);

            }
        });

        preButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (iQuestionIndex != 0) {
                    isFlipped = false;

                    iQuestionIndex--;
                    questionView.setVisibility(View.VISIBLE);
                    answerView.setVisibility(View.GONE);
                    currentView = questionView;

                    populateQuestion(iQuestionIndex);
                }

            }
        });

        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (iQuestionIndex < gd.getModel().size() - 1) {
                    isFlipped = false;
                    iQuestionIndex++;
                    questionView.setVisibility(View.VISIBLE);
                    answerView.setVisibility(View.GONE);
                    currentView = questionView;
                    populateQuestion(iQuestionIndex);

                } else {
                    Intent myIntent = new Intent(MainActivity.this,
                            ScoreActivity.class);
                    startActivity(myIntent);
                    reInitialize();
                    finish();
                }

            }
        });
    }

    public void populateQuestion(int index) {
        if (index == 0) {
            preButton.setEnabled(false);
        } else
            preButton.setEnabled(true);

        int cardNum = index + 1;
        flashcardNumber.setText("Card #" + cardNum + " of " + gd.getTotalCards());
        FlashModel mFlash = gd.getModel().get(index);
        TextView answerText = (TextView) findViewById(R.id.answerText);
        if (mFlash.getQuestion() != null)
            questionText.setText(mFlash.getQuestion());
        if (mFlash.getAnswer() != null) {
            flashCardanswer = mFlash.getAnswer();
            answerText.setText(mFlash.getAnswer());
        }
        if (mFlash.getBase64() != null) {
            byte[] decodedString = Base64.decode(mFlash.getBase64(),
                    Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
                    0, decodedString.length);
            questionImage.setImageBitmap(decodedByte);
        } else {
            questionText.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    public void reInitialize() {
        iQuestionIndex = 0;
        gd.getModel().clear();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animation1) {
            TextView answerText = (TextView) findViewById(R.id.answerText);

            if (!isFlipped) {
                answerView.setVisibility(View.VISIBLE);
                questionView.setVisibility(View.GONE);
                isFlipped = true;
                answerText.setText(flashCardanswer);
                currentView = answerView;
            } else {
                isFlipped = false;
                answerText.setText("");
                questionView.setVisibility(View.VISIBLE);
                answerView.setVisibility(View.GONE);
                currentView = questionView;
            }
            currentView.clearAnimation();
            currentView.setAnimation(animation2);
            currentView.startAnimation(animation2);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
