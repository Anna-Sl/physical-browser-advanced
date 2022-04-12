package com.sciencework.browser;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class JavascriptInterfaceImpl {

    private final AppCompatActivity activity;
    private final WebView webView;

    JavascriptInterfaceImpl(AppCompatActivity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    @JavascriptInterface
    public void startScanForAvailableWiFis() {
        //startScan
    }

}
