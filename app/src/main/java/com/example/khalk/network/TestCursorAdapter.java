package com.example.khalk.network;

/**
 * Created by khalk on 2/21/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.khalk.network.data.TestCaseContract.TestCaseEntry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * {@link TestCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of testButton data as its data source. This adapter knows
 * how to create list items for each row of testButton data in the {@link Cursor}.
 */
public class TestCursorAdapter extends CursorAdapter {

    private static final String TAG = TestCursorAdapter.class.getName();
    private ProgressBar loadingIndicator;
    private TextView resultTestTextView;
    private TextView caseNameTextView;
    private Button testButton;
    private Boolean Testing = false;
    private TestCase testCase;
    private String testURL;


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
        caseNameTextView =(TextView)view.findViewById(R.id.case_name);
        resultTestTextView=(TextView)view.findViewById(R.id.test_result);
        testButton =(Button)view.findViewById(R.id.test_button);
        loadingIndicator=(ProgressBar)view.findViewById(R.id.loading_indicator);

        //Find the columns of testButton attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_NAME);
        int controllerColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_CONTROLLER);
        int para1ColumnIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA1);
        int para2ColumnIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA2);
        int para3ColumnIndex=cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA3);
////
        // Read the testButton attributes from the Cursor for the current testButton
        String testName = cursor.getString(nameColumnIndex);
        String testController = cursor.getString(controllerColumnIndex);
        String testPara1=cursor.getString(para1ColumnIndex);
        String testPara2=cursor.getString(para2ColumnIndex);
//        String testPara3=cursor.getString(para3ColumnIndex);

        testURL=testController+"/"+testPara1+"/"+testPara2;

        Log.d(TAG, "bindView: this is from database \n "+"========"+testController+testPara1+testPara2);

//        testCase=new TestCase("SensoryBoxAPK/AudioVolume/0.8", "200", "192.168.1.2", "8888");
        testCase=new TestCase(testURL, "200", "192.168.1.2", "8888");
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Testing) {
                    run(testCase);
                }
            }
        });




    }


    public void run(TestCase test) {
        TestCursorAdapter.ResultData expectedResultData = new TestCursorAdapter.ResultData();
        expectedResultData.setCode(Integer.parseInt(test.getExpectedCode()));
        Log.d(TAG, "run: the final url "+test.finalUrl);
        new TestCursorAdapter.UrlTesting(expectedResultData, test).execute(test.finalUrl);
    }


//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        // Find individual views that we want to modify in the list item layout
//        TextView nameTextView = (TextView) view.findViewById(R.id.case_name);
//        TextView summaryTextView = (TextView) view.findViewById(R.id.test_result);
//
//        // Find the columns of testButton attributes that we're interested in
//        int nameColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_CONTROLLER);
//        int controllerColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA1);
//
//        // Read the testButton attributes from the Cursor for the current testButton
//        String testName = cursor.getString(nameColumnIndex);
//        String testBreed = cursor.getString(controllerColumnIndex);
//
//        // If the testButton breed is empty string or null, then use some default text
//        // that says "Unknown breed", so the TextView isn't blank.
//
//        // Update the TextViews with the attributes for the current testButton
//        nameTextView.setText(testName);
//        summaryTextView.setText(testBreed);
//    }
    public class UrlTesting extends AsyncTask<Object, Object, TestCursorAdapter.ResultData> implements DialogInterface.OnCancelListener {
        private TestCursorAdapter.ResultData resultData = null;
        InetAddress inetAddress;
        TestCase testCase;

        UrlTesting(TestCursorAdapter.ResultData code, TestCase test) {
            this.resultData = code;
            inetAddress = null;
            this.testCase = test;
        }

        @Override
        protected void onPreExecute() {

//            testCase.loadingIndicator.setVisibility(View.VISIBLE);
//            loadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected TestCursorAdapter.ResultData doInBackground(Object... params) {
            Testing = true;
            String UrlTestingUrl = (String) params[0];
            TestCursorAdapter.ResultData data = new TestCursorAdapter.ResultData();

            /**
             * check if ip address is rechable
             */
            try {
                inetAddress = InetAddress.getByName("192.168.1.2");
            } catch (UnknownHostException e) {
                e.printStackTrace();
                data.setStatus("prefail ==> UnknownHostException");
                testCase.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }

            try {
                if (inetAddress.isReachable(10000)) {
                    Log.d(TAG, "doInBackground: ip address reachable");
                }
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ip address unreachable");
                e.printStackTrace();
                data.setStatus("prefail ==> unreachable IP address");
                testCase.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }
            /**
             * check if socket rechable
             */
            try (Socket sSocket = new Socket()) {
                InetSocketAddress sa = new InetSocketAddress("192.168.1.2", Integer.parseInt(testCase.port));

                sSocket.connect(sa, 1000);           // --> change from 1 to 500 (for example)
                Log.d(TAG, "doInBackground: socket is rechable");

            } catch (Exception e) {
                Log.d(TAG, "doInBackground: socket is unreachable");
                data.setStatus("prefail ==> un reachable Socket");
                testCase.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }


            try {
                Request request = new Request.Builder()
                        .url(UrlTestingUrl)
                        .build();

// ensure the response (and underlying response body) is closed
                try (Response response = testCase.client.newCall(request).execute()) {
                    data.setCode(response.code());
                    Log.d(TAG, "doInBackground: this is responce code inetAddress try catch");
                    testResponce(data, this.resultData);
                    Log.d(TAG, "doInBackground: " + data);
                    return data;
                }

            } catch (IOException e) {
                // TODO: 2/12/2017 handle 3 status: pass. fail. prepare_fail
                data.setStatus("prefail ==> no responce");
                testCase.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }

        }

        // COMPLETED (3) Override onPostExecute to display the results inetAddress the TextView
        @Override
        protected void onPostExecute(TestCursorAdapter.ResultData resultData) {
            Testing = false;
//            testCase.loadingIndicator.setVisibility(View.INVISIBLE);
            if (resultData != null && !resultData.equals("")) {
                if (resultData.exception != null) {
                    testCase.setTestResult("pre_fail");
                } else {

                    System.out.print(resultData);
                }

            }
//            testCase.resultTextView.setText(testCase.getTestResult());
        }

        public void testResponce(TestCursorAdapter.ResultData responceCode, TestCursorAdapter.ResultData expectedCode) {
            if (responceCode.getCode() == expectedCode.getCode()) {
                Log.d(TAG, "onPostExecute: right");
                testCase.setTestResult("pass");
            } else {
                Log.d(TAG, "onPostExecute: false");
                testCase.setTestResult("fail");
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
        }
    }

    private class ResultData {
        private int code;
        private Exception exception;
        private String status;

        public void setCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

}

