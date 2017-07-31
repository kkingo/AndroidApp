package entity;

/**
 * Created by King on 2017/7/30.
 */

public class FingerPrint {
    private int referencePointID;
    private double xAxis;
    private double yAxis;
    private  String SSID;
    private String macAddress;
    private double rssi;

    public FingerPrint(int referencePointID, double xAxis, double yAxis, String SSID, String macAddress, double rssi) {
        this.referencePointID = referencePointID;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.SSID = SSID;
        this.macAddress = macAddress;
        this.rssi = rssi;
    }

    public int getReferencePointID() {
        return referencePointID;
    }

    public void setReferencePointID(int referencePointID) {
        this.referencePointID = referencePointID;
    }

    public double getxAxis() {
        return xAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public double getyAxis() {
        return yAxis;
    }

    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }
}
