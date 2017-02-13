package com.example.khalk.network2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by khalk on 2/12/2017.
 */

public class TestCase {
    private String testName;
    private String expectedCode;
    private String testResult;
    public String baseUrl;
    public String port;
    private Boolean Testing=false;
    private static final String TAG = TestCase.class.getName();
    OkHttpClient client;
    public ProgressBar loadingIndicator;
    public TextView resultTextView;




    TestCase(String caseName,String expectedCode){
        this.testName=caseName;
        this.expectedCode=expectedCode;
        client=client = new OkHttpClient();
        port="8888";
        baseUrl="192.168.1.4";
    }



    public void setTestName(String testName) {
        this.testName = testName;
    }


    public String getTestName() {
        return testName;
    }


    public void setExpectedCode(String expectedCode) {
        this.expectedCode = expectedCode;
    }


    public String getExpectedCode() {
        return expectedCode;
    }


    public String getTestResult(){
        return this.testResult;
    }
    public void setTestResult(String result){
        this.testResult=result;
    }
    public Boolean isTesting(){
        return Testing;
    }
    public void setTesting(Boolean test){
        this.Testing=test;
    }
    public void setLoadingIndicator(ProgressBar loading){
        this.loadingIndicator=loading;
    }
    public void setResultTextView(TextView viewResult){
        this.resultTextView=viewResult;
    }

    /**
     * this is class to test case
     */
   public void run(){
       String finalUrl = baseUrl + ":" + port + "/"+testName;
//        String finalUrl="http://192.168.1.4:8888/SensoryBoxAPK/AudioVolume/0.8";

        ResultData expectedResultData=new ResultData();
        expectedResultData.setCode(Integer.parseInt(expectedCode));
//        Log.d(TAG, "TestUrl: INteget.parseInt("+expectedResultData.getCode()+")");
        new UrlTesting(expectedResultData).execute(finalUrl);
   }


    public class UrlTesting extends AsyncTask<Object, Object, ResultData> implements DialogInterface.OnCancelListener {
        private ResultData aaa = null;
        private Dialog dialog = null;
        private TextView resutlTextView;
        private TextView pingView;
        String pingResult="";
        Socket socket;
        InetAddress in;



        UrlTesting(ResultData code) {
            this.aaa = code;
            in=null;
        }

        @Override
        protected void onPreExecute() {
            // To disable the whole screen --> setCancelable(false);
            setTesting(true);

//            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black);
//            pingView=(TextView)findViewById(R.id.pingText);
//            dialog.setCancelable(false);
            loadingIndicator.setVisibility(View.VISIBLE);
//            dialog.show();

           // super.onPreExecute();
        }


        @Override
        protected ResultData doInBackground(Object... params) {
            //Object UrlTestingUrl = params[0];

            String UrlTestingUrl= (String) params[0];
            ResultData data = new ResultData();
           // data=null;
            NetworkTestResult preFailSocketUnreachable = NetworkTestResult.PRE_FAIL_SOCKET_UNREACHABLE;
//            dialog.dismiss();



            /**
             * check if ip address is rechable
             */
            try {
//                this.in =InetAddress.getByName("http://192.168.1.4");
                in = InetAddress.getByName("192.168.1.4");
            } catch (UnknownHostException e) {
                e.printStackTrace();
                data.setStatus("prefail");
                data.setException(e);
                return data;
            }

            try {
                if (in.isReachable(10000)) {
                    Log.d(TAG, "doInBackground: ip address reachable");
                }
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ip address unreachable");
                e.printStackTrace();
                data.setStatus("prefail");
                data.setException(e);
                return data;
            }
            /**
             * check if socket rechable
             */
            try (Socket sSocket = new Socket()) {
                InetSocketAddress sa = new InetSocketAddress("192.168.1.4", 8888);

                sSocket.connect(sa, 1000);           // --> change from 1 to 500 (for example)
                // ss.close();
                Log.d(TAG, "doInBackground: socket is rechable");

            } catch (Exception e) {
                Log.d(TAG, "doInBackground: socket is unreachable");
                data.setStatus("prefail");
                data.setException(e);
                return data;
//                preFailSocketUnreachable = NetworkTestResult.PRE_FAIL_SOCKET_UNREACHABLE;
            }


            ResultData resultData = new ResultData();

            try {
                Request request = new Request.Builder()
                        .url(UrlTestingUrl)
                        .build();

// ensure the response (and underlying response body) is closed
                try (Response response = client.newCall(request).execute()) {
//                    data = String.valueOf(response.code());
                    data.setCode(response.code());
                    resultData.setCode(response.code());
                    testResponce(data, this.aaa);

                    Log.d(TAG, "doInBackground: " + data);
                    return data;
                }

            } catch (IOException e) {
                // TODO: 2/12/2017 handle 3 status: pass. fail. prepare_fail
                data.setStatus("prefail");
                data.setException(e);
                return data;
            }

        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(ResultData resultData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            setTesting(false);
            if (resultData != null && !resultData.equals("")) {
                if(resultData.exception != null){
                    setTestResult("pre_fail");

                } else {
                    System.out.print(resultData);
//                    testResponce(resultData, this.aaa);
                }


            }
//            resutlTextView.setText(getTestResult());
        }

        public void testResponce(ResultData responceCode, ResultData expectedCode) {
            if (responceCode.equals(expectedCode)) {
                Log.d(TAG, "onPostExecute: right");
                setTestResult("pass");
//                resutlTextView.setText(getTestResult());
            } else {
                Log.d(TAG, "onPostExecute: false");
                setTestResult("fail");
//                resutlTextView.setText(getTestResult());

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

