package com.example.khalk.network;

import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by khalk on 2/12/2017.
 * assumbtions:
 * desc:
 */

class TestCaseData {
    private String testName;
    private String expectedCode;
    private String testResult;
    String solidUrl;
    String port;
    ProgressBar loadingIndicator;
    TextView resultTextView;
//    OkHttpClient client;
    String finalUrl;


    TestCaseData(String caseName, String expectedCode, String url, String port) {
        this.testName = caseName;
        this.expectedCode = expectedCode;
        this.port = port;
        solidUrl = url;
        String baseUrl = "http://" + solidUrl;
        finalUrl = baseUrl + ":" + port + "/" + testName;
//        client = new OkHttpClient();
    }


    String getTestName() {
        return testName;
    }


    String getExpectedCode() {
        return expectedCode;
    }


    String getTestResult() {
        return this.testResult;
    }

    void setTestResult(String result) {
        this.testResult = result;
    }

    void setLoadingIndicator(ProgressBar loading) {
        this.loadingIndicator = loading;
    }

    void setResultTextView(TextView viewResult) {
        this.resultTextView = viewResult;
    }

//    public TextView getResultTextView() {
//        return resultTextView;
//    }

}

