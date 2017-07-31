package com.data.Utils;

/**
 * Created by xia on 2017/6/20.
 */

public class OnLineFP {
    private String Macaddress ;
    private int Rssi;

    public OnLineFP(String macAddress,int rssi){
        this.Macaddress = macAddress;
        this.Rssi = rssi;
    }

    public void setMacaddress(String macaddress){
        this.Macaddress = macaddress;
    }

    public void setRssiList(int rssi){
        this.Rssi = rssi;
    }

    public String getMacaddress(){

        return  this.Macaddress;
    }

    public int getrssList(){
        return  this.Rssi;
    }
}
