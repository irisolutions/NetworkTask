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
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by khalk on 2/12/2017.
 */

public class TestCase {
    private String caseName;
    private String caseResult;
    private int loadingProgressBarID;
    private int testButtonID;
    private int resultTextViewID;
    private String expectedCode;
    private String urlTest;

    private static final String TAG = MainActivity.class.getName();
    private ProgressBar loadingIndicator;
    OkHttpClient client;





    TestCase(String caseName,String caseResult,int loadingProgressBarID,int testButtonID,String expectedCode){
        this.caseName=caseName;
        this.caseResult=caseResult;
        this.loadingProgressBarID=loadingProgressBarID;
        this.testButtonID=testButtonID;
        this.expectedCode=expectedCode;
    }


    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setLoadingProgressBarID(int loadingProgressBarID) {
        this.loadingProgressBarID = loadingProgressBarID;
    }

    public void setTestButtonID(int testButtonID) {
        this.testButtonID = testButtonID;
    }

    public int getLoadingProgressBarID() {
        return loadingProgressBarID;
    }

    public int getTestButtonID() {
        return testButtonID;
    }

    public void setExpectedCode(String expectedCode) {
        this.expectedCode = expectedCode;
    }

    public void setUrlTest(String urlTest) {
        this.urlTest = urlTest;
    }

    public String getExpectedCode() {
        return expectedCode;
    }

    public String getUrlTest() {
        return urlTest;
    }

    /**
     * this is class to test case
     */
    public class UrlTesting extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        private String aaa = null;
        private Dialog dialog = null;
        private TextView resutlTextView;
        private TextView pingView;
        String pingResult="";
        Socket s;
        InetAddress in;



        UrlTesting(String code,int TextViewID) {
            this.aaa = code;
//            resutlTextView=(TextView)findViewById(TextViewID);
            in=null;

        }

        @Override
        protected void onPreExecute() {
            // To disable the whole screen --> setCancelable(false);

            // TODO: 2/12/2017 should handle dialog 
//            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//           dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black);
          //  pingView=(TextView)findViewById(R.id.pingText);
            dialog.setCancelable(false);
            loadingIndicator.setVisibility(View.VISIBLE);
            dialog.show();




            // super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            String UrlTestingUrl = params[0];
            String githubUrlTestingResults = null;
            try {
//                this.in =InetAddress.getByName("http://192.168.1.4");
                in=InetAddress.getByName("192.168.1.4");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            try {
                if(in.isReachable(10000)){
                    Log.d(TAG, "doInBackground: ip address reachable");
                }
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ip address unreachable");
                e.printStackTrace();
            }

            dialog.dismiss();







            try {
                Request request = new Request.Builder()
                        .url(UrlTestingUrl)
                        .build();



                Response response = client.newCall(request).execute();
                githubUrlTestingResults = String.valueOf(response.code());
                Log.d(TAG, "doInBackground: " + githubUrlTestingResults);
            } catch (IOException e) {
                // TODO: 2/12/2017 handle 3 status: pass. fail. prepare_fail
                githubUrlTestingResults="existException"+e.getStackTrace().toString();
                e.printStackTrace();
            }


            return githubUrlTestingResults;
        }



        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubUrlTestingResults) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (githubUrlTestingResults != null && !githubUrlTestingResults.equals("")) {
                if(githubUrlTestingResults.startsWith("existException")){
                    resutlTextView.setText("prepare_fail");
                } else {
                    System.out.print(githubUrlTestingResults);
                    testResponce(githubUrlTestingResults, this.aaa);
                }


            }
        }

        public void testResponce(String responceCode, String expectedCode) {
            if (responceCode.equals(expectedCode)) {
                Log.d(TAG, "onPostExecute: right");
                resutlTextView.setText("pass");
            } else {
                Log.d(TAG, "onPostExecute: false");
                resutlTextView.setText("fail");
            }
//            this.aaa = responceCode;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
        }
    }








}

