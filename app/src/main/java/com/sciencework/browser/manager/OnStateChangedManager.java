package com.sciencework.browser.manager;

import static com.sciencework.browser.utils.PbUtils.toJson;

import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

public class OnStateChangedManager {
    private final AppCompatActivity context;
    private final WebView webView;
    public OnStateChangedManager(AppCompatActivity context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    public void onStateChanged(BleData bleData) {
        Log.e("onStateChanged", "BLE: " + toJson(bleData));
        final NetworkData data = new NetworkData(null, bleData);
        onStateChanged(data);
    }
    public void onStateChanged(WifiData wifiData) {
        Log.e("onStateChanged", "Wifi: " + toJson(wifiData));
        final NetworkData data = new NetworkData(wifiData, null);
        onStateChanged(data);
    }

    private void onStateChanged(NetworkData data) {
        final String dataStr = toJson(data);
        Log.e("onStateChanged", "Start calling pbOnAvailabilityChanged with " + dataStr);
        context.runOnUiThread(() -> {
                    Log.e("THREAD", "OnStateChangedManager.evaluateJavascript " + Thread.currentThread());
                    webView.evaluateJavascript("pbOnAvailabilityChanged('" + dataStr + "');", null);
                }
        );
    }

}
