package org.buildmlearn.learnwithflashcards.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.buildmlearn.learnwithflashcards.Constants;
import org.buildmlearn.learnwithflashcards.R;
import org.buildmlearn.learnwithflashcards.animations.FlipPageTransformer;
import org.buildmlearn.learnwithflashcards.data.FlashDb;
import org.buildmlearn.learnwithflashcards.data.FlashModel;
import org.buildmlearn.learnwithflashcards.widgets.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by Anupam (opticod) on 7/7/16.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private Context mContext;
    private FlashDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        mContext = MainActivity.this;

        db = new FlashDb(this);
        db.open();

        Bundle extras = getIntent().getExtras();
        String FlashId = "1";
        if (extras != null) {
            FlashId = extras.getString(Intent.EXTRA_TEXT);
        }

        Menu m = navigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu("Flash Cards");
        final long numFlash = db.getCountFlashCards();

        for (int i = 1; i <= numFlash; i++) {
            topChannelMenu.add(String.format(Locale.getDefault(), "Flash Card %1$d", i));
            topChannelMenu.getItem(i - 1).setIcon(R.drawable.ic_assignment_black_24dp);
            final int finalI = i;
            topChannelMenu.getItem(i - 1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(mContext, MainActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(finalI))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return false;
                }
            });
        }

        MenuItem mi = m.getItem(m.size() - 1);
        mi.setTitle(mi.getTitle());


        Cursor flash_cursor = db.getFlashCursorById(Integer.parseInt(FlashId));
        flash_cursor.moveToFirst();
        String question = flash_cursor.getString(Constants.COL_QUESTION);
        String answer = flash_cursor.getString(Constants.COL_ANSWER);
        String hint = flash_cursor.getString(Constants.COL_HINT);
        String base64 = flash_cursor.getString(Constants.COL_BASE64);

        assert findViewById(R.id.intro_number) != null;
        ((TextView) findViewById(R.id.intro_number)).setText(String.format(Locale.ENGLISH, "Card #%d of %d", Integer.parseInt(FlashId), numFlash));
        assert findViewById(R.id.question) != null;
        ((TextView) findViewById(R.id.question)).setText(question);
        assert findViewById(R.id.hint) != null;
        ((TextView) findViewById(R.id.hint)).setText(String.format(Locale.ENGLISH, "Hint : %s", hint));

        Button prv = (Button) findViewById(R.id.previous);
        Button flip = (Button) findViewById(R.id.flip);
        Button next = (Button) findViewById(R.id.next);
        viewPager = (ViewPager) findViewById(R.id.viewpagerflash);


        if (null != FlashId && FlashId.equals("1")) {
            assert prv != null;
            prv.setEnabled(false);
            prv.setTextColor(ContextCompat.getColor(mContext, R.color.grey_shade_4));
        }

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

        final String finalFlashId1 = FlashId;
        assert prv != null;
        prv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nextFlashId = Integer.parseInt(finalFlashId1) - 1;
                Intent intent = new Intent(mContext, MainActivity.class)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, String.valueOf(nextFlashId))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        final String finalFlashId = FlashId;
        assert next != null;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long nextFlashId = Integer.parseInt(finalFlashId) + 1;
                if (nextFlashId <= numFlash) {

                    Intent intent = new Intent(mContext, MainActivity.class)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, String.valueOf(nextFlashId))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(mContext, LastActivity.class)
                            .setType("text/plain");
                    startActivity(intent);
                    finish();
                }
            }
        });

        assert flip != null;
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0)
                    viewPager.setCurrentItem(1, true);
                else
                    viewPager.setCurrentItem(0, true);
            }
        });

        FlashModel mFlash = new FlashModel();
        mFlash.setQuestion(question);
        mFlash.setAnswer(answer);
        mFlash.setHint(hint);
        mFlash.setBase64(base64);
        setAdapterData(mFlash);
    }

    private void setAdapterData(FlashModel mFlash) {
        MyPagerAdapter adapter = new MyPagerAdapter(mFlash);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.action_about:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setTitle(String.format("%1$s", getString(R.string.about_us)));
                builder.setMessage(getResources().getText(R.string.about_text));
                builder.setPositiveButton(getString(R.string.ok), null);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();
                assert welcomeAlert.findViewById(android.R.id.message) != null;
                assert welcomeAlert.findViewById(android.R.id.message) != null;
                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                break;
            default: //do nothing
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MyPagerAdapter extends PagerAdapter {

        final FlashModel model;

        public MyPagerAdapter(FlashModel mFlash) {
            this.model = mFlash;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View cardView;

            if (position == 0) {
                cardView = LayoutInflater.from(MainActivity.this).inflate(R.layout.question_layout_flash_card, container, false);
                ImageView image = (ImageView) cardView.findViewById(R.id.questionimage);

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
