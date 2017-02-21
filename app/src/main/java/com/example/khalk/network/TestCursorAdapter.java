package com.example.khalk.network;

/**
 * Created by khalk on 2/21/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * {@link TestCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of test data as its data source. This adapter knows
 * how to create list items for each row of test data in the {@link Cursor}.
 */
public class TestCursorAdapter extends CursorAdapter {

    private static final String TAG = CaseAdapter.class.getName();
    private ProgressBar loadingIndicator;
    private TextView resultTestTextView;
    private Boolean Testing = false;


    /**
     * Constructs a new {@link TestCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public TestCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the test data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current test can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        // Find individual views that we want to modify in the list item layout
//        TextView nameTextView = (TextView) view.findViewById(R.id.);
//        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
//
//        // Find the columns of test attributes that we're interested in
//        int nameColumnIndex = cursor.getColumnIndex(TestEntry.COLUMN_TEST_NAME);
//        int breedColumnIndex = cursor.getColumnIndex(TestEntry.COLUMN_TEST_BREED);
//
//        // Read the test attributes from the Cursor for the current test
//        String testName = cursor.getString(nameColumnIndex);
//        String testBreed = cursor.getString(breedColumnIndex);
//
//        // If the test breed is empty string or null, then use some default text
//        // that says "Unknown breed", so the TextView isn't blank.
//        if (TextUtils.isEmpty(testBreed)) {
//            testBreed = context.getString(R.string.unknown_breed);
//        }
//
//        // Update the TextViews with the attributes for the current test
//        nameTextView.setText(testName);
//        summaryTextView.setText(testBreed);
    }
}

