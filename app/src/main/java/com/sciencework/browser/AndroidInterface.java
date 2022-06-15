package com.sciencework.browser;

import static com.sciencework.browser.utils.PbUtils.fromJson;
import static com.sciencework.browser.utils.PbUtils.isNeedToScan;
import static com.sciencework.browser.utils.PbUtils.sort;
import static com.sciencework.browser.utils.PbUtils.toJson;

import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.PbOptions;
import com.sciencework.browser.data.WifiData;
import com.sciencework.browser.manager.ScanningManager;

public class AndroidInterface {

    private final AppCompatActivity context;
    private final ScanningManager scanningManager;

    public AndroidInterface(AppCompatActivity context, ScanningManager scanningManager) {
        this.context = context;
        this.scanningManager = scanningManager;
    }

    @JavascriptInterface
    public String getCachedWifiData(String optionsStr) {
        PbOptions options = fromJson(optionsStr);
        Log.e("scan", "AndroidInterface.getCachedWifiData: options " + toJson(options));
        WifiData cachedWifiData = scanningManager.getCachedWifiData();
        if (options.sorted) {
            cachedWifiData = sort(cachedWifiData);
        }
        Log.e("scan", "AndroidInterface.getCachedWifiData returns " + toJson(cachedWifiData));
        return toJson(cachedWifiData);
    }

    @JavascriptInterface
    public String getCachedBleData(String optionsStr) {
        PbOptions options = fromJson(optionsStr);
        Log.e("scan", "AndroidInterface.getCachedBleData: options " + toJson(options));
        BleData cachedBleData = scanningManager.getCachedBleData();
        if (options.sorted) {
            cachedBleData = sort(cachedBleData);
        }
        return toJson(cachedBleData);
    }

    @JavascriptInterface
    public String scanWifi(String optionsStr) {
        Log.e("THREAD", "AndroidInterface.scanWifi" + Thread.currentThread());
        PbOptions options = fromJson(optionsStr);
        Log.e("scan", "AndroidInterface.scanWifi: options " + toJson(options));

        WifiData cachedWifiData = scanningManager.getCachedWifiData();
        if (!isNeedToScan(cachedWifiData.getTimeMillis(), options.scanIfOlder)) {
            if (options.sorted) {
                cachedWifiData = sort(cachedWifiData);
            }
            return toJson(cachedWifiData);
        }

        context.runOnUiThread(() -> {
            scanningManager.startWifiScan(options);
        });
        return null;
    }

    @JavascriptInterface
    public String scanBle(String optionsStr) {
        Log.e("THREAD", "AndroidInterface.scanBle" + Thread.currentThread());
        PbOptions options = fromJson(optionsStr);
        Log.e("scan", "AndroidInterface.scanBle: options " + toJson(options));

        BleData cachedBleData = scanningManager.getCachedBleData();
        if (options.scanIfOlder != null
                && !isNeedToScan(cachedBleData.getTimeMillis(), options.scanIfOlder)) {
            if (options.sorted) {
                cachedBleData = sort(cachedBleData);
            }
            return toJson(cachedBleData);
        }

        context.runOnUiThread(() -> {
            scanningManager.startBleScan(options);
        });
        return null;
    }

}
