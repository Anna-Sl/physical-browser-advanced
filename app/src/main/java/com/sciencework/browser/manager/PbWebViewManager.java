package com.sciencework.browser.manager;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.AndroidInterface;
import com.sciencework.browser.PbLooper;
import com.sciencework.browser.PbWebViewClient;
import com.sciencework.browser.R;

public class PbWebViewManager {

    private final AppCompatActivity activity;
    private final WebView webView;
    private final AndroidInterface androidInterface;

    private final ScanningManager scanningManager;

    public PbWebViewManager(AppCompatActivity activity) {
        this.activity = activity;
        webView = activity.findViewById(R.id.activity_main_webview);

        OnScannedManager onScannedManager = new OnScannedManager(activity, webView);
        OnStateChangedManager onStateChangedManager = new OnStateChangedManager(activity, webView);
        PbWifiManager wifiManager = new PbWifiManager(activity, onScannedManager, onStateChangedManager);
        PbBleManager bleManager = new PbBleManager(activity, onScannedManager, onStateChangedManager);
        scanningManager = new ScanningManager(onScannedManager, wifiManager, bleManager);
        androidInterface = new AndroidInterface(activity, scanningManager);

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
        final ProgressDialog progDialog = ProgressDialog.show(activity, "Loading","Please wait...", true);
        progDialog.setCancelable(false);

        return new PbWebViewClient (
                () -> {
                    Log.e("THREAD", "PbWebViewClient.onPageStartedProcessor: " + Thread.currentThread());
                    webView.addJavascriptInterface(androidInterface, "androidInterface");
                    scanningManager.startScan();
                    progDialog.show();
                },
                () -> {
                    Log.e("THREAD", "PbWebviewClient.onPageFinishedProcessor: " + Thread.currentThread());
                    progDialog.dismiss();
                    new PbLooper().start();
                }
        );
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

            // code helps when androidInterface stops being found
//            webView.clearCache(true);
//            webView.loadUrl("about:blank");
//            webView.addJavascriptInterface(androidInterface, "androidInterface");
//            webView.clearCache(true);
//            webView.loadUrl("about:blank");

            webView.loadUrl(activity.getResources().getString(R.string.helloPage));
        }, 1);
    }

}
