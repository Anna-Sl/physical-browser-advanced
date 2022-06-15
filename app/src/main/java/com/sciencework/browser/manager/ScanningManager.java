package com.sciencework.browser.manager;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.PbOptions;
import com.sciencework.browser.data.WifiData;

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
        wifiManager.startScan();
        bleManager.startScan();
    }

    public void startWifiScan(PbOptions options) {
        if (options.sorted) {
            onScannedManager.activateWifiNeedToSort();
        }
        long scanIfOlder = options.scanIfOlder == null ? PbBleManager.DEFAULT_SCAN_IF_OLDER : options.scanIfOlder;
        wifiManager.startScan(scanIfOlder);
    }

    public void startBleScan(PbOptions options) {
        if (options.sorted) {
            onScannedManager.activateBleNeedToSort();
        }
        long scanIfOlder = options.scanIfOlder == null ? PbBleManager.DEFAULT_SCAN_IF_OLDER : options.scanIfOlder;
        long duration = options.duration == null ? PbBleManager.DEFAULT_SCAN_DURATION : options.duration;
        bleManager.startScan(scanIfOlder, duration);
    }

    public BleData getCachedBleData() {
        return bleManager.getBleData();
    }

    public WifiData getCachedWifiData() {
        return wifiManager.getWifiData();
    }
}
