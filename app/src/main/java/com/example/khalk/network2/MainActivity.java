package com.example.khalk.network2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    OkHttpClient client;
    private static final String TAG = MainActivity.class.getName();
    //final static String BASE_URL ="http://192.168.1.4:8888/SensoryBoxAPK/AudioVolume/0.8";
    final static String BaseUrl = "http://192.168.1.4";
    final static String sensoryBox = "/SensoryBoxAPK/";
    Button AudioVolume_08_Button;
    TextView AudioVolume_08_ResultTextView;
    Button Languge_en_Button;
    TextView Languge_en_ResultTextView;
    Button Languge_ar_Button;
    TextView Languge_ar_ResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioVolume_08_Button=(Button)findViewById(R.id.audioVolume_0_8_Button);
        AudioVolume_08_ResultTextView=(TextView)findViewById(R.id.audioVolume_0_8_textResult);
        Languge_en_Button=(Button)findViewById(R.id.lang_en_Button);
        Languge_en_ResultTextView=(TextView)findViewById(R.id.lang_en_textResult);
        Languge_ar_Button=(Button)findViewById(R.id.lang_ar_Button);
        Languge_ar_ResultTextView=(TextView)findViewById(R.id.lang_ar_textResult);
        client = new OkHttpClient();

        AudioVolume_08_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUrl("8888", sensoryBox, "AudioVolume", "0.8","200",AudioVolume_08_ResultTextView.getId());
            }
        });

        Languge_en_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2/12/2017 prevent multiple button press , make it after post execute-after request-
                TestUrl("8888", sensoryBox, "ChangeLanguage", "en","200",Languge_en_ResultTextView.getId());
            }
        });

        Languge_ar_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUrl("8888", sensoryBox, "ChangeLanguage", "ar","200",Languge_ar_ResultTextView.getId());
            }
        });

    }

    private void TestUrl(String port, String controller, String method, String para1,String expectedCode, int textViewResultsID) {
        String finalUrl = BaseUrl + ":" + port + controller + method + "/" + para1;
//        String finalUrl="http://192.168.1.4:8888/SensoryBoxAPK/AudioVolume/0.8";
        Log.d(TAG, finalUrl);


        new UrlTesting(expectedCode,textViewResultsID).execute(finalUrl);
    }






    public class UrlTesting extends AsyncTask<String, Void, String> {
        private String aaa = null;

        private TextView resutlTextView;

        UrlTesting(String code,int TextViewID) {
            this.aaa = code;
            resutlTextView=(TextView)findViewById(TextViewID);
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlTestingUrl = params[0];
            String githubUrlTestingResults = null;

            try {
                Request request = new Request.Builder()
                        .url(UrlTestingUrl)
                        .build();
                Response response = client.newCall(request).execute();
                githubUrlTestingResults = String.valueOf(response.code());
                Log.d(TAG, "doInBackground: " + githubUrlTestingResults);
            } catch (IOException e) {
                // TODO: 2/12/2017 handle 3 status: pass. fail. prepare_fail
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
                resutlTextView.setText("OK");
            } else {
                Log.d(TAG, "onPostExecute: false");
                resutlTextView.setText("ERROR");
            }
//            this.aaa = responceCode;
        }
    }

}



