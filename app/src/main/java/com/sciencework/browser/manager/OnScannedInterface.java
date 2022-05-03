package com.sciencework.browser.manager;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.WifiData;

public interface OnScannedInterface {

    void onWifiScanned(WifiData data);

    void onBleScanned(BleData data);

}
