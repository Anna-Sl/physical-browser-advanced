package com.sciencework.browser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.sciencework.browser.data.WifiData;

import java.util.ArrayList;
import java.util.List;

public class JavascriptInterfaceImpl {

    private final WifiManager wifiManager;
    private final WebView webView;
    private List<WifiData> wifiContextInfo = new ArrayList<>();
    private boolean sendToWebView = false;

    JavascriptInterfaceImpl(Context context, WebView webView) {
        this.webView = webView;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(initWifiScanReceiver(),
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

    private BroadcastReceiver initWifiScanReceiver() {
        return new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReceive(Context c, Intent intent) {
                if (!intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    return;
                }
                List<WifiData> result = new ArrayList<>();
                for(ScanResult wifi: wifiManager.getScanResults()) {
                    result.add(new WifiData(wifi));
                }
                wifiContextInfo = result;
                if (sendToWebView) {
                    sendNetworksToWebView(result);
                    sendToWebView = false;
                }
            }
        };
    }

    private void sendNetworksToWebView(final List<WifiData> result) {
        final String param = toGson(result);
        final String javascriptCode = "PhysicalBrowserWifiChecker.networksAreScanned('"+param+"');";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(javascriptCode, s -> {});
        } else {
            webView.loadUrl("javascript: " + javascriptCode);
        }
    }

    private String toGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    @JavascriptInterface
    public String getNetworks() {
        return toGson(wifiContextInfo);
    }

    @JavascriptInterface
    public void startScanForAvailableWiFis() {
        wifiManager.startScan();
        sendToWebView = true;
    }

}
