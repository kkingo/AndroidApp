package com.example.controlpcpro.fragment;



import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.data.Utils.OnLineFP;
import com.example.controlpcpro.R;
import com.google.gson.Gson;
import com.view.FloorPlanView;
import com.wifi.utils.WifiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Page01 extends Fragment {
    final  private  String TAG = "PAGE01";
    private Timer mtimer;
    private TimerTask mTimerTask;
    private OkHttpClient okHttpClient;
    private String url;
    private FloorPlanView floorPlanView;

    private  String s;
    private  String str;

    private WifiUtils localWifiUtils;
    private List<OnLineFP> onlinewifiString = new ArrayList();
    private List<ScanResult> wifiscanResult ;
    private Float[][] corri= {{850f,730f,610f,490f,370f},{1060f,1060f,1060f,1060f,1060f}};

    private float currentx=0;
    private float currenty=0;

    private static final MediaType JSON =  MediaType.parse("application/json; charset=utf-8");

    private long startTime;
    private long overTime;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page01, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.id_map_layout);
        Log.d(TAG, "onCreateView: get context");
        floorPlanView = new FloorPlanView(getContext());
        floorPlanView.invalidate();
        linearLayout.addView(floorPlanView);
        return view;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.mContext = context;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.i(TAG, "setUserVisibleHint: ");
            url = "http://192.168.1.107:3000";
            okHttpClient = new OkHttpClient();
            mtimer =new Timer();
            mTimerTask= new TimerTask() {
               @Override
              public void run() {
                   localWifiUtils = new WifiUtils(getContext());
                   localWifiUtils.WifiStartScan();
                   wifiscanResult = localWifiUtils.getScanResults();
                   //Log.i(TAG, "run: wifiscanLeng"+wifiscanResult.size());
                   onlinewifiString.clear();
                   scanResultToList(onlinewifiString,wifiscanResult);
                   //Log.i(TAG, "run: onlineLeng"+onlinewifiString.size());

                   Gson gson =new Gson();
                   String json = gson.toJson(onlinewifiString);

                   //Log.i(TAG, "run json:"+json);
                   RequestBody requestBody = RequestBody.create(JSON,json);
                   final Request request = new Request.Builder()
                           .url(url+"/api/onlinefp")
                           .post(requestBody)
                           .build();
                   Call call = okHttpClient.newCall(request);
                   startTime = System.currentTimeMillis();
                   call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            final String errorMMessage = e.getMessage();
                                Log.i(TAG, "onFailure: ON MAIN THREAD");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            overTime = System.currentTimeMillis();
                            long timeCost = overTime - startTime;
                            Log.d(TAG , "cost time = "+String.valueOf(timeCost)+"ms");
                              // Log.i(TAG, "onResponse: Not main thread"+response.body().string());
                                s =response.body().string();
                           // response.receivedResponseAtMillis();
                                Log.i(TAG, "onResponse: "+s);
                                if(Integer.valueOf(s)>-1){
                                    overTime = System.currentTimeMillis();
                                    Log.i(TAG, "onResponse: Not main thread"+s);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //floorPlanView.Located(corri[0][Integer.valueOf(s)],corri[1][Integer.valueOf(s)]);
                                            if((currentx!=corri[0][Integer.valueOf(s)])||(currenty!=corri[1][Integer.valueOf(s)]) ){
                                                floorPlanView.addPoint(corri[0][Integer.valueOf(s)],corri[1][Integer.valueOf(s)]);
                                            }
                                        }
                                    });
                                }
                                //floorPlanView.Located(corri[0][Integer.valueOf(response.body().string())],corri[0][Integer.valueOf(response.body().string())]);
                          }
                    });
                }
            };

            mtimer.schedule(mTimerTask,0,1000);
        }else {
            Log.i(TAG, "outVIs ");
            if(mTimerTask!=null){
                Log.i(TAG, "cancel TimerTask ");
                mTimerTask.cancel();
                mTimerTask =null;
            }
            if(mtimer!=null){
                Log.i(TAG, "cancel Timer ");
                mtimer.cancel();
                mtimer.purge();
                mtimer=null;
            }

        }
    }
    public void scanResultToList(List<OnLineFP> fg, List<ScanResult> wifiSR){
        //将WiFiScanResult添加到List
        //Log.d(TAG, "scanResultToList: "+wifiSR.size());
        for(int i=0; i<wifiSR.size();i++){
            ScanResult strScan = wifiSR.get(i);
            OnLineFP onLinewifi = new OnLineFP(strScan.BSSID,strScan.level);
            fg.add(onLinewifi);
        }
    }

}
