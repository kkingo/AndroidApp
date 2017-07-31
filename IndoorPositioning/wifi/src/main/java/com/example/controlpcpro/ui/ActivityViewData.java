package com.example.controlpcpro.ui;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.controlpcpro.R;
import com.wifi.utils.WifiUtils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ActivityViewData extends Activity {

    private TextView mTextView;
    private String wifiInfo = "wifi_info";
    private int count = 0;
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_datas);
        count = getIntent().getBundleExtra("count").getInt("count");
        //Toast.makeText(ActivityViewData.this, "有"+count+"个数据库", Toast.LENGTH_SHORT).show();
        initViews();
    }

    private void initViews() {
        //程序第一次进入时，创建数据库
        mSQLiteDatabase = openOrCreateDatabase("wifi",MODE_PRIVATE,null);
        mTextView = (TextView)findViewById(R.id.id_tv);
        showDataBase();
    }

    private void showDataBase(){
        /*String s = WifiUtils.getData(mSQLiteDatabase, wifiInfo +""+count);
        Toast.makeText(ActivityViewData.this, ""+s, Toast.LENGTH_SHORT).show();*/
        StringBuilder sb = new StringBuilder();
        for (int i =1;i<=count;i++){
            String dbName = wifiInfo+""+i;
            sb.append("第"+i+"次记录为：\n");
            sb.append(WifiUtils.getData(mSQLiteDatabase,dbName));
            sb.append("\n\n\n\n");
        }
        mTextView.setText(sb);
    }
}
