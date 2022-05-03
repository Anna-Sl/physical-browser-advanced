package com.sciencework.browser.data;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WfApInfo implements Comparable<WfApInfo>{
    public static int NUMBER_OF_LEVELS = 5;

    public String name;
    public String address;
//    public String capabilities;
    public int signalStrength;
    public int relSignalStrength;
//    public boolean connected;
//    public boolean known;

    public WfApInfo(ScanResult wifi) {
        this.name = wifi.SSID;
        this.address = wifi.BSSID;
//        this.capabilities = wifi.capabilities;
        this.signalStrength = wifi.level;
        this.relSignalStrength = WifiManager.calculateSignalLevel(wifi.level, NUMBER_OF_LEVELS);
    }

    public WfApInfo(WfApInfo o) {
        this.name = o.name;
        this.address = o.address;
//        this.capabilities = o.capabilities;
        this.signalStrength = o.signalStrength;
        this.relSignalStrength = o.relSignalStrength;
//        this.connected = o.connected;
//        this.known = o.known;
    }

    @Override
    public int compareTo(WfApInfo e) {
        return this.signalStrength - e.signalStrength;
    }
}
