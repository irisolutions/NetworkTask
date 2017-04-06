package com.example.khalk.network;

import okhttp3.OkHttpClient;

/**
 * Created by khalk on 2/28/2017.
 * assumbtions :
 * desc :
 */

public class  Client {

    private static OkHttpClient client=new OkHttpClient();


    private Client(){}

    public static OkHttpClient getInstance(){
        return client;
    }
}
