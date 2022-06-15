package com.sciencework.browser.manager;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static com.sciencework.browser.utils.PbUtils.isNeedToScan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sciencework.browser.data.WfApInfo;
import com.sciencework.browser.data.WifiData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PbWifiManager {
    public static final long DEFAULT_SCAN_IF_OLDER = 10000;

    private final Context context;
    private final OnScannedManager onScanned;
    private final OnStateChangedManager onStateChangedManager;
    private final WifiManager wifiManager;
    private final AtomicInteger startScanCounter = new AtomicInteger(0);

    private volatile WifiData wifiData;
    private boolean isScanning;

    public PbWifiManager(Context context, OnScannedManager onScanned, OnStateChangedManager onStateChangedManager) {
        this.context = context;
        this.onScanned = onScanned;
        this.onStateChangedManager = onStateChangedManager;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(receiver(), prepareIntentFilter());
        context.registerReceiver(receiver(), prepareIntentFilter());
    }

    public void startScan() {
        startScan(DEFAULT_SCAN_IF_OLDER);
    }

    public void startScan(long scanIfOlder) {
        if (!wifiManager.isWifiEnabled()) {
            onScanned.onScanned(new WifiData("ERROR: Wi-Fi is disabled!"));
            return;
        }
        Log.e("THREAD", "PbWifiManager.startScan: " + Thread.currentThread());
        if (isScanning) {
            Log.e("PbWifiTaskManager", "ERROR: startScan is called, but isScanning=true");
            return;
        }
        Log.e("PbWifiManager", "scanIfOlder is " + scanIfOlder);
        if (wifiData != null && !isNeedToScan(wifiData.getTimeMillis(), scanIfOlder)) {
            Log.e("PbWifiManager", "old data is return");
            onScanned.onScanned(wifiData);
            return;
        }
        isScanning = true;
        startScanCounter.incrementAndGet();
        wifiManager.startScan();
    }

    public WifiData getWifiData() {
        return wifiData;
    }

    private BroadcastReceiver receiver() {
        return new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context c, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    if (!isScanning) {
                        return;
                    }
                    isScanning = false;
                    Log.e("THREAD", "PbWifiManager.onReceive: " + Thread.currentThread());
                    wifiData = new WifiData(true, prepareScannedData(), System.currentTimeMillis());
                    onScanned.onScanned(wifiData);
                }
                if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                    if (state == WIFI_STATE_DISABLED) {
                        onStateChangedManager.onStateChanged(new WifiData(false));
                    }
                    if (state == WIFI_STATE_ENABLED) {
                        onStateChangedManager.onStateChanged(new WifiData(true));
                    }
                }
            }
        };
    }

    private IntentFilter prepareIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        return intentFilter;
    }

    private List<WfApInfo> prepareScannedData() {
        final List<WfApInfo> scanResults = new ArrayList<>();
        for (ScanResult scanResult : wifiManager.getScanResults()) {
            scanResults.add(new WfApInfo(scanResult));
        }
        return scanResults;
    }

}
