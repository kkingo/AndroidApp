package com.data.Utils;

/**
 * Created by xia on 2017/6/12.
 */

public class FingerPrint {
    private int RerPID;
    private double Xaxis;
    private double Yaxis;
    private  String SSID;
    private String Macaddress;
    private double Rssi;

    public FingerPrint(int id, double xaxis, double yaxis, String ssid, String macaddress , double rssi){
        RerPID = id;
        Xaxis = xaxis;
        Yaxis = yaxis;
        SSID = ssid;
        Macaddress = macaddress;
        Rssi = rssi ;
    }
}
