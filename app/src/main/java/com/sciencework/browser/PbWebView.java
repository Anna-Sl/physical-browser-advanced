package com.sciencework.browser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PbWebView {

    private final AppCompatActivity context;
    private final WebView webView;
    private final JavascriptInterfaceImpl androidInterface;
    private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(15);

    public PbWebView(AppCompatActivity activity, ProgressDialog progDialog, String initUrl) {
        context = activity;
        webView = activity.findViewById(R.id.activity_main_webview);
        androidInterface = new JavascriptInterfaceImpl(activity, webView);
        PbWebViewClient pbWebViewClient = new PbWebViewClient(progDialog, view -> addJavascriptInterface(), queue);
        webView.setWebViewClient(pbWebViewClient);
        setupSettings();
        loadInitPage(initUrl);
    }

    public void update(String url) {
        Log.e("PbWebView", "update is clicked");
        webView.clearCache(true);
        webView.loadUrl("about:blank");
        webView.loadUrl(url);
        addJavascriptInterface();
    }

    private void setupSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
    }

    private void loadInitPage(String helloPage) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.e("PbWebView", "loadInitPage");
            webView.loadUrl(helloPage);
            addJavascriptInterface();
        }, 1);
    }

    private void addJavascriptInterface() {
        webView.addJavascriptInterface(androidInterface, "androidInterface");
        new PbWifiManager(context, webView, queue).startScan();
    }

}
