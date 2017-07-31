package com.http.utils.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;

/**
 * Created by King on 2017/7/21.
 */

public class CommonRequest {

    public static Request createPostRequest(String url , RequestParams params){
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if(params !=null){
            for (Map.Entry<String,String> entry : params.urlParams.entrySet()){
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        return new Request.Builder().url(url).post(mFormBody).build();
    }

    public static Request createGetRequest(String url , RequestParams params){
        StringBuilder requestBuilder = new StringBuilder(url).append("?");
        if(params != null ){
            for(Map.Entry<String,String > entry : params.urlParams.entrySet()){
                requestBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(requestBuilder.substring(0,requestBuilder.length()-1)).get().build();
    }

    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createMultiPostRequest(String url , RequestParams params){
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if(params != null){
            for(Map.Entry<String , Object> entry : params.fileParams.entrySet()){

            }
        }
        return null;
    }
}
