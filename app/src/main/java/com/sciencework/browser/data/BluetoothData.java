package com.sciencework.browser.data;

import android.net.wifi.ScanResult;

public class BluetoothData {
    public String SSID;

    public BluetoothData(ScanResult bluetooth) {
        this.SSID = bluetooth.SSID;
    }
}
