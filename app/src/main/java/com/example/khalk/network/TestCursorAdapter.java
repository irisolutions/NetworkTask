package com.example.khalk.network;

/**
 * Created by khalk on 2/21/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.khalk.network.data.TestCaseContract.TestCaseEntry;

/**
 * {@link TestCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of testButton data as its data source. This adapter knows
 * how to create list items for each row of testButton data in the {@link Cursor}.
 */
public class TestCursorAdapter extends CursorAdapter {

    private static final String TAG = TestCursorAdapter.class.getName();

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

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView caseNameTextView =(TextView)view.findViewById(R.id.case_name);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prefIP = sharedPrefs.getString(
                context.getString(R.string.settings_ip),
               context.getString(R.string.settings_ip_default));
//        Log.d(TAG, "onCreate: sharedPreferences ip ======>"+prefIP);
        String prefPort = sharedPrefs.getString(context.getString(R.string.settings_port),context.getString(R.string.settings_port_default));
//        Log.d(TAG, "onCreate: sharedPreferences port =====>"+prefPort);

        //Find the columns of testButton attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_NAME);
        int controllerColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_CONTROLLER);
        int para1ColumnIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA1);
        int para2ColumnIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA2);
        int para3ColumnIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA3);
        int expectedTestCodeIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_EXPECTED_CODE);
////
        // Read the testButton attributes from the Cursor for the current testButton
        String testName = cursor.getString(nameColumnIndex);
        String testController = cursor.getString(controllerColumnIndex);
        String testPara1=cursor.getString(para1ColumnIndex);
        String testPara2=cursor.getString(para2ColumnIndex);
//        String testPara3=cursor.getString(para3ColumnIndex);
        String expectedTestCode=cursor.getString(expectedTestCodeIndex);

        String testURL=testController+"/"+testPara1+"/"+testPara2;

        Log.d(TAG, "bindView: this is from database \n "+"========"+testController+testPara1+testPara2);

//       caseNameTextView.setText(testURL);
//        testCase=new TestCase("SensoryBoxAPK/AudioVolume/0.8", "200", "192.168.1.2", "8888");
        CustomLinearLayout customLinearLayout = (CustomLinearLayout)view;
        customLinearLayout.setTestCase(new TestCase(testURL,expectedTestCode,prefIP,prefPort));
    }

}

