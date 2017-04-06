package com.example.khalk.network.data;

/**
 * Created by khalk on 2/21/2017.
 * assumbtions:
 * desc:
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.khalk.network.data.TestCaseContract.TestCaseEntry;

/**
 * Database helper for Tests app. Manages database creation and version management.
 */
 class TestCaseDbHelper extends SQLiteOpenHelper {

//    public static final String LOG_TAG = TestCaseDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "test_case.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link TestCaseDbHelper}.
     *
     * @param context of the app
     */
    TestCaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the Tests table
        String SQL_CREATE_Tests_TABLE = "CREATE TABLE " + TestCaseEntry.TABLE_NAME + " ("
                + TestCaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TestCaseEntry.COLUMN_TEST_NAME + " TEXT NOT NULL, "
                + TestCaseEntry.COLUMN_TEST_CONTROLLER + " TEXT NOT NULL, "
                + TestCaseEntry.COLUMN_TEST_PARA1 + " TEXT NOT NULL, "
                + TestCaseEntry.COLUMN_TEST_PARA2 + " TEXT NOT NULL, "
                + TestCaseEntry.COLUMN_TEST_PARA3 + " TEXT , "
                + TestCaseEntry.COLUMN_TEST_EXPECTED_CODE + " TEXT" + ");";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_Tests_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}