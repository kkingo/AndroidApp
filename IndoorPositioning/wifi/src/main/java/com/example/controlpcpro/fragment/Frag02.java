package com.example.controlpcpro.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.Utils.FingerPrint;
import com.data.Utils.OnLineFP;
import com.data.Utils.OnlineFPT;
import com.example.controlpcpro.R;
import com.example.controlpcpro.adapter.LVAdapter;
import com.example.controlpcpro.bean.Info;
import com.example.controlpcpro.ui.MyMainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.http.utils.HttpGetThread;
import com.wifi.utils.WifiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

public class Frag02 extends Fragment{
    private Info mData=new Info();
    private Button testbutton;
    private Button collectbutton;
    private EditText ipaddress;
    private EditText rporder;
    private EditText rpXaxis;
    private EditText rpYaxis;
    private EditText collecttimes;
    private String url;
    private String str;
/*    private final static int CONNECT_TIMEOUT =60;
    private final static int READ_TIMEOUT=100;
    private final static int WRITE_TIMEOUT=60;
    private static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                                                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                                                    .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                                                    .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间
                                                    .build();*/
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final MediaType JSON =  MediaType.parse("application/json; charset=utf-8");
    private String TAG ="Fg02";
    public enum Status{
        COLLECTED,NOCELLECTED
    }
    public enum INTStatus{
        TESTED,NOTESTED
    }
    private Timer timer;
    private int tmp=1;

    private WifiUtils localWifiUtils;
    private List<FingerPrint> wifiListString = new ArrayList();
    private List<ScanResult> wifiscanResult ;
    private long startTime ;
    private long overTime;
    //默认未收集
    private Status mStatus = Status.NOCELLECTED;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag02, container, false);
        localWifiUtils = new WifiUtils(getActivity());
        init(view);
        return view;
    }

    private void init(View view) {
        testbutton = (Button) view.findViewById(R.id.btn_test);
        collectbutton = (Button) view.findViewById(R.id.btn_start);
        ipaddress = (EditText)view.findViewById(R.id.edit_IPadd);
        rporder = (EditText) view.findViewById(R.id.edit_RPID);
        rpXaxis = (EditText) view.findViewById(R.id.edit_RPX);
        rpYaxis= (EditText) view.findViewById(R.id.edit_PRY);
        collecttimes = (EditText) view.findViewById(R.id.edit_CTime);
        TestButtonListener tbtnListener = new TestButtonListener();
        testbutton.setOnClickListener(tbtnListener);
        CollectButtonListener cbtnListener = new CollectButtonListener();
        collectbutton.setOnClickListener(cbtnListener);
        if(mData.getEnumStatus()==Status.NOCELLECTED){
            collectbutton.setText("开始");
        }else {
            collectbutton.setText("上传");
            collectbutton.setBackgroundColor(Color.RED);
        }

        /*List<Info> mDatas = new ArrayList<Info>();
        for (int i = 1;i <= 36;i++){
            Info info = new Info();
            info.setOrder(i+"");
            //info.setStatus("待搜集数据");
            info.setEnumStatus(Status.NOCELLECTED);
            mDatas.add(info);
        }
        ListView lv = (ListView) view.findViewById(R.id.id_rv);

        lv.setAdapter(new LVAdapter(getActivity(),mDatas));*/

        /*RecyclerView lv = (RecyclerView) view.findViewById(R.id.id_rv);

        //设置布局管理器
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置adapter
        lv.setAdapter(new RVAdapter(mDatas,getActivity()));
        //设置Item增加、移除动画
        lv.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        lv.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));*/
    }
    public void scanResultToList(List<FingerPrint> fg, List<ScanResult> wifiSR){
        //将WiFiScanResult添加到List
        int rporder_int = Integer.valueOf(rporder.getText().toString());
        Double rpXaxis_double = Double.valueOf(rpXaxis.getText().toString());
        Double rpYaxia_double = Double.valueOf(rpYaxis.getText().toString());
        Log.d(TAG, "scanResultToList: "+rporder_int);
        Log.d(TAG, "scanResultToList: "+wifiSR.size());
        for(int i=0; i<wifiSR.size();i++){
            ScanResult strScan = wifiSR.get(i);
            FingerPrint wififg = new FingerPrint(rporder_int,rpXaxis_double,rpYaxia_double,strScan.SSID,strScan.BSSID,strScan.level);
            // FingerPrint wififg = new FingerPrint(3,10.0,10.0,strScan.SSID,strScan.BSSID,strScan.level);
            fg.add(wififg);
        }
    }
    class TestButtonListener implements View.OnClickListener{
        //网络测试按钮监听器
        @Override
        public void onClick(View v) {
            if(ipaddress.getText().toString().trim().equals("")){
                url="http://192.168.8.172:3000";
            }else {
                url = "http://"+ipaddress.getText()+":3000";
            }
            Log.i(TAG, "onClick: "+url);
            Request mrequest = new Request.Builder()
                    .url(url+"/api/linktest")
                    .build();
            Call call =  mOkHttpClient.newCall(mrequest);
            startTime = System.currentTimeMillis();
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    final String errorMMessage = e.getMessage();
                    if(Looper.getMainLooper().getThread()==Thread.currentThread()){
                        Log.d(TAG, "onFailure: ON MAIN THREAD");
                    }else {
                        Log.d(TAG, "onFailure: Not main thread");
                    }
                    final  View view =getView();
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            MyDialog("连接服务器测试失败");
                        }
                    });
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    overTime = System.currentTimeMillis();
                    Log.d(TAG,"cost time = " +String.valueOf(overTime - startTime)+"ms");
                    if(Looper.getMainLooper().getThread()==Thread.currentThread()){
                        Log.d(TAG, "onResponse: ON MAIN THREAD");
                    }else {
                        str = response.body().string();
                        Log.d(TAG, "onResponse: Not main thread"+str);
                    }
                    //str = response.body().string();
                    // Log.i(TAG, "onResponse: "+str);
                    //Map map = gson.fromJson(str,Map.class);
                    //Log.i(TAG, "onResponse: map lentth"+map.size());
                    final View view =getView();
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            MyDialog("连接服务器测试成功");
                        }
                    });
                    mData.setIntStatus(INTStatus.TESTED);
                }
            });
        }
    }

    class CollectButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(mData.getIntStatus()==INTStatus.NOTESTED){
                MyDialog("请先进行网络测试");
            }
            else{
                if((!rpYaxis.getText().toString().trim().equals(""))&&(!rpXaxis.getText().toString().trim().equals(""))&&(!rporder.getText().toString().trim().equals(""))){
                    if(mData.getEnumStatus()==Status.NOCELLECTED){
                            //弹出对话框
                            final LinearLayout ll_loading, ll_console;
                            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setCancelable(true);
                            final LayoutInflater inflater = getActivity().getLayoutInflater();
                            final View view = inflater.inflate(R.layout.dialog_main_info,null);
                            ll_console = (LinearLayout) view.findViewById(R.id.id_console);
                            ll_loading = (LinearLayout) view.findViewById(R.id.id_loading);
                            final Button btn_cancel2 = (Button) view.findViewById(R.id.id_btn_cancel2);
                            final Button btn_cancel1 = (Button) view.findViewById(R.id.id_btn_cancel);
                            final Button btn_ok = (Button) view.findViewById(R.id.id_btn_ok);
                            final TextView tv_time = (TextView) view.findViewById(R.id.id_tv_time);
                            final TextView tv_status = (TextView) view.findViewById(R.id.id_tv_status);
                            btn_cancel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //timer.cancel();
                                    //wifiListString.clear();
                                    alertDialog.dismiss();
                                }
                            });
                            timer = new Timer();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //saveData();
                                    tmp = Integer.valueOf(collecttimes.getText().toString())-1 ;
                                    ll_console.setVisibility(View.GONE);
                                    ll_loading.setVisibility(View.VISIBLE);
                                    wifiListString.clear();
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tv_time.setText("" + tmp + "S");
                                                    localWifiUtils.WifiStartScan();
                                                    wifiscanResult = localWifiUtils.getScanResults();
                                                    scanResultToList(wifiListString,wifiscanResult);
                                                    tmp--;
                                                    if (tmp == -1) {
                                                        timer.cancel();
                                                        tv_status.setText("数据已经搜集完毕");
                                                        tv_status.setTextColor(Color.parseColor("#FF0000"));
                                                        btn_cancel2.setText("确定");
                                                        collectbutton.setBackgroundColor(Color.RED);
                                                        collectbutton.setText("上传");
                                                        collectbutton.setTextColor(Color.WHITE);
                                                        mData.setEnumStatus(Status.COLLECTED);
                                                        //---------------
                                                    }
                                                }
                                            });
                                        }
                                    }, 0, 500);
                                }
                            });

                            btn_cancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    timer.cancel();
                                    // wifiListString.clear();
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.setView(view);
                            alertDialog.show();
                        }else{
                            //上传
                            Log.d(TAG,"start upLoad");
                            sendWithPieces();
                        }
               }else {
                MyDialog("请先输入有效的位置参数");
                }
            }
        }
    }
    private void MyDialog(String setText){
        final AlertDialog success = new AlertDialog.Builder(getActivity()).create();
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_test_info,null);
        final TextView textView = (TextView) view.findViewById(R.id.dial_succful);
        final Button button = (Button) view.findViewById(R.id.id_dialog_btn_ok);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                success.dismiss();
            }
        });
        textView.setText(setText);
        success.setView(view);
        success.show();
    }
    private void sendWithPieces(){
/*        List<List<FingerPrint>> pieces = new ArrayList<List<FingerPrint>>();
        Log.d(TAG ,String.valueOf(wifiListString.size()));
        if(wifiListString.size() >= 100) {
            int arrSize = wifiListString.size()%100==0? (wifiListString.size()/100):(wifiListString.size()/100+1);
            List<FingerPrint> piece = new ArrayList<FingerPrint>();
            piece.add(wifiListString.get(0));
            for(int i = 1; i < wifiListString.size(); i++){
                if(i%100 != 0){
                    piece.add(wifiListString.get(i));
                }
                if(i%100 == 0){
                    pieces.add(piece);
                    piece = new ArrayList<FingerPrint>();
                }
                if(i == wifiListString.size()-1 ) {
                    pieces.add(piece);
                }
            }
        }
        Gson gson = new Gson();
        List<String> jsonStrings = new ArrayList<String>();
        for(List<FingerPrint> piece : pieces){
            String jsonString = gson.toJson(piece);
            jsonStrings.add(jsonString);
        }*/
   /*     for(String str : jsonStrings){
            RequestBody body = RequestBody.create(JSON,str);
            Request request = new Request.Builder()
                    .url(url+"/api/offlinefp")
                    .post(body)
                    .build();
            Log.d(TAG,str);
            Call call =  mOkHttpClient.newCall(request);
            call.enqueue(upLoadCallBack);
        }*/
        Log.d(TAG ,String.valueOf(wifiListString.size()));
        Gson gson = new Gson();
        String str = gson.toJson(wifiListString);
        RequestBody body = RequestBody.create(JSON,str);
        Request request = new Request.Builder()
                .url(url+"/api/offlinefp")
                .post(body)
                .build();
        Log.d(TAG,str);
        Call call =  mOkHttpClient.newCall(request);
        call.enqueue(upLoadCallBack);
    }
    Callback upLoadCallBack =new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            final String errorMMessage = e.getMessage();
            if(Looper.getMainLooper().getThread()==Thread.currentThread()){
                Log.d(TAG, "onFailure: ON MAIN THREAD");
            }else {
                Log.d(TAG, "onFailure: Not main thread"+errorMMessage);
            }
            final  View view =getView();
            view.post(new Runnable() {
                @Override
                public void run() {
                    MyDialog("上传数据失败，请重试");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(Looper.getMainLooper().getThread()==Thread.currentThread()){
                Log.d(TAG, "onResponse: ON MAIN THREAD");
            }else {
                Log.d(TAG, "onResponse: Not main thread");
            }
            final View view =getView();
            view.post(new Runnable() {
                @Override
                public void run() {
                    MyDialog("数据上传成功");
                    collectbutton.setText("开始");
                    collectbutton.setBackgroundColor(Color.GREEN);
                    mData.setEnumStatus(Status.NOCELLECTED);
                }
            });
        }
    };
}
