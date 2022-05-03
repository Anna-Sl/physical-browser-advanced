package com.sciencework.browser.data;

public class NetworkData {
    private final WifiData wifiData;
    private final BleData bleData;

    public NetworkData(WifiData wifiData, BleData bleData) {
        this.wifiData = wifiData;
        this.bleData = bleData;
    }
}
