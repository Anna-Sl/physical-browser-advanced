package com.sciencework.browser;

import static com.sciencework.browser.utils.PbUtils.fromGson;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.sciencework.browser.data.PbProperties;
import com.sciencework.browser.manager.PbBleManager;
import com.sciencework.browser.manager.PbWifiManager;
import com.sciencework.browser.manager.ScanningManager;

import org.json.JSONObject;

public class AndroidInterface {

    private final ScanningManager scanningManager;

    public AndroidInterface(ScanningManager scanningManager) {
        this.scanningManager = scanningManager;
    }

//    @JavascriptInterface
//    public void scanWifi(boolean sorted) {
//        wifiManager.startScan(forsed=true);
//    }
//
//    @JavascriptInterface
//    public void scanBle(Filter filter, int timeoutInSeconds, boolean sorted) {
//        bleManager.startScan(forsed=true);
//    }

    @JavascriptInterface
    public void scanWifi(String propertiesJson) {
        Log.e("THREAD", "AndroidInterface.scanWifi" + Thread.currentThread());
        Log.e("scanWifi.JSON", propertiesJson);
        PbProperties properties = fromGson(propertiesJson);
        scanningManager.startWifiScan(properties);
    }

    @JavascriptInterface
    public void scanBle(String propertiesJson) {
        Log.e("THREAD", "AndroidInterface.scanBle" + Thread.currentThread());
        Log.e("scanBle.JSON", propertiesJson);
        PbProperties properties = fromGson(propertiesJson);
        scanningManager.startBleScan(properties);
    }

}
