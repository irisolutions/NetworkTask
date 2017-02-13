package com.example.khalk.network2;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {
    EditText urlEditText;
    EditText portEditText;
    String url = "192.168.1.2";
    String port = "8888";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startButton = (Button) findViewById(R.id.startButton);
        urlEditText = (EditText) findViewById(R.id.eneteredUrl);
        portEditText = (EditText) findViewById(R.id.eneteredPort);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUrl = urlEditText.getText().toString();
                String enteredPort = portEditText.getText().toString();
                if (enteredUrl != null && enteredUrl != " ") {
                    url = enteredUrl;
                }
                if (enteredPort != null && enteredPort != " ") {
                    port = enteredPort;
                }
                Intent NetworkTestItems = new Intent(MainActivity.this, NetworkItemsActivity.class);
                NetworkTestItems.putExtra("url", url);
                NetworkTestItems.putExtra("port", port);
                startActivity(NetworkTestItems);
            }
        });

    }


}

