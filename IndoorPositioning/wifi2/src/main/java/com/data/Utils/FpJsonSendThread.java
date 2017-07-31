package com.data.Utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xia on 2017/6/12.
 */

public class FpJsonSendThread extends Thread{
        private static final MediaType JSON =  MediaType.parse("application/json; charset=utf-8");
        private OkHttpClient mokhttpclent ;
        private String murl;
        private String mjson;

        public FpJsonSendThread(OkHttpClient okHttpClient, String url, String json){
            this.mokhttpclent = okHttpClient;
            murl = url;
            mjson = json ;
        }

        public  String post() throws IOException {
            RequestBody body = RequestBody.create(JSON, mjson);
            Request request = new Request.Builder()
                    .url(murl)
                    .post(body)
                    .build();
            try (Response response = mokhttpclent.newCall(request).execute()) {
                return response.body().string();
            }
        }

        public void run(){
            try {
                post();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
