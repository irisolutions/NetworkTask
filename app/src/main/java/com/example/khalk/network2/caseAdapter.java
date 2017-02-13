package com.example.khalk.network2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by khalk on 2/13/2017.
 * this is adapter for testcases
 */

public class CaseAdapter extends ArrayAdapter<TestCase> {
    private static final String TAG = CaseAdapter.class.getName();
    private ProgressBar loadingIndicator;
    private TextView resultTestTextView;
    private Boolean Testing = false;

    public CaseAdapter(Context context, ArrayList<TestCase> testCases) {
        super(context, 0, testCases);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        final TestCase testCase = getItem(position);
        TextView nameTestTextView = (TextView) listItemView.findViewById(R.id.case_name);
        nameTestTextView.setText(testCase.getTestName());

        loadingIndicator = (ProgressBar) listItemView.findViewById(R.id.loading_indicator);
        testCase.setLoadingIndicator(loadingIndicator);

        Button testButton = (Button) listItemView.findViewById(R.id.test_button);
        resultTestTextView = (TextView) listItemView.findViewById(R.id.test_result);
        testCase.setResultTextView(resultTestTextView);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Testing) {
                    run(testCase);
                }
            }
        });

        return listItemView;
    }

    public void run(TestCase test) {
        ResultData expectedResultData = new ResultData();
        expectedResultData.setCode(Integer.parseInt(test.getExpectedCode()));
        Log.d(TAG, "run: the final url "+test.finalUrl);
        new UrlTesting(expectedResultData, test).execute(test.finalUrl);
    }


    public class UrlTesting extends AsyncTask<Object, Object, ResultData> implements DialogInterface.OnCancelListener {
        private ResultData resultData = null;
        InetAddress inetAddress;
        TestCase testCase;

        UrlTesting(ResultData code, TestCase test) {
            this.resultData = code;
            inetAddress = null;
            this.testCase = test;
        }

        @Override
        protected void onPreExecute() {
            testCase.loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ResultData doInBackground(Object... params) {
            Testing = true;
            String UrlTestingUrl = (String) params[0];
            ResultData data = new ResultData();

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
        protected void onPostExecute(ResultData resultData) {
            Testing = false;
            testCase.loadingIndicator.setVisibility(View.INVISIBLE);
            if (resultData != null && !resultData.equals("")) {
                if (resultData.exception != null) {
                    testCase.setTestResult("pre_fail");
                } else {

                    System.out.print(resultData);
                }

            }
            testCase.resultTextView.setText(testCase.getTestResult());
        }

        public void testResponce(ResultData responceCode, ResultData expectedCode) {
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
