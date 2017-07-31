package com.data.Utils;

import java.util.ArrayList;

/**
 * Created by xia on 2017/7/10.
 */

public class OnlineFPT {
    private String Macaddress ;
    private ArrayList<Double> rssiList;

    public OnlineFPT(String macAddress,ArrayList<Double> rssilist){
        this.Macaddress = macAddress;
        this.rssiList = rssilist;
    }

    public void setMacaddress(String macaddress){
        this.Macaddress = macaddress;
    }

    public void setRssiList(ArrayList<Double> rssilist){
        this.rssiList = rssilist;
    }

    public String getMacaddress(){
        return  this.Macaddress;
    }

    public ArrayList<Double> getrssList(){
        return  this.rssiList;
    }

}
