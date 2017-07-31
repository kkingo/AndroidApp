package entity;

/**
 * Created by King on 2017/7/30.
 */

public class OnlineFingerPrint {
    private String macAddress ;
    private int rssi;

    public OnlineFingerPrint(String macAddress, int rssi) {
        this.macAddress = macAddress;
        this.rssi = rssi;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
