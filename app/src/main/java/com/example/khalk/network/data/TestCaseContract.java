package com.example.khalk.network.data;

/**
 * Created by khalk on 2/21/2017.
 */

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract for the tests app.
 */
public final class TestCaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TestCaseContract() {
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.khalk.network";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.khalk.network/tests is a valid path for
     * looking at test data. content://com.example.khalk.network/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_TESTS = "tests";

    /**
     * Inner class that defines constant values for the tests database table.
     * Each entry in the table represents a single test.
     */
    public static final class TestCaseEntry implements BaseColumns {

        /**
         * The content URI to access the test data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TESTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of tests.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TESTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single test.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TESTS;

        /**
         * Name of database table for tests
         */
        public final static String TABLE_NAME = "tests";

        /**
         * Unique ID number for the test (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the test case.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TEST_NAME = "name";

        /**
         * Controller for the test case
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TEST_CONTROLLER = "controller";

        /**
         * Para1 for the test case
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TEST_PARA1 = "para1";

        /**
         * Para2 for the test case
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TEST_PARA2 = "para2";
        /**
         * Para3 for the test case
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TEST_PARA3 = "para3";
        /**
         * expected code for the test case
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_TEST_EXPECTED_CODE = "expected_code";

    }

}
