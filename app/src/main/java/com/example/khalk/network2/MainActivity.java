package com.example.khalk.network2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    OkHttpClient client;
    private static final String TAG = MainActivity.class.getName();
    //final static String BASE_URL ="http://192.168.1.4:8888/SensoryBoxAPK/AudioVolume/0.8";
    final static String BaseUrl = "http://192.168.1.4";
    final static String sensoryBox = "/SensoryBoxAPK/";
    Button audioVolume08Button;
    TextView audioVolume08ResultTextView;
    Button langugeEnButton;
    TextView langugeEnResultTextView;
    Button langugeArButton;
    TextView langugeArResultTextView;
    Button resetButton;
    Button testAllButton;
    public TextView pingTest;
    private ProgressBar loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
         setContentView(R.layout.list);
        Log.d(TAG, "onCreate: we are in onCreate method");

        // Create a list of testcases
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();

        testCases.add(new TestCase("SensoryBoxAPK/AudioVolume/0.8","200"));
        testCases.add(new TestCase("SensoryBoxAPK/ChangeLanguage/en","200"));
        testCases.add(new TestCase("SensoryBoxAPK/ChangeLanguage/ar","200"));


        // Create an {@link CaseAdapter}, whose data source is a list of {@link Word}socket. The
        // adapter knows how to create list items for each item in the list.
        CaseAdapter caseAdapter = new CaseAdapter(this, testCases);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.tescase_list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(caseAdapter);




    }
}

