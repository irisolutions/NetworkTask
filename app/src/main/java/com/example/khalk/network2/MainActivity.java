package com.example.khalk.network2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    Button audioVolume08Button;
    TextView audioVolume08ResultTextView;
    Button langugeEnButton;
    TextView langugeEnResultTextView;
    Button langugeArButton;
    TextView langugeArResultTextView;
    Button resetButton;
    Button testAllButton;
    private ProgressBar loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioVolume08Button =(Button)findViewById(R.id.audioVolume_0_8_Button);
        audioVolume08ResultTextView =(TextView)findViewById(R.id.audioVolume_0_8_textResult);
        langugeEnButton =(Button)findViewById(R.id.lang_en_Button);
        langugeEnResultTextView =(TextView)findViewById(R.id.lang_en_textResult);
        langugeArButton =(Button)findViewById(R.id.lang_ar_Button);
        langugeArResultTextView =(TextView)findViewById(R.id.lang_ar_textResult);
        resetButton=(Button)findViewById(R.id.resetButton);
        testAllButton=(Button)findViewById(R.id.testAllButton);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        client = new OkHttpClient();

        audioVolume08Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUrl("8888", sensoryBox, "AudioVolume", "0.8","200", audioVolume08ResultTextView.getId());
                Log.d(TAG, "onClick: ");
                
            }
        });

        langugeEnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2/12/2017 prevent multiple button press , make it after post execute-after request-
                TestUrl("8888", sensoryBox, "ChangeLanguage", "en","200", langugeEnResultTextView.getId());
            }
        });

        langugeArButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUrl("8888", sensoryBox, "ChangeLanguage", "ar","200", langugeArResultTextView.getId());
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioVolume08ResultTextView.setText("");
                langugeArResultTextView.setText("");
                langugeEnResultTextView.setText("");
            }
        });

    }

    private void TestUrl(String port, String controller, String method, String para1,String expectedCode, int textViewResultsID) {
        String finalUrl = BaseUrl + ":" + port + controller + method + "/" + para1;
//        String finalUrl="http://192.168.1.4:8888/SensoryBoxAPK/AudioVolume/0.8";
        Log.d(TAG, finalUrl);


        new UrlTesting(expectedCode,textViewResultsID).execute(finalUrl);
    }






    public class UrlTesting extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        private String aaa = null;
        private Dialog dialog = null;
        private TextView resutlTextView;

        UrlTesting(String code,int TextViewID) {
            this.aaa = code;
            resutlTextView=(TextView)findViewById(TextViewID);
        }

        @Override
        protected void onPreExecute() {
            // To disable the whole screen --> setCancelable(false);
            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black);
            dialog.setCancelable(false);
            loadingIndicator.setVisibility(View.VISIBLE);
            dialog.show();

           // super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlTestingUrl = params[0];
            String githubUrlTestingResults = null;

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



