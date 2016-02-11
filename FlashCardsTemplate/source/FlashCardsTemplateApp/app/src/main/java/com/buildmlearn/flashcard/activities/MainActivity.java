package com.buildmlearn.flashcard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buildmlearn.flashcard.R;
import com.buildmlearn.flashcard.animation.FlipPageTransformer;
import com.buildmlearn.flashcard.objects.FlashModel;
import com.buildmlearn.flashcard.objects.GlobalData;
import com.buildmlearn.flashcard.widgets.FixedSpeedScroller;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements OnClickListener {

    Button flipButton;
    Button preButton;
    Button nextButton;
    int iQuestionIndex = 0;
    GlobalData gd;
    String flashCardanswer;
    TextView flashcardNumber;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iQuestionIndex = 0;

        flashcardNumber = (TextView) findViewById(R.id.flashCardNumber);
        flipButton = (Button) findViewById(R.id.flip_button);
        preButton = (Button) findViewById(R.id.pre_button);
        nextButton = (Button) findViewById(R.id.next_button);
        viewPager = (ViewPager) findViewById(R.id.viewpagerflash);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager.setPageTransformer(true, new FlipPageTransformer());

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this, new AccelerateDecelerateInterpolator());
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gd = GlobalData.getInstance();
        gd.readXml(this, "flash_content.xml");
        populateQuestion(iQuestionIndex);

        flipButton.setOnClickListener(this);
        preButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    public void populateQuestion(int index) {
        if (index == 0) {
            preButton.setEnabled(false);
        } else
            preButton.setEnabled(true);

        int cardNum = index + 1;
        flashcardNumber.setText("Card #" + cardNum + " of " + gd.getTotalCards());
        FlashModel mFlash = gd.getModel().get(index);

        setAdapterData(mFlash);
    }

    private void setAdapterData(FlashModel mFlash) {
        MyPagerAdapter adapter = new MyPagerAdapter(mFlash);
        viewPager.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_button:
                if (iQuestionIndex != 0) {
                    iQuestionIndex--;
                    populateQuestion(iQuestionIndex);
                }
                break;
            case R.id.next_button:
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
                break;
            case R.id.flip_button:
                if (viewPager.getCurrentItem() == 0)
                    viewPager.setCurrentItem(1, true);
                else
                    viewPager.setCurrentItem(0, true);
                break;
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        FlashModel model;

        public MyPagerAdapter(FlashModel mFlash) {
            this.model = mFlash;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View cardView = null;

            if (position == 0) {
                cardView = LayoutInflater.from(MainActivity.this).inflate(R.layout.question_layout_flash_card, container, false);
                ImageView image = (ImageView) cardView.findViewById(R.id.questionimage);
                TextView text = (TextView) cardView.findViewById(R.id.questiontext);

                text.setText(model.getQuestion());

                if (model.getBase64() != null && !model.getBase64().equals("")) {
                    byte[] decodedString = Base64.decode(model.getBase64(),
                            Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
                            0, decodedString.length);
                    image.setImageBitmap(decodedByte);
                } else {
                    image.setImageResource(R.drawable.image_quiz);
                }
            } else {
                cardView = LayoutInflater.from(MainActivity.this).inflate(R.layout.answer_layout_flash_card, container, false);
                TextView text = (TextView) cardView.findViewById(R.id.answertext);

                text.setText(model.getAnswer());
            }

            int width = MainActivity.this.getResources().getDisplayMetrics().widthPixels - MainActivity.this.getResources().getDimensionPixelSize(R.dimen.material_button_height);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            cardView.findViewById(R.id.cardview).setLayoutParams(params);

            container.addView(cardView);
            return cardView;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
