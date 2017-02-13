package com.example.khalk.network2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
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
    private String testName;
    private String testResult;
    private int loadingProgressBarID;
    private int testButtonID;
    private int resultTextViewID;
    private String expectedCode;
    private String urlTest;

    private static final String TAG = MainActivity.class.getName();
    private int progressBarID;
    OkHttpClient client;

    TestCase(String caseName,String caseResult,int loadingProgressBarID,int testButtonID,String expectedCode){
        this.testName =caseName;
        this.testResult =caseResult;
        this.loadingProgressBarID=loadingProgressBarID;
        this.testButtonID=testButtonID;
        this.expectedCode=expectedCode;
    }


    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestName() {
        return testName;
    }

    public String getTestResult() {
        return testResult;
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





}

