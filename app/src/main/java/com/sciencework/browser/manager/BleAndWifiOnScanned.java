package com.sciencework.browser.manager;

import static com.sciencework.browser.utils.PbUtils.toGson;

import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

public class BleAndWifiOnScanned implements OnScannedInterface {

    private final AppCompatActivity context;
    private final WebView webView;
    private final Handler handler = new Handler();
    private WifiData wifiData;
    private BleData bleData;

    public BleAndWifiOnScanned(AppCompatActivity context, WebView webView) {
        this.context = context;
        this.webView = webView;

    }

    public void startScan() {
//        new PbWifiManager(context, this).startScan();
//        new PbBleManager(context, this).startScan();
    }

    @Override
    public void onWifiScanned(WifiData data) {
        Log.e("THREAD", "BleAndWifiOnScanned.onWifiScanned: " + Thread.currentThread());
//        handler.post(() -> {
            Log.e("THREAD", "BleAndWifiOnScanned.onWifiScanned inside Handler: " + Thread.currentThread());
            Log.e("NetworkScannedManager", "onWifiScanned is started");
            wifiData = data;
            if (bleData != null) {
                onScanned();
            }
            Log.e("NetworkScannedManager", "onWifiScanned is finished");
//        });
    }

    @Override
    public void onBleScanned(BleData data) {
        Log.e("THREAD", "BleAndWifiOnScanned.onBleScanned: " + Thread.currentThread());
//        handler.post(() -> {
            Log.e("THREAD", "BleAndWifiOnScanned.onBleScanned inside Handler: " + Thread.currentThread());
            Log.e("NetworkScannedManager", "onBleScanned is started");
            bleData = data;
            if (wifiData != null) {
                onScanned();
            }
            Log.e("NetworkScannedManager", "onBleScanned is finished");
//        });
    }

    private void onScanned() {
        Log.e("THREAD", "BleAndWifiOnScanned.onScanned: " + Thread.currentThread());
        Log.e("NetworkScannedManager", "onScanned() is started");
        final NetworkData data = new NetworkData(wifiData, bleData);
        final String networkDataStr = toGson(data);
        PbQueueManager.getInstance().queue().add(() -> {
            Log.e("THREAD", "queue.add(): " + Thread.currentThread());
            Log.e("Networks are scanned", "Start calling __onPageLoadedInPhysicalBrowser__ with " + networkDataStr);
            context.runOnUiThread(() -> {
                    Log.e("THREAD", "about to load __onPageLeadedInPhysicalBrowser: " + Thread.currentThread());
                    webView.evaluateJavascript("__onPageLoadedInPhysicalBrowser__('" + networkDataStr + "');", null);
                }
            );
        });
        Log.e("NetworkScannedManager", "onScanned() is finished");
    }
}
