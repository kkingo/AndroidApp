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
import com.data.Utils.OnlineFPT;
import com.example.controlpcpro.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.view.FloorPlanView;
import com.wifi.utils.WifiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    final  private  String TAG = "PAG01";
    private Timer mtimer;
    private TimerTask mTimerTask;
    private OkHttpClient okHttpClient;
    private String url;
    private FloorPlanView floorPlanView;

    private  String s;
    private  String str;

    private WifiUtils localWifiUtils;
    private List<OnLineFP> onlinewifiString = new ArrayList<OnLineFP>();
    private List<ScanResult> wifiscanResult ;
    private Map<String,ArrayList> map = new HashMap<String, ArrayList>();

    private Float[][] corri= {{850f,730f,610f,490f,370f},{1060f,1060f,1060f,1060f,1060f}};

    private float currentx=0;
    private float currenty=0;

    private static final MediaType JSON =  MediaType.parse("application/json; charset=utf-8");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page01, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.id_map_layout);
        floorPlanView = new FloorPlanView(getContext());
        floorPlanView.invalidate();
        linearLayout.addView(floorPlanView);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.i(TAG, "setUserVisibleHint: ");
            url = "http://192.168.1.107:3000";

            okHttpClient = new OkHttpClient();

            Request mrequest = new Request.Builder()
                    .url(url+"/api/getfingerprints")
                    .build();
            Call call = okHttpClient.newCall(mrequest);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    final String errorMMessage = e.getMessage();
                    Log.i(TAG, "onFailure: "+errorMMessage);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    str=response.body().string();
                    //成功获取database数组
                    Gson gson = new Gson();
                    JsonParser parser =  new JsonParser();
                    JsonArray jsona = parser.parse(str).getAsJsonArray();
                    //解析json数组
                    for(JsonElement onlinefp : jsona){
                        OnlineFPT monLineFP = gson.fromJson(onlinefp,new TypeToken<OnlineFPT>(){}.getType());
                        Log.i(TAG, "onResponse: "+monLineFP.getMacaddress());
                        Log.i(TAG, "onResponse: "+monLineFP.getrssList());
                        map.put(monLineFP.getMacaddress(),monLineFP.getrssList());
                    }
                    if(map.get("")==null){
                        Log.i(TAG, "onResponse: missed key");
                    }
                    Log.i(TAG, "onResponse: "+map.get("3c:e5:a6:8d:1c:30"));
                }
            });

            mtimer =new Timer();
            mTimerTask= new TimerTask() {
                @Override
                public void run() {
                    scanResultToList(getContext());
                    Log.i(TAG, "run: onlineLeng"+onlinewifiString.size());
                    ArrayList<Double> knnDistance = new ArrayList<Double>();
                    for(int i=0;i<5;i++){
                        knnDistance.add(i,0.0);
                    }
                    for(int i=0; i<onlinewifiString.size(); i++){
                        ArrayList<Double> offlineRSSI = map.get(onlinewifiString.get(i).getMacaddress());
                        if(offlineRSSI!=null){
                            //offline have store this AP
                            Log.i(TAG, "run: Have this AP"+offlineRSSI);
                            for(int j=0;j<offlineRSSI.size();j++){
                                knnDistance.set(j,knnDistance.get(j)+(offlineRSSI.get(j)-onlinewifiString.get(i).getRssi())*(offlineRSSI.get(j)-onlinewifiString.get(i).getRssi()));
                            }
                            Log.i(TAG, "run: knnDisenct"+knnDistance);
                        }
                    }
                    int index=0;
                    Double minknnDistance =knnDistance.get(0);
                    for(int i=1;i< knnDistance.size();i++){
                        if(knnDistance.get(i)<minknnDistance){
                            index = i;
                            minknnDistance = knnDistance.get(i);
                        }
                    }
                    final int locatindex =index;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: indexID"+locatindex);
                            if (currentx!=corri[0][locatindex]||currenty!=corri[1][locatindex]){
                                floorPlanView.addPoint(corri[0][locatindex],corri[1][locatindex]);
                            }
                        }
                    });
                    /*Gson gson =new Gson();
                    String json = gson.toJson(onlinewifiString);

                    Log.i(TAG, "run json:"+json);
                    RequestBody requestBody = RequestBody.create(JSON,json);
                    final Request request = new Request.Builder()
                            .url(url+"/api/onlinefp")
                            .post(requestBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            final String errorMMessage = e.getMessage();
                                Log.i(TAG, "onFailure: ON MAIN THREAD");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                              // Log.i(TAG, "onResponse: Not main thread"+response.body().string());
                                s =response.body().string();
                           // response.receivedResponseAtMillis();
                                Log.i(TAG, "onResponse: "+s);
                                if(Integer.valueOf(s)>-1){
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
                    });*/
                }
            };
            mtimer.schedule(mTimerTask,0,5000);
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

    public void scanResultToList(Context context){
        localWifiUtils = new WifiUtils(context);
        localWifiUtils.WifiStartScan();
        wifiscanResult = localWifiUtils.getScanResults();
        Log.i(TAG, "run: wifiscanLeng"+wifiscanResult.size());
        onlinewifiString.clear();
        Log.d(TAG, "scanResultToList: "+wifiscanResult.size());
        for(int i=0; i<wifiscanResult.size();i++){
            ScanResult strScan = wifiscanResult.get(i);
            OnLineFP onLinewifi = new OnLineFP(strScan.BSSID,strScan.level);
            onlinewifiString.add(onLinewifi);
        }
    }

}
