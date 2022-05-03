package com.sciencework.browser.manager;

import static com.sciencework.browser.utils.PbUtils.toGson;

import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

import java.util.Collections;

public class OnScannedManager {

    private final AppCompatActivity context;
    private final WebView webView;

    private boolean pageIsLoading;
    private WifiData wifiData;
    private BleData bleData;
    private boolean wifiNeedToSort;
    private boolean bleNeedToSort;

    public OnScannedManager(AppCompatActivity context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    public void activatePageLoadingMode() {
        pageIsLoading = true;
    }

    public void activateWifiNeedToSort() {
        wifiNeedToSort = true;
    }

    public void activateBleNeedToSort() {
        bleNeedToSort = true;
    }

    public void onScanned(WifiData wifiData) {
        Log.e("THREAD", "OnScannedManager.onWifiScanned: " + Thread.currentThread());
        this.wifiData = wifiData;
        if (pageIsLoading) {
            if (bleData != null) {
                callOnPageLoadedInPhysicalBrowser();
            }
            return;
        }
        sortIfNeed(wifiData);
        callOnNetworkScannedInPhysicalBrowser(wifiData, null);
    }

    private void sortIfNeed(WifiData wifiData) {
        if (wifiNeedToSort) {
            Log.e("SORT", "OnScannedManager.sortWifiIfNeed before: " + toGson(wifiData.getScanned()));
            if (wifiData.getScanned() != null)
                Collections.sort(wifiData.getScanned(), Collections.reverseOrder());
            Log.e("SORT", "OnScannedManager.sortWifiIfNeed after: " + toGson(wifiData.getScanned()));
            wifiNeedToSort = false;
        }
    }

    public void onScanned(BleData bleData) {
        Log.e("THREAD", "OnScannedManager.onBleScanned: " + Thread.currentThread());
        this.bleData = bleData;
        if (pageIsLoading) {
            if (wifiData != null) {
                callOnPageLoadedInPhysicalBrowser();
            }
            return;
        }
        sortIfNeed(bleData);
        callOnNetworkScannedInPhysicalBrowser(null, bleData);
    }

    private void sortIfNeed(BleData bleData) {
        if (bleNeedToSort) {
            Log.e("SORT", "OnScannedManager.sortBleIfNeed before: " + toGson(bleData.getScanned()));
            if (bleData.getScanned() != null)
                Collections.sort(bleData.getScanned(),Collections.reverseOrder());
            Log.e("SORT", "OnScannedManager.sortBleIfNeed after: " + toGson(bleData.getScanned()));
            bleNeedToSort = false;
        }
    }

    private void callOnNetworkScannedInPhysicalBrowser(WifiData wifiData, BleData bleData) {
        final NetworkData data = new NetworkData(wifiData, bleData);
        final String dataStr = toGson(data);
        Log.e("Wifi or Ble scanned", "Start calling __onNetworkScannedInPhysicalBrowser__ with " + dataStr);
        context.runOnUiThread(() -> {
                    Log.e("THREAD", "OnScannedManager.evaluateJavascript " + Thread.currentThread());
                    webView.evaluateJavascript("__onNetworkScannedInPhysicalBrowser__('" + dataStr + "');", null);
                }
        );
    }

    private void callOnPageLoadedInPhysicalBrowser() {
        Log.e("THREAD", "OnScannedManager.onScanned: " + Thread.currentThread());
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
        // clear every data of all website scans, because page was loaded
        pageIsLoading = false;
        wifiNeedToSort = false;
        bleNeedToSort = false;
        wifiData = null;
        bleData = null;
        Log.e("NetworkScannedManager", "onScanned() is finished");
    }

}
