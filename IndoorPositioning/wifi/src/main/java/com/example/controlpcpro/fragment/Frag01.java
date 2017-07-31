package com.example.controlpcpro.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.controlpcpro.application.MyApplication;
import com.example.controlpcpro.R;
import com.example.controlpcpro.ui.ActivityViewData;
import com.wifi.utils.WifiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Frag01 extends Fragment {

    private ArrayAdapter<String> arrayWifiAdapter;
    private boolean isOnece = true;
    private WifiUtils localWifiUtils;
    private Timer mTimer;
    private List<String> wifiListString = new ArrayList();
    public static List<ScanResult> wifiResultList;
    private Button wifiSearchButton;
    private ListView wifiSearchListView;
    //private Button btn_show;
    public static String wifiInfo = "wifi_info";
    private static int count = 0;
    //private Button btn_save;
    private LinearLayout ll_control;

    public static SQLiteDatabase mSQLiteDatabase;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message paramAnonymousMessage) {
            super.handleMessage(paramAnonymousMessage);
            wifiListString.clear();
            localWifiUtils.WifiStartScan();
            wifiResultList = localWifiUtils.getScanResults();
            scanResultToString(wifiResultList,wifiListString);
            localWifiUtils.getConfiguration();
            if (wifiListString != null) {
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag01, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ll_control = (LinearLayout) view.findViewById(R.id.id_ll_control);
        /*btn_save = (Button)view.findViewById(R.id.id_btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ll_control.setVisibility(View.GONE);
                saveData();
            }
        });*/
        wifiSearchButton = ((Button)view.findViewById(R.id.wifiSearchButton));
        WIFIButtonListener localWIFIButtonListener = new WIFIButtonListener();
        this.wifiSearchButton.setOnClickListener(localWIFIButtonListener);
        this.wifiSearchListView = ((ListView)view.findViewById(R.id.wifiListView));
        this.arrayWifiAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, this.wifiListString);
        this.wifiSearchListView.setAdapter(this.arrayWifiAdapter);
        this.localWifiUtils = new WifiUtils(getActivity());
        this.mTimer = new Timer();

        //程序第一次进入时，创建数据库
        mSQLiteDatabase = getActivity().openOrCreateDatabase("wifi",getActivity().MODE_PRIVATE,null);
        /*//为mSQLiteDatabase创建一个默认表
        WifiUtils.createDatabaseIfNotExist(mSQLiteDatabase, wifiInfo +""+count);*/

        /*btn_show = (Button)view.findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ControlPCMainActivity.this, "有"+count+"个数据库", Toast.LENGTH_SHORT).show();
                showDatas();
            }
        });*/

    }

    public static void showDatas(){
        String s = WifiUtils.getData(mSQLiteDatabase, wifiInfo +""+1);
        //Toast.makeText(MyApplication.getAppContext(),""+s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyApplication.getAppContext(),ActivityViewData.class);
        Bundle bundle = new Bundle();
        bundle.putInt("count",count);
        intent.putExtra("count",bundle);
        MyApplication.getAppContext().startActivity(intent);
    }

    private void refreshOrGetList() {
        if (this.isOnece) {
            this.localWifiUtils.WifiOpen();
            this.isOnece = false;
        }
        this.wifiListString.clear();
        this.localWifiUtils.WifiStartScan();
        this.wifiResultList = this.localWifiUtils.getScanResults();
        this.mHandler.sendMessage(new Message());
    }

    public static void saveData(String x,String y){
        //创建一个表
        count++;
        WifiUtils.createDatabaseIfNotExist(mSQLiteDatabase, wifiInfo +""+count);
        //Toast.makeText(getContext(), "Ok", Toast.LENGTH_SHORT).show();
        //插入数据到当前数据库
        writeScanResultToSQLite(wifiResultList,x,y);
        //scanResultToString(wifiResultList,wifiListString);
    }

    private static void writeScanResultToSQLite(List<ScanResult> listScan,String x,String y){
        //把当前scanResult保存到数据库
        for (ScanResult result:listScan) {
            WifiUtils.insertData(mSQLiteDatabase, result, wifiInfo + "" + count,x,y);
        }
        //Toast.makeText(MyApplication.getAppContext(), "插入数据成功！", Toast.LENGTH_SHORT).show();
    }

    //ScanResult类型转为String
    public void scanResultToString(List<ScanResult> listScan,List<String> listStr){
        /*//将扫描结果写入数据库
        writeScanResultToSQLite(listScan);*/
        for(int i = 0; i <listScan.size(); i++){
            ScanResult strScan = listScan.get(i);
            String str = strScan.level + "dBm        " + strScan.SSID + "    \n" + strScan.BSSID;
            boolean bool = listStr.add(str);
            if(bool){
                arrayWifiAdapter.notifyDataSetChanged();//数据更新,只能单个Item更新，不能够整体List更新
            }
        }
    }

    class WIFIButtonListener implements View.OnClickListener {

        public void onClick(View paramView) {
            //隐藏布局
            ll_control.setVisibility(View.GONE);
            refreshOrGetList();
            mTimer.schedule(new TimerTask() {
                public void run() {
                    mHandler.sendMessage(new Message());
                }
            }, 1000L, 1000L);
        }
    }
}
