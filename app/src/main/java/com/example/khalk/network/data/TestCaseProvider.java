package com.example.khalk.network.data;

/**
 * Created by khalk on 2/21/2017.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.khalk.network.data.TestCaseContract.TestCaseEntry;


/**
 * {@link ContentProvider} for TESTS app.
 */
public class TestCaseProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = TestCaseProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the TESTS table
     */
    private static final int TESTS = 100;

    /**
     * URI matcher code for the content URI for a single test in the TESTS table
     */
    private static final int TEST_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.TESTS/TESTS" will map to the
        // integer code {@link #TESTS}. This URI is used to provide access to MULTIPLE rows
        // of the TESTS table.
        sUriMatcher.addURI(TestCaseContract.CONTENT_AUTHORITY, TestCaseContract.PATH_TESTS, TESTS);

        // The content URI of the form "content://com.example.android.TESTS/TESTS/#" will map to the
        // integer code {@link #TEST_ID}. This URI is used to provide access to ONE single row
        // of the TESTS table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.TESTS/TESTS/3" matches, but
        // "content://com.example.android.TESTS/TESTS" (without a number at the end) doesn't match.
        sUriMatcher.addURI(TestCaseContract.CONTENT_AUTHORITY, TestCaseContract.PATH_TESTS + "/#", TEST_ID);
    }

    /**
     * Database helper object
     */
    private TestCaseDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TestCaseDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TESTS:
                // For the TESTS code, query the TESTS table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the TESTS table.
                cursor = database.query(TestCaseEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TEST_ID:
                // For the TEST_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.TESTS/TESTS/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = TestCaseEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the TESTS table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TestCaseEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TESTS:
                return insertTest(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a test into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertTest(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(TestCaseEntry.COLUMN_TEST_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Test requires a name");
        }
// TODO: 2/21/2017 check validation of the input 

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new test with the given values
        long id = database.insert(TestCaseEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the test content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TESTS:
                return updateTest(uri, contentValues, selection, selectionArgs);
            case TEST_ID:
                // For the TEST_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = TestCaseEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateTest(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update TESTS in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more TESTS).
     * Return the number of rows that were successfully updated.
     */
    private int updateTest(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link TestCaseEntry#COLUMN_TEST_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(TestCaseEntry.COLUMN_TEST_NAME)) {
            String name = values.getAsString(TestCaseEntry.COLUMN_TEST_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Test requires a name");
            }
        }

        // TODO: 2/21/2017 check validation of inputs

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(TestCaseEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TESTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TestCaseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TEST_ID:
                // Delete a single row given by the ID in the URI
                selection = TestCaseEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TestCaseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TESTS:
                return TestCaseEntry.CONTENT_LIST_TYPE;
            case TEST_ID:
                return TestCaseEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
