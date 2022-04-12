package com.sciencework.browser;

import static com.sciencework.browser.utils.PbUtils.toGson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PbWifiTaskManager {

    private final WifiManager wifiManager;
    private final AtomicInteger startScanCounter = new AtomicInteger(0);
    private final AppCompatActivity context;

    public PbWifiTaskManager(AppCompatActivity context, WebView webView) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(
                receiver(
                    networkData -> networksAreScannedCallback(networkData, webView)
                ),
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );
    }

    public void startScan() {
        Log.e("PbWifiManager", "startScan is called");
        startScanCounter.incrementAndGet();
        wifiManager.startScan();
    }

    private BroadcastReceiver receiver(Consumer<NetworkData> processor) {
        return new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context c, Intent intent) {
                if (!intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    return;
                }
                if (startScanCounter.get() == 0) {
                    return;
                } else {
                    startScanCounter.decrementAndGet();
                }
                List<WifiData> wifiDataArray = extractWifiDataArray(wifiManager.getScanResults());
                NetworkData networkData = new NetworkData(wifiDataArray, null);
                processor.accept(networkData);
            }
        };
    }

    private List<WifiData> extractWifiDataArray(List<ScanResult> scanResults) {
        List<WifiData> result = new ArrayList<>();
        for(ScanResult scanResult: scanResults) {
            result.add(new WifiData(scanResult));
        }
        return result;
    }

    private void networksAreScannedCallback(NetworkData networkData, WebView webView) {
        Log.e("Networks are scanned", "registerReceiver");
        String networkDataStr = toGson(networkData);
        PbNetworkManager.getInstance().queue().add(() -> {
            Log.e("Networks are scanned", "Start calling openedInPhysicalBrowser with " + networkDataStr);
            context.runOnUiThread(() ->
                webView.evaluateJavascript("openedInPhysicalBrowser('" + networkDataStr + "');", null)
            );
        });
    }


}
