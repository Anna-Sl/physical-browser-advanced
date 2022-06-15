package com.sciencework.browser.data;

import android.net.wifi.ScanResult;

public class WfApInfo implements Comparable<WfApInfo> {

    public String name;
    public String address;
    public int rssi;

    public WfApInfo(ScanResult wifi) {
        this.name = wifi.SSID;
        this.address = wifi.BSSID;
        this.rssi = wifi.level;
    }

    public WfApInfo(WfApInfo o) {
        this.name = o.name;
        this.address = o.address;
        this.rssi = o.rssi;
    }

    @Override
    public int compareTo(WfApInfo e) {
        return this.rssi - e.rssi;
    }
}
