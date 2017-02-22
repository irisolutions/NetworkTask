package com.example.khalk.network;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by khalk on 2/21/2017.
 */

public class CustomLinearLayout extends LinearLayout {
    private String TAG=CustomLinearLayout.class.getName();
    private TestCase testCase;
    private Boolean testing=false;
    private Context mContext;

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TestCase getTestCase(){
        return this.testCase;
    }

    public void setUrlTestingContext(Context c){
        this.mContext=c;
    }

    public void setTestCase(TestCase testCaseObject) {
        this.testCase = testCaseObject;
        Button testButton = (Button) findViewById(R.id.test_button);
        TextView testNameTextView=(TextView)findViewById(R.id.case_name);
        final TextView testResultTextView=(TextView)findViewById(R.id.test_result);
        ProgressBar loadingBarIndicator=(ProgressBar)findViewById(R.id.loading_indicator);
        TextView expectedTextView=(TextView)findViewById(R.id.expected_code);
        testNameTextView.setText(testCase.getTestName());
        expectedTextView.setText(testCase.getExpectedCode());
        testCase.setLoadingIndicator(loadingBarIndicator);
        testCase.setResultTextView(testResultTextView);

        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!testing) {
                    testResultTextView.setText(" ");
                    run(testCase);
                }
            }
        });
    }

    public void run(TestCase test) {
        CustomLinearLayout.ResultData expectedResultData = new CustomLinearLayout.ResultData();
        expectedResultData.setCode(Integer.parseInt(test.getExpectedCode()));
        Log.d(TAG, "run: the final url "+test.finalUrl);
        new CustomLinearLayout.UrlTesting(expectedResultData, test).execute(test.finalUrl);
    }


    public class UrlTesting extends AsyncTask<Object, Object, CustomLinearLayout.ResultData>  implements DialogInterface.OnCancelListener  {
        private CustomLinearLayout.ResultData resultData = null;
        InetAddress inetAddress;
        TestCase testCase;
        private Dialog dialog = null;
       // private Context mContext;

//        public void setContext (Context context){
//            mContext = context;
//        }


        UrlTesting(CustomLinearLayout.ResultData code, TestCase test) {
            this.resultData = code;
            inetAddress = null;
            this.testCase = test;
        }

        @Override
        protected void onPreExecute() {

//            testCase.loadingIndicator.setVisibility(View.VISIBLE);
            testCase.loadingIndicator.setVisibility(View.VISIBLE);

            // To disable the whole screen --> setCancelable(false);
//            dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            dialog = new Dialog(mContext, android.R.style.Theme_Black);
            dialog.setCancelable(false);
            dialog.show();

//            testing=true;
        }

        @Override
        protected CustomLinearLayout.ResultData doInBackground(Object... params) {
            testing = true;
            String UrlTestingUrl = (String) params[0];
            CustomLinearLayout.ResultData data = new CustomLinearLayout.ResultData();
            dialog.dismiss();


            /**
             * check if ip address is rechable
             */
            try {
//                inetAddress = InetAddress.getByName("192.168.1.2");
                inetAddress = InetAddress.getByName(testCase.solidUrl);
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
                InetSocketAddress sa = new InetSocketAddress(testCase.solidUrl, Integer.parseInt(testCase.port));

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
        protected void onPostExecute(CustomLinearLayout.ResultData resultData) {
            testing = false;
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

        public void testResponce(CustomLinearLayout.ResultData responceCode, CustomLinearLayout.ResultData expectedCode) {
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
