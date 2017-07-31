package com.http.utils;

import android.nfc.Tag;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xia on 2017/6/17.
 */

public class HttpGetThread extends Thread {
    private OkHttpClient mokhttpclent ;
    private String murl;

     private String TAG ="GetThread";
    public HttpGetThread(OkHttpClient okHttpClient, String url){
        mokhttpclent = okHttpClient;
        murl = url;
    }

    public String get() throws IOException {
        Request request = new Request.Builder().url(murl).build();
        

        try (Response response = mokhttpclent.newCall(request).execute()){
            return response.body().string();
        }
    }

    @Override
    public void run() {
        try {
            String rpstr =get();
            Log.i(TAG, "run: "+rpstr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
