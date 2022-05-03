package com.sciencework.browser.data;

import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_CLASSIC;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_DUAL;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_UNKNOWN;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

public class BtApInfo implements Comparable<BtApInfo> {
    public String address;
    public String name;
    // About to remove type. It is usually unknown
    public String type;
    public int rssi;

    public BtApInfo(BluetoothDevice device, short rssi) {
        this.address = device.getAddress();
        this.name = device.getName();
        this.rssi = rssi;
        this.type = parseType(device);
    }

    public BtApInfo(ScanResult result) {
        this.name = result.getDevice().getName();
        this.address = result.getDevice().getAddress();
        this.type = parseType(result.getDevice());
        this.rssi = result.getRssi();
    }

    private String parseType(BluetoothDevice device) {
        switch (device.getType()) {
            case (DEVICE_TYPE_CLASSIC):
                return "classic";
            case (DEVICE_TYPE_LE):
                return "le";
            case (DEVICE_TYPE_DUAL):
                return "dual";
            case (DEVICE_TYPE_UNKNOWN):
            default:
                return "unknown";
        }
    }

    @Override
    public int compareTo(BtApInfo o) {
        return this.rssi - o.rssi;
    }
}
