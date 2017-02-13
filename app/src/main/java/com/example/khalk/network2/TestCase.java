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

    public String solidUrl;
    public String baseUrl;
    public String port;
    public ProgressBar loadingIndicator;
    public TextView resultTextView;
    public View parent;
    public OkHttpClient client;
    public String finalUrl;


    TestCase(String caseName, String expectedCode) {
        this.testName = caseName;
        this.expectedCode = expectedCode;
        port = "8888";
        baseUrl = "http://192.168.1.2";
        solidUrl = "192.168.1.2";
        finalUrl=baseUrl + ":" + port + "/" + testName;
        client = new OkHttpClient();
    }

    TestCase(String caseName, String expectedCode,String url) {
        this.testName = caseName;
        this.expectedCode = expectedCode;
        port = "8888";
        solidUrl = url;
        baseUrl = "http://"+solidUrl;
        finalUrl=baseUrl + ":" + port + "/" + testName;
        client = new OkHttpClient();
    }
    TestCase(String caseName, String expectedCode,String url,String port) {
        this.testName = caseName;
        this.expectedCode = expectedCode;
        this.port = port;
        solidUrl = url;
        baseUrl = "http://"+solidUrl;
        finalUrl=baseUrl + ":" + port + "/" + testName;
        client = new OkHttpClient();
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


    public String getTestResult() {
        return this.testResult;
    }

    public void setTestResult(String result) {
        this.testResult = result;
    }

    public void setLoadingIndicator(ProgressBar loading) {
        this.loadingIndicator = loading;
    }

    public void setResultTextView(TextView viewResult) {
        this.resultTextView = viewResult;
    }

    public TextView getResultTextView() {
        return resultTextView;
    }

    public void setParent(View view) {
        this.parent = view;
    }

}

