package entity;

import java.util.ArrayList;

/**
 * Created by King on 2017/7/30.
 */

public class AccessPiont {
    private String macAddress ;
    private ArrayList<Double> rssiList;

    public AccessPiont(String macAddress, ArrayList<Double> rssiList) {
        this.macAddress = macAddress;
        this.rssiList = rssiList;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public ArrayList<Double> getRssiList() {
        return rssiList;
    }

    public void setRssiList(ArrayList<Double> rssiList) {
        this.rssiList = rssiList;
    }
}
