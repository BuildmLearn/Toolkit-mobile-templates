package com.buildmlearn.flashcard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.buildmlearn.flashcard.R;
import com.buildmlearn.flashcard.objects.FlashModel;
import com.buildmlearn.flashcard.objects.GlobalData;

public class MainActivity extends BaseActivity {

    Button flipButton;
    Button preButton;
    Button nextButton;
    int iQuestionIndex = 0;
    GlobalData gd = GlobalData.getInstance();
    String flashCardanswer;
    TextView flashcardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iQuestionIndex = 0;

        flashcardNumber = (TextView) findViewById(R.id.flashCardNumber);

        flipButton = (Button) findViewById(R.id.flip_button);
        preButton = (Button) findViewById(R.id.pre_button);
        nextButton = (Button) findViewById(R.id.next_button);

        populateQuestion(iQuestionIndex);

        flipButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        preButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (iQuestionIndex != 0) {
                    iQuestionIndex--;
                    populateQuestion(iQuestionIndex);
                }

            }
        });

        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (iQuestionIndex < gd.getModel().size() - 1) {
                    iQuestionIndex++;
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
        if (mFlash.getQuestion() != null)
            if (mFlash.getAnswer() != null) {
                flashCardanswer = mFlash.getAnswer();
            }
        if (mFlash.getBase64() != null) {
            byte[] decodedString = Base64.decode(mFlash.getBase64(),
                    Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
                    0, decodedString.length);
//            questionImage.setImageBitmap(decodedByte);
        } else {
//            questionText.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    public void reInitialize() {
        iQuestionIndex = 0;
        gd.getModel().clear();
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
