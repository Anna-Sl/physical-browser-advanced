package com.sciencework.browser;

import android.net.wifi.ScanResult;

public class WifiData {
    public String SSID;

    public WifiData(ScanResult wifi) {
        this.SSID = wifi.SSID;
    }
}
