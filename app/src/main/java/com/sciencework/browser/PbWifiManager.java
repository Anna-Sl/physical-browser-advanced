package com.sciencework.browser;

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

import com.google.gson.Gson;
import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PbWifiManager {

    private final WifiManager wifiManager;
    private final AtomicInteger concurrent = new AtomicInteger(0);
    private final AppCompatActivity context;

    public PbWifiManager(AppCompatActivity context, WebView webView, BlockingQueue<Runnable> queue) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(
                receiver(
                    (networkData) -> networksAreScannedCallback(networkData, webView, queue)
                ),
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );
    }

    public PbWifiManager(AppCompatActivity context, Consumer<NetworkData> networkDataConsumer) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(receiver(networkDataConsumer), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void startScan() {
        Log.e("PbWifiManager", "startScan is called");
        concurrent.incrementAndGet();
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
                if (concurrent.get() == 0) {
                    return;
                } else {
                    concurrent.decrementAndGet();
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

    private void networksAreScannedCallback(NetworkData networkData, WebView webView, BlockingQueue<Runnable> queue) {
        Log.e("Networks are scanned", "registerReceiver");
        String networkDataStr = toGson(networkData);
        queue.add(() -> {
            Log.e("Networks are scanned", "Start calling openedInPhysicalBrowser with " + networkDataStr);
            context.runOnUiThread(() ->
                webView.evaluateJavascript("openedInPhysicalBrowser('" + networkDataStr + "');", null)
            );
        });
    }

    private String toGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

}
