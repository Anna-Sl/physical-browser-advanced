package com.sciencework.browser.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.sciencework.browser.data.BleData;
import com.sciencework.browser.data.BtApInfo;
import com.sciencework.browser.data.PbOptions;
import com.sciencework.browser.data.WfApInfo;
import com.sciencework.browser.data.WifiData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PbUtils {

    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static PbOptions fromJson(String options) {
        Gson gson = new Gson();
        return gson.fromJson(options, PbOptions.class);
    }

    public static String toJson(PbOptions options) {
        Gson gson = new Gson();
        return gson.toJson(options, PbOptions.class);
    }

    public static boolean checkForAccessFineLocation(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isNeedToScan(long timeMillis, long timeoutInMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis - timeMillis) >= timeoutInMillis;
    }

    public static WifiData sort(WifiData data) {
        List<WfApInfo> copied = new ArrayList<>(data.getPoints());
        Collections.sort(copied,Collections.reverseOrder());
        return new WifiData(data.isEnabled(), copied, data.getTimeMillis());
    }

    public static BleData sort(BleData data) {
        List<BtApInfo> copied = new ArrayList<>(data.getPoints());
        Collections.sort(copied,Collections.reverseOrder());
        return new BleData(data.isEnabled(), copied, data.getTimeMillis());
    }


}
