package com.sciencework.browser;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class PbWebViewManager {

    private final AppCompatActivity activity;
    private final WebView webView;
    private final JavascriptInterfaceImpl androidInterface;

    public PbWebViewManager(AppCompatActivity activity) {
        this.activity = activity;
        webView = activity.findViewById(R.id.activity_main_webview);
        androidInterface = new JavascriptInterfaceImpl(activity, webView);

        setupSettings();
        webView.setWebViewClient(createPbWebViewClient());
        loadInitPage();
    }

    public void update(String url) {
        Log.e("PbWebView", "update is clicked");
        webView.clearCache(true);
        webView.loadUrl("about:blank");
        webView.loadUrl(url);
    }

    private PbWebViewClient createPbWebViewClient() {
        ProgressDialog progDialog = ProgressDialog.show(activity, "Loading","Please wait...", true);
        progDialog.setCancelable(false);
        PbWebViewClient pbWebViewClient = new PbWebViewClient (
                () -> {
                    webView.addJavascriptInterface(androidInterface, "androidInterface");
                    new PbWifiTaskManager(activity, webView).startScan();
                    progDialog.show();
                },
                () -> {
                    progDialog.dismiss();
                    new PbLooper().start();
                }
        );
        return pbWebViewClient;
    }

    private void setupSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
    }

    private void loadInitPage() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.e("PbWebView", "loadInitPage");
            webView.loadUrl(activity.getResources().getString(R.string.helloPage));
        }, 1);
    }

}
