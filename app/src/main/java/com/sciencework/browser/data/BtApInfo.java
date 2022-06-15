package com.sciencework.browser.data;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

public class BtApInfo implements Comparable<BtApInfo> {
    public String address;
    public String name;
    public int rssi;

    public BtApInfo(BluetoothDevice device, short rssi) {
        this.address = device.getAddress();
        this.name = device.getName();
        this.rssi = rssi;
    }

    public BtApInfo(ScanResult result) {
        this.name = result.getDevice().getName();
        this.address = result.getDevice().getAddress();
        this.rssi = result.getRssi();
    }

    @Override
    public int compareTo(BtApInfo o) {
        return this.rssi - o.rssi;
    }
}
