package okhttps;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by King on 2017/7/30.
 */

public class OkHttpClients {
    private OkHttpClient mOkHttpClient;
    private Request request;

    public OkHttpClients(int timeOut,boolean redirect) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(redirect);
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public OkHttpClients() {
        this.mOkHttpClient = new OkHttpClient();
    }
    public void sendGetRequest(String url , RequestParams params , DisposeDataListener listener){
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        Request request = new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new JsonCallBack(listener));
    }

    public void sendPostRequest(String url, RequestParams params, DisposeDataListener listener){
        FormBody.Builder mBuild = new FormBody.Builder();
        for(Map.Entry<String,String> entry : params.urlParams.entrySet()){
            mBuild.add(entry.getKey(),entry.getValue());
        }
        FormBody formBody = mBuild.build();
        Call call = mOkHttpClient.newCall(new Request.Builder().url(url).post(formBody).build());
        call.enqueue(new JsonCallBack(listener));
    }
}
