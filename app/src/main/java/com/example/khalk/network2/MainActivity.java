package com.example.khalk.network2;

import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.button;
import static android.R.attr.port;
//add comment
public class MainActivity extends AppCompatActivity {
    OkHttpClient client;

    private static final String TAG = MainActivity.class.getName();
    //final static String BASE_URL ="http://192.168.0.107:8888/SensoryBoxAPK/AudioVolume/0.8";
//    final static String BaseUrl = "http://192.168.0.107";
    final static String BaseUrl = "http://192.168.16.100";
    final static String sensoryBox = "/SensoryBoxAPK/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        SendUrl("80", sensoryBox, "AudioVolume", "1.1");
//        SendUrl("8888", sensoryBox, "AudioVolume", "0.7");
//        SendUrl("8888", sensoryBox, "AudioVolume", "0.6");
//        SendUrl("8888", sensoryBox, "ChangeLanguage", "en\\aaa");

    }

    private void SendUrl(String port, String sensoryBox, String para1, String para2) {
//        String finalUrl = BaseUrl + ":" + port + sensoryBox + para1 + "/" + para2;
        String finalUrl="http://192.168.16.101:8080/aaaaa/bbbbb/index.html";
        Log.d(TAG, finalUrl);


        new UrlTesting("200").execute(finalUrl);
    }


    public class UrlTesting extends AsyncTask<String, Void, String> {
        public String aaa = null;

        UrlTesting(String code) {
            this.aaa = code;
        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return String.valueOf(response.code());
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlTestingUrl = params[0];
            String githubUrlTestingResults = null;

            try {
                githubUrlTestingResults = run(UrlTestingUrl);
                Log.d(TAG, "doInBackground: " + githubUrlTestingResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return githubUrlTestingResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubUrlTestingResults) {
            if (githubUrlTestingResults != null && !githubUrlTestingResults.equals("")) {
                System.out.print(githubUrlTestingResults);
                testResponce(githubUrlTestingResults, this.aaa);

            }
        }

        public void testResponce(String responceCode, String expectedCode) {
            if (responceCode.equals(expectedCode)) {
                Log.d(TAG, "onPostExecute: right");
            } else {
                Log.d(TAG, "onPostExecute: false");
            }
//            this.aaa = responceCode;
        }
    }

}



