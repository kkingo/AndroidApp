package com.example.controlpcpro.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.data.Utils.FingerPrint;
import com.data.Utils.FpJsonSendThread;
import com.example.controlpcpro.bean.Info;
import com.example.controlpcpro.R;
import com.example.controlpcpro.fragment.Frag02;
import com.google.gson.Gson;
import com.wifi.utils.WifiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/5/22.
 */

public class LVAdapter extends BaseAdapter {

    private List<Info> mDatas = new ArrayList<Info>();
    private LayoutInflater mInflater;
    private View mView;
    private Activity mContext;
    private Timer timer;

    private WifiUtils localWifiUtils;
    private List<FingerPrint> wifiListString = new ArrayList();
    private List<ScanResult> wifiscanResult ;

    private  String TAG ="LVATAG";

    private FpJsonSendThread jsonSend;

    private static int count = 0;

    int tmp = 1;

    public LVAdapter(Activity context, List<Info> datas) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.lv_item, null);
        mDatas = datas;
        mContext = context;
        localWifiUtils = new WifiUtils(context);

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        //if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lv_item, null);
            viewHolder.order = (TextView) convertView.findViewById(R.id.id_order);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.id_tv);
            viewHolder.tv_souji = (TextView) convertView.findViewById(R.id.id_tv_souji);
            convertView.setTag(viewHolder);
        /*} else {
            viewHolder = (ViewHolder) convertView.getTag();
        }*/

        viewHolder.msg.setText(mDatas.get(position).getStatus());
        viewHolder.order.setText(mDatas.get(position).getOrder());
        if (mDatas.get(position).getEnumStatus() == Frag02.Status.NOCELLECTED) {
            viewHolder.tv_souji.setText("搜集数据");
            viewHolder.msg.setText("未搜集数据");
        } else if (mDatas.get(position).getEnumStatus() == Frag02.Status.COLLECTED){
            viewHolder.tv_souji.setText("上传数据");
            viewHolder.tv_souji.setBackgroundColor(Color.RED);
            viewHolder.msg.setText("已搜集数据");
            viewHolder.msg.setTextColor(Color.BLACK);
        }

        viewHolder.tv_souji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDatas.get(position).getEnumStatus() == Frag02.Status.COLLECTED) {
                    //如果已经搜集数据，那么就查看
                    //showDatas();
                    try {
                        upLoadData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //弹出Dialog框
                    final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setCancelable(true);

                    final LayoutInflater inflater = mContext.getLayoutInflater();
                    final View v = inflater.inflate(R.layout.dialog_main_info, null);

                    final LinearLayout ll_loading, ll_console;

                    ll_console = (LinearLayout) v.findViewById(R.id.id_console);
                    ll_loading = (LinearLayout) v.findViewById(R.id.id_loading);

                    final Button btn_cancel2 = (Button) v.findViewById(R.id.id_btn_cancel2);

                    final TextView tv_time = (TextView) v.findViewById(R.id.id_tv_time);
                    final TextView tv_status = (TextView) v.findViewById(R.id.id_tv_status);

                    v.findViewById(R.id.id_btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //timer.cancel();
                            alertDialog.dismiss();
                        }
                    });

                    v.findViewById(R.id.id_btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //saveData();
                            tmp = 1;
                            ll_loading.setVisibility(View.VISIBLE);
                            ll_console.setVisibility(View.GONE);

                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            //String[] strings = getPos(position);
                                            //saveData(strings[0],strings[1]);
                                            tv_time.setText("" + tmp + "S");
                                            localWifiUtils.WifiStartScan();
                                            wifiscanResult = localWifiUtils.getScanResults();
                                            scanResultToList(wifiListString,wifiscanResult);
                                            tmp--;
                                            if (tmp == 0) {
                                                timer.cancel();
                                                Toast.makeText(mContext, "数据搜集完毕", Toast.LENGTH_SHORT).show();
                                                tv_status.setText("数据已经搜集完毕");
                                                tv_status.setTextColor(Color.parseColor("#FF0000"));
                                                btn_cancel2.setText("确定");
                                                //---------------
                                                mDatas.get(position).setEnumStatus(Frag02.Status.COLLECTED);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }, 0, 1000);
                            //alertDialog.dismiss();

                        }
                    });

                    btn_cancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.setView(v);
                    alertDialog.show();
                }
            }
        });

        return convertView;
    }

    private String[] getPos(int pos){
        String[] strings = new String[2];
        float x = 0.9f;
        float y = (pos + 1)*0.5f;

        strings[0] = x+"";
        strings[1] = y+"";

        return strings;
    }

    public void scanResultToList(List<FingerPrint> fg, List<ScanResult> wifiSR){
        //将WiFiScanResult添加到List

            for(int i=0; i<wifiSR.size();i++){
                    ScanResult strScan = wifiSR.get(i);
                    FingerPrint wififg = new FingerPrint(2,5.0,5.0,strScan.SSID,strScan.BSSID,strScan.level);
                    fg.add(wififg);
            }
    }
    public  void  upLoadData() throws IOException {
        String url = "http://192.168.1.100:3000/api/offlinefp";
        Gson gson = new Gson();
        String json = gson.toJson(wifiListString);
        jsonSend = new FpJsonSendThread(new OkHttpClient(),url,json);
        Log.d(TAG, "upLoadData: "+json);
        jsonSend.start();
    }

    class ViewHolder {
        TextView order, msg, tv_souji;
    }

    /*public void saveData(){
        //创建一个表
        count++;
        WifiUtils.createDatabaseIfNotExist(mSQLiteDatabase, wifiInfo +""+count);
        //Toast.makeText(getContext(), "Ok", Toast.LENGTH_SHORT).show();
        //插入数据到当前数据库
        writeScanResultToSQLite(wifiResultList);
        //scanResultToString(wifiResultList,wifiListString);
    }

    public void writeScanResultToSQLite(List<ScanResult> listScan){
        //把当前scanResult保存到数据库
        for (ScanResult result:listScan) {+
            WifiUtils.insertData(mSQLiteDatabase, result, wifiInfo + "" + count);
        }
        Toast.makeText(mContext, "插入数据成功！", Toast.LENGTH_SHORT).show();
    }*/
}
