package com.http.utils.listener;

/**
 * Created by King on 2017/7/21.
 */

public class DisposeDataHandler {

    public DisposeDataListener listener = null;
    public Class<?> mClass = null;

    public DisposeDataHandler(DisposeDataListener listener, Class<?> mClass) {
        this.listener = listener;
        this.mClass = mClass;
    }
    public DisposeDataHandler(DisposeDataListener listener){
        this.listener = listener;
    }
}
