package com.sciencework.browser.utils;

import com.google.gson.Gson;

public class PbUtils {

    public static String toGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

}
