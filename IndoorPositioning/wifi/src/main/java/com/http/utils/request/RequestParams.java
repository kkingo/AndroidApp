package com.http.utils.request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by King on 2017/7/21.
 */

public class RequestParams {

    public ConcurrentHashMap<String,String> urlParams = new ConcurrentHashMap<String,String>();
    public ConcurrentHashMap<String,Object> fileParams = new ConcurrentHashMap<String, Object>();
    public RequestParams(){
        this((Map<String, String>) null);
    }

    public RequestParams(Map<String,String> source) {
        if(source != null){
            for(Map.Entry<String,String> entry : source.entrySet()){
                put(entry.getKey(),entry.getValue());
            }
        }
    }

    public RequestParams(final String key ,final String value){
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    public void put(String key ,String value){
        if(key != null && value !=null)
            urlParams.put(key,value);
    }

    public void put(String key ,Object value){
        if(key != null && value != null)
            fileParams.put(key,value);
    }
}
