package com.sciencework.browser.manager;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;
import static com.sciencework.browser.utils.PbUtils.isDataFresh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.BtApInfo;

import java.util.ArrayList;
import java.util.List;

public class PbBleManager {
    private static final long MAX_SCAN_PERIOD = 5000;
    private static final long SCAN_PERIOD = 2500;
    private static final long BLE_TIMEOUT_SECONDS = 5;

    private boolean enabled;
    private final OnScannedManager onScannedManager;
    private final OnStateChangedManager onStateChangedManager;
    private final Handler scanHandler = new Handler();
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bleScanner;
    private final ScanCallback scanCallback;
    private List<BtApInfo> devices;

    private boolean isScanning;
    private int callsCount = 0;
    private BleData bleData;

    public PbBleManager(Context context, OnScannedManager onScannedManager, OnStateChangedManager onStateChangedManager) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.onStateChangedManager = onStateChangedManager;
        final boolean batchingSupported = bluetoothAdapter.isOffloadedScanBatchingSupported();
        Log.e("BluetoothAdapter", "BluetoothAdapter.isOffloadedScanBatchingSupported() is "+batchingSupported);
        final boolean filteringSupported = bluetoothAdapter.isOffloadedFilteringSupported();
        Log.e("BluetoothAdapter", "BluetoothAdapter.isOffloadedFilteringSupported() is "+filteringSupported);
        this.bleScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.onScannedManager = onScannedManager;
        this.devices = new ArrayList<>();
        this.scanCallback = createScanCallback();

        context.registerReceiver(receiver(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

    }

    public void startScan(boolean forced) {
        startScanWithSettings(forced, SCAN_PERIOD);
    }

    public void startScan(boolean forced, long scanDurationMillis) {
        Log.e("BLE startScan", "scanDurationMillis is " + scanDurationMillis);
        if (scanDurationMillis > MAX_SCAN_PERIOD) {
            Log.d("BLE startScan", "scanDurationMillis is too long: " + scanDurationMillis);
            scanDurationMillis = MAX_SCAN_PERIOD;
        } else if (scanDurationMillis <= 0) {
            scanDurationMillis = SCAN_PERIOD;
        }
        startScanWithSettings(forced, scanDurationMillis);
    }

    private void startScanWithSettings(boolean forced, long scanDurationMillis) {
        if (!bluetoothAdapter.isEnabled()) {
            onScannedManager.onScanned(new BleData("ERROR: Bluetooth is disabled!"));
            return;
        }
        Log.e("THREAD", "PbBleManager.startScan: " + Thread.currentThread());
        if (isScanning) {
            Log.e("PbBleTaskManager", "ERROR: startScan is called, but isScanning=true");
            return;
        }
        Log.e("PbBleManager", "forced is " + forced);
        if (!forced && bleData != null && isDataFresh(bleData.getTimeMillis(), BLE_TIMEOUT_SECONDS)) {
            Log.e("PbBleManager", "old data is return");
            onScannedManager.onScanned(bleData);
            return;
        }
        callsCount = 0;
        isScanning = true;

//        ScanFilter filter = new ScanFilter.Builder().setDeviceName(null).build();

        ArrayList<ScanFilter> filters = new ArrayList<>();
//        filters.add(filter);


        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(SCAN_MODE_LOW_POWER)
                .setReportDelay(scanDurationMillis)
                .build();

        Log.i("BLE","The setting are "+settings.getReportDelayMillis());
//        scanHandler.postDelayed(this::onStopScan, SCAN_PERIOD+1000);

        bleScanner.startScan(filters,settings,scanCallback);
//        bleScanner.startScan(scanCallback);
    }

    private ScanCallback createScanCallback() {
        return new ScanCallback() {
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.i("BLE","onBatchScanResults: " + results.size());
//                Log.i("BLE","onBatchScanResults: " + results);
                if (!isScanning) {
                    Log.e("BLE", "onBatchScanResults: EXTRA CALL");
                    return;
                }
                parseBatchResult(results);
                stopScan();
            }

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.i("BLE","onScanResult: " + result);
                callsCount += 1;
                devices.add(new BtApInfo(result));
            }
        };
    }

    public void stopScan() {
//        Log.e("BLE ScanCallback", "Called next count times: " + callsCount);
        isScanning = false;
        bleScanner.stopScan(scanCallback);
        bleData = new BleData(true, devices, System.currentTimeMillis());
        onScannedManager.onScanned(bleData);
    }

    private void parseBatchResult(List<ScanResult> results) {
        devices = new ArrayList<>();
        for (ScanResult result: results) {
            devices.add(new BtApInfo(result));
        }
    }


    private BroadcastReceiver receiver() {
        return new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context c, Intent intent) {
                String action = intent.getAction();
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                        onStateChangedManager.onStateChanged(new BleData(false));
                    }
                    if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        bleScanner = bluetoothAdapter.getBluetoothLeScanner();
                        onStateChangedManager.onStateChanged(new BleData(true));
                    }
                }
            }
        };
    }

}
