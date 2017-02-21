package com.example.khalk.network;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
                Intent NetworkTestItems = new Intent(MainActivity.this, NetworkItemsActivity.class);
                startActivity(NetworkTestItems);
            }
        });

    }


}

