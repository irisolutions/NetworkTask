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












//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        audioVolume08Button =(Button)findViewById(R.id.audioVolume_0_8_Button);
//        audioVolume08ResultTextView =(TextView)findViewById(R.id.audioVolume_0_8_textResult);
//        langugeEnButton =(Button)findViewById(R.id.lang_en_Button);
//        langugeEnResultTextView =(TextView)findViewById(R.id.lang_en_textResult);
//        langugeArButton =(Button)findViewById(R.id.lang_ar_Button);
//        langugeArResultTextView =(TextView)findViewById(R.id.lang_ar_textResult);
//        resetButton=(Button)findViewById(R.id.resetButton);
//        testAllButton=(Button)findViewById(R.id.testAllButton);
//        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
//        pingTest=(TextView)findViewById(R.id.pingText);
//        final InetAddress[] inAddress = new InetAddress[1];
//        client = new OkHttpClient();
//
//        audioVolume08Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TestUrl("8888", sensoryBox, "AudioVolume", "0.8","200", audioVolume08ResultTextView.getId());
//                Log.d(TAG, "onClick: ");
//
//
//            }
//        });
//
//        langugeEnButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: 2/12/2017 prevent multiple button press , make it after post execute-after request-
//                TestUrl("8888", sensoryBox, "ChangeLanguage", "en","200", langugeEnResultTextView.getId());
//            }
//        });
//
//        langugeArButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TestUrl("8888", sensoryBox, "ChangeLanguage", "ar","200", langugeArResultTextView.getId());
//            }
//        });
//
//        resetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                audioVolume08ResultTextView.setText("");
//                langugeArResultTextView.setText("");
//                langugeEnResultTextView.setText("");
//            }
//        });
//
//
//
//    }
//
//    private void TestUrl(String port, String controller, String method, String para1,String expectedCode, int textViewResultsID) {
//        String finalUrl = BaseUrl + ":" + port + controller + method + "/" + para1;
////        String finalUrl="http://192.168.1.4:8888/SensoryBoxAPK/AudioVolume/0.8";
//        Log.d(TAG, finalUrl);
//        ResultData expectedResultData=new ResultData();
//        expectedResultData.setCode(Integer.parseInt(expectedCode));
//        Log.d(TAG, "TestUrl: INteget.parseInt("+expectedResultData.getCode()+")");
//        new UrlTesting(expectedResultData,textViewResultsID).execute(finalUrl);
//    }
//
//
//
//
//
//
//    public class UrlTesting extends AsyncTask<Object, Object, ResultData> implements DialogInterface.OnCancelListener {
//        private ResultData aaa = null;
//        private Dialog dialog = null;
//        private TextView resutlTextView;
//        private TextView pingView;
//        String pingResult="";
//        Socket socket;
//        InetAddress in;
//
//
//
//        UrlTesting(ResultData code, int TextViewID) {
//            this.aaa = code;
//            resutlTextView=(TextView)findViewById(TextViewID);
//            in=null;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // To disable the whole screen --> setCancelable(false);
//            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
////            dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black);
//            pingView=(TextView)findViewById(R.id.pingText);
//            dialog.setCancelable(false);
//            loadingIndicator.setVisibility(View.VISIBLE);
//            dialog.show();
//
//           // super.onPreExecute();
//        }
//
//
//        @Override
//        protected ResultData doInBackground(Object... params) {
//            //Object UrlTestingUrl = params[0];
//
//            String UrlTestingUrl= (String) params[0];
//            ResultData data = new ResultData();
//           // data=null;
//            NetworkTestResult preFailSocketUnreachable = NetworkTestResult.PRE_FAIL_SOCKET_UNREACHABLE;
//            dialog.dismiss();
//
//
//
//            /**
//             * check if ip address is rechable
//             */
//            try {
////                this.in =InetAddress.getByName("http://192.168.1.4");
//                in = InetAddress.getByName("192.168.1.4");
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//                data.setStatus("prefail");
//                data.setException(e);
//                return data;
//            }
//
//            try {
//                if (in.isReachable(10000)) {
//                    Log.d(TAG, "doInBackground: ip address reachable");
//                }
//            } catch (IOException e) {
//                Log.d(TAG, "doInBackground: ip address unreachable");
//                e.printStackTrace();
//                data.setStatus("prefail");
//                data.setException(e);
//                return data;
//            }
//            /**
//             * check if socket rechable
//             */
//            try (Socket ss = new Socket()) {
//                InetSocketAddress sa = new InetSocketAddress("192.168.1.4", 8888);
//
//                ss.connect(sa, 1000);           // --> change from 1 to 500 (for example)
//                // ss.close();
//                Log.d(TAG, "doInBackground: socket is rechable");
//
//            } catch (Exception e) {
//                Log.d(TAG, "doInBackground: socket is unreachable");
//                data.setStatus("prefail");
//                data.setException(e);
//                return data;
////                preFailSocketUnreachable = NetworkTestResult.PRE_FAIL_SOCKET_UNREACHABLE;
//            }
//
//
//            ResultData resultData = new ResultData();
//
//            try {
//                Request request = new Request.Builder()
//                        .url(UrlTestingUrl)
//                        .build();
//
//// ensure the response (and underlying response body) is closed
//                try (Response response = client.newCall(request).execute()) {
////                    data = String.valueOf(response.code());
//                    data.setCode(response.code());
//                    resultData.setCode(response.code());
//                    testResponce(data, this.aaa);
//
//                    Log.d(TAG, "doInBackground: " + data);
//                    return data;
//                }
//
//            } catch (IOException e) {
//                // TODO: 2/12/2017 handle 3 status: pass. fail. prepare_fail
//                data.setStatus("prefail");
//                data.setException(e);
//                return data;
//            }
//
//        }
//
//        // COMPLETED (3) Override onPostExecute to display the results in the TextView
//        @Override
//        protected void onPostExecute(ResultData resultData) {
//            loadingIndicator.setVisibility(View.INVISIBLE);
//            if (resultData != null && !resultData.equals("")) {
//                if(resultData.exception != null){
//                    resutlTextView.setText("pre_fail");
//                } else {
//                    System.out.print(resultData);
////                    testResponce(resultData, this.aaa);
//                }
//
//
//            }
//        }
//
//        public void testResponce(ResultData responceCode, ResultData expectedCode) {
//            if (responceCode.equals(expectedCode)) {
//                Log.d(TAG, "onPostExecute: right");
//                resutlTextView.setText("pass");
//            } else {
//                Log.d(TAG, "onPostExecute: false");
//                resutlTextView.setText("fail");
//            }
//        }
//
//        @Override
//        public void onCancel(DialogInterface dialog) {
//            cancel(true);
//        }
//    }
//
//
//    private class ResultData {
//        private int code;
//        private Exception exception;
//        private String status;
//
//        public void setCode(int code) {
//            this.code = code;
//        }
//
//        public int getCode() {
//            return code;
//        }
//
//        public void setException(Exception exception) {
//            this.exception = exception;
//        }
//
//        public Exception getException() {
//            return exception;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//    }
//}
//
//
//
