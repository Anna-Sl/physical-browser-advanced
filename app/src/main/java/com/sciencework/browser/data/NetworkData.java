package com.sciencework.browser.data;

import java.util.List;

public class NetworkData {
    private final List<WifiData> wifiDataArray;
    private final List<BluetoothData> bluetoothDataArray;

    public NetworkData(List<WifiData> wifiDataArray, List<BluetoothData> bluetoothDataArray) {
        this.wifiDataArray = wifiDataArray;
        this.bluetoothDataArray = bluetoothDataArray;
    }
}
