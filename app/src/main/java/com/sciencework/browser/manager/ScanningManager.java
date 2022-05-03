package com.sciencework.browser.manager;

import com.sciencework.browser.data.PbProperties;

public class ScanningManager {

    private final OnScannedManager onScannedManager;
    private final PbWifiManager wifiManager;
    private final PbBleManager bleManager;

    public ScanningManager(OnScannedManager onScannedManager,
                           PbWifiManager wifiManager,
                           PbBleManager bleManager) {
        this.onScannedManager = onScannedManager;
        this.wifiManager = wifiManager;
        this.bleManager = bleManager;
    }

    public void startScan() {
        onScannedManager.activatePageLoadingMode();
        wifiManager.startScan(false);
        bleManager.startScan(false);
    }

    public void startWifiScan(PbProperties properties) {
        if (properties.sorted) {
            onScannedManager.activateWifiNeedToSort();
        }
        wifiManager.startScan(properties.forced);
    }

    public void startBleScan(PbProperties properties) {
        if (properties.sorted) {
            onScannedManager.activateBleNeedToSort();
        }
        bleManager.startScan(properties.forced, properties.durationMillis);
    }
}
