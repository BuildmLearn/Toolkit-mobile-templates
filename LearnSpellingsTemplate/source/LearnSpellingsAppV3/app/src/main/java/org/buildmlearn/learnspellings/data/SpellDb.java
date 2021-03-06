package org.buildmlearn.learnspellings.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.buildmlearn.learnspellings.Constants;
import org.buildmlearn.learnspellings.data.SpellContract.Spellings;

import java.util.Arrays;

/**
 * @brief Contains database util functions for spell template's app.
 *
 * Created by Anupam (opticod) on 1/6/16.
 */
public class SpellDb {

    private static final String EQUAL = " == ";
    private final SpellDBHelper dbHelper;
    private SQLiteDatabase db;

    public SpellDb(Context context) {
        dbHelper = new SpellDBHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public boolean isOpen() {
        return db.isOpen();
    }

    public void close() {
        dbHelper.close();
    }

    public void markAnswered(int id, String answer) {

        ContentValues values = new ContentValues();
        values.put(Spellings.ANSWERED, answer);
        values.put(Spellings.ATTEMPTED, 1);

        db.update(Spellings.TABLE_NAME, values, Spellings._ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    private void markUnAnswered(int id) {

        ContentValues values = new ContentValues();
        values.put(Spellings.ANSWERED, "");
        values.put(Spellings.ATTEMPTED, 0);

        db.update(Spellings.TABLE_NAME, values, Spellings._ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public void resetCount() {
        for (int i = 1; i <= getCountSpellings(); i++) {
            markUnAnswered(i);
        }
    }

    public int[] getStatistics() {
        int stat[] = new int[3];
        Arrays.fill(stat, 0);

        for (int i = 1; i <= getCountSpellings(); i++) {
            Cursor cursor = getSpellingCursorById(i);
            cursor.moveToFirst();

            String correct_answer = cursor.getString(Constants.COL_WORD);
            int attempted = cursor.getInt(Constants.COL_ATTEMPTED);
            if (attempted == 1) {
                String answer = cursor.getString(Constants.COL_ANSWERED);
                if (answer.equalsIgnoreCase(correct_answer)) {
                    stat[0]++;
                } else {
                    stat[1]++;
                }
            } else {
                stat[2]++;
            }
        }
        return stat;
    }

    public Cursor getSpellingCursorById(int id) {

        String selection = Spellings._ID + EQUAL + id;

        return db.query(
                Spellings.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null
        );
    }

    public long getCountSpellings() {

        return DatabaseUtils.queryNumEntries(db,
                Spellings.TABLE_NAME);
    }

    public int bulkInsertSpellings(@NonNull ContentValues[] values) {

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(Spellings.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return returnCount;
    }

}
