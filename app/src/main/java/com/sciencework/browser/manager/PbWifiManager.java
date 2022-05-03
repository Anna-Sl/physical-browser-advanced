package com.sciencework.browser.manager;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static com.sciencework.browser.utils.PbUtils.isDataFresh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.WifiData;
import com.sciencework.browser.utils.WifiDataPreparation;

import java.util.concurrent.atomic.AtomicInteger;

public class PbWifiManager {
    private final static long WIFI_TIMEOUT_SEC = 30;

    private final Context context;
    private final OnScannedManager onScanned;
    private final OnStateChangedManager onStateChangedManager;
    private WifiManager wifiManager;
    private final AtomicInteger startScanCounter = new AtomicInteger(0);

    private WifiData wifiData;
    private boolean isScanning;

    public PbWifiManager(Context context, OnScannedManager onScanned, OnStateChangedManager onStateChangedManager) {
        this.context = context;
        this.onScanned = onScanned;
        this.onStateChangedManager = onStateChangedManager;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(receiver(), prepareIntentFilter());
        context.registerReceiver(receiver(), prepareIntentFilter());
    }


    public void startScan(boolean forced) {
        if (!wifiManager.isWifiEnabled()) {
            onScanned.onScanned(new WifiData("ERROR: Wi-Fi is disabled!"));
            return;
        }
        Log.e("THREAD", "PbWifiManager.startScan: " + Thread.currentThread());
        if (isScanning) {
            Log.e("PbWifiTaskManager", "ERROR: startScan is called, but isScanning=true");
            return;
        }
        Log.e("PbWifiManager", "forced is " + forced);
        if (!forced && wifiData != null && isDataFresh(wifiData.getTimeMillis(), WIFI_TIMEOUT_SEC)) {
            Log.e("PbWifiManager", "old data is return");
            onScanned.onScanned(wifiData);
            return;
        }
        isScanning = true;
        startScanCounter.incrementAndGet();
        wifiManager.startScan();
    }

    private BroadcastReceiver receiver() {
        return new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context c, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    if (startScanCounter.get() == 0) {
                        return;
                    } else {
                        startScanCounter.decrementAndGet();
                    }
                    Log.e("THREAD", "PbWifiManager.onReceive: " + Thread.currentThread());
                    wifiData = new WifiDataPreparation(context, wifiManager).prepare();
                    onScanned.onScanned(wifiData);
                    isScanning = false;
                }
                if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                    if (state == WIFI_STATE_DISABLED) {
                        onStateChangedManager.onStateChanged(new WifiData(false));
                    }
                    if (state == WIFI_STATE_ENABLED) {
//                        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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

}
