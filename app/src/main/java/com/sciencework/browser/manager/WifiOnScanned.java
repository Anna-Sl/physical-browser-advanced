package com.sciencework.browser.manager;

import static com.sciencework.browser.utils.PbUtils.toGson;

import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

public class WifiOnScanned implements OnScannedInterface {

    private final AppCompatActivity context;
    private final WebView webView;

    public WifiOnScanned(AppCompatActivity context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @Override
    public void onWifiScanned(WifiData wifiData) {
        Log.e("THREAD", "WifiOnScanned.onWifiScanned " + Thread.currentThread());
        Log.e("WifiOnScanned", "onWifiScanned()");
        final NetworkData data = new NetworkData(wifiData, null);
        final String dataStr = toGson(data);
//        PbQueueManager.getInstance().queue().add(() -> {
            Log.e("Networks are scanned", "Start calling __onNetworkScannedInPhysicalBrowser__ with " + data);
            context.runOnUiThread(() -> {
                    Log.e("THREAD", "in onWifiScanned - webView.evaluateJavascript " + Thread.currentThread());
                    webView.evaluateJavascript("__onNetworkScannedInPhysicalBrowser__('" + dataStr + "');", null);
                }
            );
//        });
    }

    @Override
    public void onBleScanned(BleData bleData) {
        throw new RuntimeException("Not implemented");
    }
}
