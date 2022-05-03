package com.sciencework.browser.utils;

import static com.sciencework.browser.utils.PbUtils.checkForAccessFineLocation;
import static com.sciencework.browser.utils.PbUtils.toGson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.sciencework.browser.data.WfApInfo;
import com.sciencework.browser.data.ConnectedWfInfo;
import com.sciencework.browser.data.WifiData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WifiDataPreparation {

    private final Context context;
    private final WifiManager wm;
    private Map<String, WfApInfo> mapBssidToWfData;
//    private String connectedBssid;

    public WifiDataPreparation(Context context, WifiManager wm) {
        this.context = context;
        this.wm = wm;
    }

    public WifiData prepare() {
//        this.mapBssidToWfData = prepareMapBssidToWfData();
//        this.connectedBssid = getConnectedBssid();
        return new WifiData(wm.isWifiEnabled(), prepareScannedData(), System.currentTimeMillis());
    }

//    private Collection<WfApInfo> prepareScannedWifiApArray() {
//        // set known=true
//        final Collection<String> knownBssid = getKnownBssid();
//        final Collection<String> scannedBssid = getScannedBssid();
//        knownBssid.retainAll(scannedBssid);
//        for (String bssid: knownBssid) {
//            mapBssidToWfData.get(bssid).known = true;
//        }
//
//        // set connected=true
//        if (connectedBssid != null && mapBssidToWfData.get(connectedBssid) != null) {
//            mapBssidToWfData.get(connectedBssid).connected = true;
//        }
//        return mapBssidToWfData.values();
//    }



    private List<WfApInfo> prepareScannedData() {
        final List<WfApInfo> scanResults = new ArrayList<>();
        for (ScanResult scanResult : wm.getScanResults()) {
            scanResults.add(new WfApInfo(scanResult));
        }
        return scanResults;
    }
//    private Map<String, WfApInfo> prepareMapBssidToWfData() {
//        final List<ScanResult> scanResults = wm.getScanResults();
////        Log.e("NETWORKS", "prepareMapBssidToWfData: " + toGson(scanResults));
//        final Map<String, WfApInfo> mapBssidToWfData = new HashMap<>();
//        for (ScanResult scanResult : scanResults) {
//            mapBssidToWfData.put(scanResult.BSSID, new WfApInfo(scanResult));
//        }
//        return mapBssidToWfData;
//    }

//    private Collection<String> getKnownBssid() {
//        final Set<String> knownBssid = new HashSet<>();
//        if (! checkForAccessFineLocation(context)) {
//            return knownBssid;
//        }
//        @SuppressLint("MissingPermission")
//        final List<WifiConfiguration> configs = wm.getConfiguredNetworks();
//        for (WifiConfiguration config: configs) {
//            knownBssid.add(config.BSSID);
//        }
//        return knownBssid;
//    }
//
//    private Collection<String> getScannedBssid() {
//        final Set<String> scannedBssid = new HashSet<>();
//        final List<ScanResult> scanResults = wm.getScanResults();
//        for (ScanResult result: scanResults) {
//            scannedBssid.add(result.BSSID);
//        }
//        return scannedBssid;
//    }

//    private String getConnectedBssid() {
//        return wm.getConnectionInfo().getBSSID();
//    }
}
