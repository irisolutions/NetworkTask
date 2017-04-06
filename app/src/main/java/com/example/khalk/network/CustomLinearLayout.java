package com.example.khalk.network;

import android.content.Context;
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

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by khalk on 2/21/2017.
 * assumbtions:
 * desc:
 */

@SuppressWarnings("DefaultFileTemplate")
public class CustomLinearLayout extends LinearLayout {
    private String TAG = CustomLinearLayout.class.getName();
    private TestCaseData testCaseData;
    private Boolean testing = false;
    private IDialogHandler iDialogHandler;
    testCaseUIHandler caseUIHandler;
    private Boolean showDialogON = true;
    private Boolean hideDialogON = true;

    public void setShowDialogON(Boolean showDialogON) {
        this.showDialogON = showDialogON;
    }

    public void setHideDialogON(Boolean hideDialogON) {
        this.hideDialogON = hideDialogON;
    }

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
//

    //public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

    //super(context, attrs, defStyleAttr, defStyleRes);

    //}

    public TestCaseData getTestCaseData() {
        return this.testCaseData;
    }

    /**
     * we assume that this method is only called once
     *
     * @param testCaseDataObject
     */
    public void setTestCaseData(TestCaseData testCaseDataObject) {
        this.testCaseData = testCaseDataObject;
        caseUIHandler = new testCaseUIHandler();
        caseUIHandler.setUITestCase();
        caseUIHandler.initUITestCase();
    }

    public void run(TestCaseData test) {
        CustomLinearLayout.ResultData expectedResultData = new CustomLinearLayout.ResultData();
        expectedResultData.setCode(Integer.parseInt(test.getExpectedCode()));
        Log.d(TAG, "run: the final url " + test.finalUrl);
        new CustomLinearLayout.UrlTesting(expectedResultData, test).execute(test.finalUrl);
    }

    public void setiDialogHandler(IDialogHandler iDialogHandler) {
        this.iDialogHandler = iDialogHandler;
    }


    public class UrlTesting extends AsyncTask<Object, Object, CustomLinearLayout.ResultData> {
        private CustomLinearLayout.ResultData resultData = null;
        InetAddress inetAddress;
        TestCaseData testCaseData;
        Button button = (Button) findViewById(R.id.test_button);
        OkHttpClient client;
//        Client client;


        UrlTesting(CustomLinearLayout.ResultData code, TestCaseData test) {
            this.resultData = code;
            inetAddress = null;
            this.testCaseData = test;
//            client = new OkHttpClient();
             client=Client.getInstance();

        }

        @Override
        protected void onPreExecute() {
            ProgressBar loadingBarIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingBarIndicator.setVisibility(View.VISIBLE);
            if (showDialogON) iDialogHandler.showDialog();
            button.setEnabled(false);
        }

        @Override
        protected CustomLinearLayout.ResultData doInBackground(Object... params) {
            String UrlTestingUrl = (String) params[0];
            CustomLinearLayout.ResultData data;
            data = new ResultData();
            testing = true;
            /**
             * check if ip address is rechable
             */
            try {
                inetAddress = InetAddress.getByName(testCaseData.solidUrl);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                data.setStatus("prefail ==> UnknownHostException");
                testCaseData.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }

            try {
                if (inetAddress.isReachable(2000)) {
                    Log.d(TAG, "doInBackground: ip address reachable");
                }
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ip address unreachable");
                e.printStackTrace();
                data.setStatus("prefail ==> unreachable IP address");
                testCaseData.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }
            /**
             * check if socket rechable
             */
            try (Socket sSocket = new Socket()) {
                InetSocketAddress sa = new InetSocketAddress(testCaseData.solidUrl, Integer.parseInt(testCaseData.port));
// TODO: 2/26/2017 check socket connections
//                sSocket.connect(sa, 1000);           // --> change from 1 to 500 (for example)
                Log.d(TAG, "doInBackground: socket is rechable");

            } catch (Exception e) {
                Log.d(TAG, "doInBackground: socket is unreachable");
                data.setStatus("prefail ==> un reachable Socket");
                testCaseData.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }


            try {
                Request request = new Request.Builder()
                        .url(UrlTestingUrl)
                        .build();
                Log.d(TAG, "doInBackground: url====" + UrlTestingUrl);

// ensure the response (and underlying response body) is closed
//                 Response response;
                Response response=null;

                try {
                    Call call = client.newCall(request);
                    response = call.execute();
                    data.setCode(response.code());
                    Log.d(TAG, "doInBackground: this is responce code inetAddress try catch");
                    testResponce(data, this.resultData);
                    Log.d(TAG, "doInBackground: " + data);
                    return data;
                } finally {

                    if (response != null) {
                        response.close();

                    }
                }

            } catch (IOException e) {
                // TODO: 2/12/2017 handle 3 status: pass. fail. prepare_fail
                data.setStatus("prefail ==> no responce");
                testCaseData.setTestResult(data.getStatus().toString());
                data.setException(e);
                return data;
            }


        }

        // COMPLETED (3) Override onPostExecute to display the results inetAddress the TextView
        @Override
        protected void onPostExecute(CustomLinearLayout.ResultData resultData) {
            if (hideDialogON) iDialogHandler.hideDialog();
            setShowDialogON(true);
            setHideDialogON(true);
            testCaseData.loadingIndicator.setVisibility(View.INVISIBLE);
            button.setEnabled(true);
            testing = false;
            if (resultData != null && !resultData.equals("")) {
                if (resultData.exception != null) {
                    if (testCaseData.getTestResult() == null) {
                        testCaseData.setTestResult("pre_fail");
                    }

                } else {

                    System.out.print(resultData);
                }

            }
            testCaseData.resultTextView.setText(testCaseData.getTestResult());
        }

        private void testResponce(CustomLinearLayout.ResultData responceCode, CustomLinearLayout.ResultData expectedCode) {
            Log.d(TAG, "testResponce: responce code=====" + responceCode.getCode());
            if (responceCode.getCode() == expectedCode.getCode()) {
                Log.d(TAG, "onPostExecute: right");
                testCaseData.setTestResult("pass");
            } else {
                Log.d(TAG, "onPostExecute: false");
                testCaseData.setTestResult("fail");
            }
        }

    }

    private class ResultData {
        private int code;
        private Exception exception;
        private String status;

        void setCode(int code) {
            this.code = code;
        }

        int getCode() {
            return code;
        }

        void setException(Exception exception) {
            this.exception = exception;
        }

        void setStatus(String status) {
            this.status = status;
        }

        String getStatus() {
            return status;
        }
    }

    public class testCaseUIHandler implements ITestCaseData {
        private Button testButton;
        private TextView testNameTextView;
        private TextView testResultTextView;
        ProgressBar loadingBarIndicator;
        TextView expectedTextView;

        @Override
        public void setUITestCase() {
            testButton = (Button) findViewById(R.id.test_button);
            testNameTextView = (TextView) findViewById(R.id.case_name);
            testResultTextView = (TextView) findViewById(R.id.test_result);
            loadingBarIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            expectedTextView = (TextView) findViewById(R.id.expected_code);

        }

        @Override
        public void initUITestCase() {
            testNameTextView.setText(testCaseData.getTestName());
            expectedTextView.setText(testCaseData.getExpectedCode());
            testCaseData.setLoadingIndicator(loadingBarIndicator);
            testCaseData.setResultTextView(testResultTextView);
            testButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!testing) {
                        testResultTextView.setText(" ");
                        run(testCaseData);
                    }

                }
            });

        }
    }


}
