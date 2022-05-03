package com.sciencework.browser.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.sciencework.browser.data.PbProperties;

import java.util.Collection;

public class PbUtils {

    public static String toGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static PbProperties fromGson(String properties) {
        Gson gson = new Gson();
        return gson.fromJson(properties, PbProperties.class);
    }

    public static boolean checkForAccessFineLocation(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isDataFresh(long timeMillis, long timeoutInSeconds) {
        long currentTimeMillis = System.currentTimeMillis();
        long spentSeconds = (int) Math.ceil((double) (currentTimeMillis - timeMillis) / 1000);
        Log.e("TIME IS SPENT", "for timeMillis " + timeMillis + " spent seconds: " + spentSeconds);
        return (currentTimeMillis - timeMillis) < timeoutInSeconds * 1000;
    }

}
