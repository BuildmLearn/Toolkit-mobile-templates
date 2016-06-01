package org.buildmlearn.comprehension.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.buildmlearn.comprehension.data.ComprehensionContract.Questions;

/**
 * Created by Anupam (opticod) on 1/6/16.
 */
public class ComprehensionDb {

    private static final String EQUAL = " == ";
    private final ComprehensionDBHelper dbHelper;
    private SQLiteDatabase db;

    public ComprehensionDb(Context context) {
        dbHelper = new ComprehensionDBHelper(context);
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

    public Cursor getQuestionsCursor() {

        return db.query(
                Questions.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getQuestionCursorById(int id) {

        String selection = Questions._ID + EQUAL + id;

        return db.query(
                Questions.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null
        );
    }

    public long getCountQuestions() {

        return DatabaseUtils.queryNumEntries(db,
                Questions.TABLE_NAME);
    }

    public int bulkInsertQuestions(@NonNull ContentValues[] values) {

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(Questions.TABLE_NAME, null, value);
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

    public int bulkInsertMetaDetails(@NonNull ContentValues[] values) {

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(ComprehensionContract.MetaDetails.TABLE_NAME, null, value);
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
