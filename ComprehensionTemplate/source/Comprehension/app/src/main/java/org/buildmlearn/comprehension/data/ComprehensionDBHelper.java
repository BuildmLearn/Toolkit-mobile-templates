package org.buildmlearn.comprehension.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.buildmlearn.comprehension.data.ComprehensionContract.MetaDetails;
import org.buildmlearn.comprehension.data.ComprehensionContract.Questions;

/**
 * Created by Anupam (opticod) on 1/6/16.
 */

class ComprehensionDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comprehension.db";
    private static final int DATABASE_VERSION = 1;

    public ComprehensionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE__TABLE_A = "CREATE TABLE " + Questions.TABLE_NAME + " (" +
                Questions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Questions.QUESTION + "  TEXT," +
                Questions.OPTION_1 + "  TEXT," +
                Questions.OPTION_2 + "  TEXT," +
                Questions.OPTION_3 + "  TEXT," +
                Questions.OPTION_4 + "  TEXT," +
                Questions.ANSWER + "  INTEGER )";

        sqLiteDatabase.execSQL(SQL_CREATE__TABLE_A);

        final String SQL_CREATE__TABLE_B = "CREATE TABLE " + MetaDetails.TABLE_NAME + " (" +
                MetaDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MetaDetails.TITLE + "  TEXT," +
                MetaDetails.PASSAGE + "  TEXT," +
                MetaDetails.TIME + "  INTEGER )";

        sqLiteDatabase.execSQL(SQL_CREATE__TABLE_B);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Questions.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MetaDetails.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}