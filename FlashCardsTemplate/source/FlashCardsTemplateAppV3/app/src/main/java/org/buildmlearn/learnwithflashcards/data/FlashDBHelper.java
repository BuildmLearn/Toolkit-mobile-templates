package org.buildmlearn.learnwithflashcards.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.buildmlearn.learnwithflashcards.data.FlashContract.FlashCards;

/**
 * @brief DatabaseHelper for flash card template's simulator.
 *
 * Created by Anupam (opticod) on 7/7/16.
 */

class FlashDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "learn_flash.db";
    private static final int DATABASE_VERSION = 1;

    public FlashDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE__TABLE_A = "CREATE TABLE " + FlashCards.TABLE_NAME + " (" +
                FlashCards._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FlashCards.QUESTION + "  TEXT," +
                FlashCards.ANSWER + "  TEXT," +
                FlashCards.HINT + "  TEXT," +
                FlashCards.BASE64 + "  TEXT )";

        sqLiteDatabase.execSQL(SQL_CREATE__TABLE_A);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FlashCards.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}