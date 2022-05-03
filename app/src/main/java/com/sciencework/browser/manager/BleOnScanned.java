package com.sciencework.browser.manager;

import static com.sciencework.browser.utils.PbUtils.toGson;

import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.NetworkData;
import com.sciencework.browser.data.WifiData;

public class BleOnScanned implements OnScannedInterface {

    private final AppCompatActivity context;
    private final WebView webView;

    public BleOnScanned(AppCompatActivity context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @Override
    public void onWifiScanned(WifiData wifiData) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void onBleScanned(BleData bleData) {
        Log.e("THREAD", "BleOnScanner.onBleScanner: " + Thread.currentThread());
        Log.e("BleOnScanned", "onBleScanned()");
        final NetworkData data = new NetworkData(null, bleData);
        final String dataStr = toGson(data);
//        PbQueueManager.getInstance().queue().add(() -> {
            Log.e("Networks are scanned", "Start calling __onNetworkScannedInPhysicalBrowser__ with " + dataStr);
            context.runOnUiThread(() -> {
                    Log.e("THREAD", "BleOnScanned.onBleScanned - evaluateJavascript " + Thread.currentThread());
                    webView.evaluateJavascript("__onNetworkScannedInPhysicalBrowser__('" + dataStr + "');", null);
                }
            );
//        });
    }
}
