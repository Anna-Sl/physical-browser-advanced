package com.sciencework.browser.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.BtApInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BluetoothDataPreparation {

    private final BluetoothAdapter btAdapter;
    private final HashMap<String, BtApInfo> macAddressToApInfo;

    public BluetoothDataPreparation(HashMap<String, BtApInfo> map,  BluetoothAdapter btAdapter) {
        this.btAdapter = btAdapter;
        this.macAddressToApInfo = map;
    }

    public BleData prepare() {
        return null;
//        return new BleData(btAdapter.isEnabled(), macAddressToApInfo.values());
    }

}
