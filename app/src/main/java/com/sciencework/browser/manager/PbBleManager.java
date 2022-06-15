package com.sciencework.browser.manager;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;
import static com.sciencework.browser.utils.PbUtils.isNeedToScan;

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
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.BtApInfo;

import java.util.ArrayList;
import java.util.List;

public class PbBleManager {
    private static final long MAX_SCAN_DURATION = 5000;
    public static final long DEFAULT_SCAN_DURATION = 2500;
    public static final long DEFAULT_SCAN_IF_OLDER = 5000;

    private final OnScannedManager onScannedManager;
    private final OnStateChangedManager onStateChangedManager;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bleScanner;
    private final ScanCallback scanCallback;
    private List<BtApInfo> devices;

    private boolean isScanning;
    private volatile BleData bleData;

    public PbBleManager(Context context, OnScannedManager onScannedManager, OnStateChangedManager onStateChangedManager) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.onStateChangedManager = onStateChangedManager;
        this.bleScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.onScannedManager = onScannedManager;
        this.devices = new ArrayList<>();
        this.scanCallback = createScanCallback();
        context.registerReceiver(receiver(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    public void startScan() {
        startScanWithSettings(DEFAULT_SCAN_IF_OLDER, DEFAULT_SCAN_DURATION);
    }

    public void startScan(long scanIfOlder, long duration) {
        Log.e("BLE startScan", "duration is " + duration);
        if (duration > MAX_SCAN_DURATION) {
            Log.d("BLE startScan", "duration is too long: " + duration);
            duration = MAX_SCAN_DURATION;
        } else if (duration <= 0) {
            duration = DEFAULT_SCAN_DURATION;
        }
        startScanWithSettings(scanIfOlder, duration);
    }

    public BleData getBleData() {
        return bleData;
    }

    private void startScanWithSettings(long scanIfOlder, long scanDurationMillis) {
        Log.e("startScan", "bleScanner status is: " + bluetoothAdapter.getState());

        if (!bluetoothAdapter.isEnabled()) {
            onScannedManager.onScanned(new BleData("ERROR: Bluetooth is disabled!"));
            return;
        }
        Log.e("THREAD", "PbBleManager.startScan: " + Thread.currentThread());
        if (isScanning) {
            Log.e("PbBleTaskManager", "ERROR: startScan is called, but isScanning=true");
            return;
        }
        Log.e("PbBleManager", "scanIfOlder is " + scanIfOlder);
        if (bleData != null && !isNeedToScan(bleData.getTimeMillis(), scanIfOlder)) {
            Log.e("PbBleManager", "old data is return");
            onScannedManager.onScanned(bleData);
            return;
        }
        isScanning = true;

        ArrayList<ScanFilter> filters = new ArrayList<>();
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(SCAN_MODE_LOW_POWER)
                .setReportDelay(scanDurationMillis)
                .build();

        bleScanner.startScan(filters, settings, scanCallback);
    }

    private ScanCallback createScanCallback() {
        return new ScanCallback() {
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.i("BLE","onBatchScanResults: " + results.size());
                if (!isScanning) {
                    Log.e("BLE", "onBatchScanResults: EXTRA CALL");
                    return;
                }
                parseBatchResult(results);
                isScanning = false;
                bleScanner.stopScan(scanCallback);
                bleData = new BleData(true, devices, System.currentTimeMillis());
                onScannedManager.onScanned(bleData);
            }
        };
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
