package org.buildmlearn.matchtemplate.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * Created by Anupam (opticod) on 24/7/16.
 */
public class MatchDb {

    private static final String EQUAL = " == ";
    private final MatchDBHelper dbHelper;
    private SQLiteDatabase db;

    public MatchDb(Context context) {
        dbHelper = new MatchDBHelper(context);
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

    public Cursor getMetaCursor() {

        return db.query(
                MatchContract.MetaDetails.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getMatchCursor() {

        return db.query(
                MatchContract.Matches.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getMatchCursorById(int id) {

        String selection = MatchContract.Matches._ID + EQUAL + id;

        return db.query(
                MatchContract.Matches.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null
        );
    }

    public long getCountMatches() {

        return DatabaseUtils.queryNumEntries(db,
                MatchContract.Matches.TABLE_NAME);
    }

    public int bulkInsertMatches(@NonNull ContentValues[] values) {

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(MatchContract.Matches.TABLE_NAME, null, value);
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

                long _id = db.insert(MatchContract.MetaDetails.TABLE_NAME, null, value);
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
