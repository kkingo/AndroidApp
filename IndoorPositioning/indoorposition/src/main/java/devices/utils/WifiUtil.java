package devices.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import entity.OnlineFingerPrint;

/**
 * Created by King on 2017/7/30.
 */

public class WifiUtil {
    private WifiManager localWifiManager;//提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
    private List<WifiConfiguration> wifiConfigList;//WIFIConfiguration描述WIFI的链接信息，包括SSID、SSID隐藏、password等的设置
    private WifiInfo wifiConnectedInfo;//已经建立好网络链接的信息

    private Context mContext;

    public WifiUtil( Context context){
        mContext = context;
        localWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 创建一个指定名称的数据表
     * @param mSQLiteDatabase
     * @param table
     * @return
     */
    public static SQLiteDatabase createDatabaseIfNotExist(SQLiteDatabase mSQLiteDatabase, String table){
        String CREATE_TABLE = "create table if not exists "+table+"(_id INTEGER, level TEXT,ssid TEXT,bssid TEXT,posx TEXT,posy TEXT)";
        mSQLiteDatabase.execSQL(CREATE_TABLE);

        return mSQLiteDatabase;
    }

    //向数据库插入数据
    public static void insertData(SQLiteDatabase mSQLiteDatabase, ScanResult sr, String table, String x, String y){
        ContentValues cv = new ContentValues();
        cv.put("level",sr.level);
        cv.put("ssid",sr.SSID);
        cv.put("bssid",sr.BSSID);
        cv.put("posx",x);
        cv.put("posy",y);
        mSQLiteDatabase.insert(table, "_id", cv);
        //mSQLiteDatabase.execSQL("insert into "+table+"(_id,level,ssid,bssid,posx,posy) values("+1+","+sr.level+","+sr.SSID+","+sr.BSSID+","+0.9+","+0.5+")");
    }

    public static String getData(SQLiteDatabase mSQLiteDatabase, String table){
        StringBuilder sb = new StringBuilder();
        Cursor cursor = mSQLiteDatabase.query(table, new String[]{"_id,level,ssid,bssid,posx,posy"}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
            String level = cursor.getString(1);//获取第二列的值
            String ssid = cursor.getString(2);//获取第二列的值
            String bssid = cursor.getString(3);//获取第二列的值
            String x = cursor.getString(4);//获取第二列的值
            String y = cursor.getString(5);//获取第二列的值
            //Toast.makeText(this, ""+ssid, Toast.LENGTH_SHORT).show();
            sb.append("   id:"+id+"   level:"+level+"   ssid:"+ssid+"   bssid:"+bssid+"   x:"+x+"   y:"+y+"\n");
        }
        return sb.toString();
    }

    /**
     * 检查WIFI状态
     * @return
     */
    public int WifiCheckState(){
        return localWifiManager.getWifiState();
    }

    /**
     * 开启WIFI
     */
    public void WifiOpen(){
        if(!localWifiManager.isWifiEnabled()){
            localWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 扫描wifi
     */
    public void WifiStartScan(){
        localWifiManager.startScan();
    }

    /**
     * 得到Scan结果
     * @return
     */
    public List<ScanResult> getScanResults(){
        return localWifiManager.getScanResults();//得到扫描结果
    }


    public List<OnlineFingerPrint> getOnlineFingerPrint(){
        List<OnlineFingerPrint> onlineFingerPrints = new ArrayList<OnlineFingerPrint>();
        List<ScanResult> wifiscanResult = new ArrayList<ScanResult>();
        localWifiManager.startScan();
        wifiscanResult = localWifiManager.getScanResults();
        onlineFingerPrints.clear();
        for(int i=0; i<wifiscanResult.size();i++){
            ScanResult strScan = wifiscanResult.get(i);
            OnlineFingerPrint onLineFP = new OnlineFingerPrint(strScan.BSSID,strScan.level);
            onlineFingerPrints.add(onLineFP);
        }
        return onlineFingerPrints;
    }
    /**
     * 得到Wifi配置好的信息
     */
    public void getConfiguration(){
        wifiConfigList = localWifiManager.getConfiguredNetworks();//得到配置好的网络信息
        for(int i =0;i<wifiConfigList.size();i++){
            Log.i("getConfiguration",wifiConfigList.get(i).SSID);
            Log.i("getConfiguration",String.valueOf(wifiConfigList.get(i).networkId));
        }
    }

    //得到建立连接的信息
    public void getConnectedInfo(){
        wifiConnectedInfo = localWifiManager.getConnectionInfo();
    }

    //得到连接的MAC地址
    public String getConnectedMacAddr(){
        return (wifiConnectedInfo == null)? "NULL":wifiConnectedInfo.getMacAddress();
    }

}
