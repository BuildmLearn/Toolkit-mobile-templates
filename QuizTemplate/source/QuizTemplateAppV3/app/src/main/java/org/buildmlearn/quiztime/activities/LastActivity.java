package org.buildmlearn.quiztime.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.buildmlearn.quiztime.R;
import org.buildmlearn.quiztime.data.QuizDb;

import java.util.Locale;

/**
 * Created by Anupam (opticod) on 11/8/16.
 */
public class LastActivity extends AppCompatActivity {

    private QuizDb db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

        final Activity activity = this;

        db = new QuizDb(this);
        db.open();

        int stat[] = db.getStatistics();

        assert ((TextView) findViewById(R.id.correct)) != null;
        ((TextView) findViewById(R.id.correct)).setText(String.format(Locale.getDefault(), "Total Correct : %1$d", stat[0]));
        assert ((TextView) findViewById(R.id.wrong)) != null;
        ((TextView) findViewById(R.id.wrong)).setText(String.format(Locale.getDefault(), "Total Wrong : %1$d", stat[1]));
        assert ((TextView) findViewById(R.id.un_answered)) != null;
        ((TextView) findViewById(R.id.un_answered)).setText(String.format(Locale.getDefault(), "Total Unanswered : %1$d", stat[2]));

        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.resetCount();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent = new Intent(activity, QuestionActivity.class)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, "1")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

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
                assert ((TextView) welcomeAlert.findViewById(android.R.id.message)) != null;
                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                break;
            default: //do nothing
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
