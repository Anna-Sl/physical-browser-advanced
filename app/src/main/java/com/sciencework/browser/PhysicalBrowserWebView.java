package com.sciencework.browser;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class PhysicalBrowserWebView {

    private final WebView webView;
    private final JavascriptInterfaceImpl androidInterface;

    public PhysicalBrowserWebView(AppCompatActivity activity, ProgressDialog progDialog) {
        webView = activity.findViewById(R.id.activity_main_webview);
        webView.setWebViewClient(new PhysicalBrowserWebViewClient(progDialog));
        androidInterface = new JavascriptInterfaceImpl(activity, webView);
        setupSettings();
        loadInitPage(activity.getResources().getString(R.string.helloPage));
    }

    public void update(String url) {
        webView.clearCache(true);
        webView.reload();
        webView.loadUrl("about:blank");
        webView.loadUrl(url);
        webView.addJavascriptInterface(androidInterface, "androidInterface");
    }

    private void setupSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
    }

    private void loadInitPage(String helloPage) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            webView.loadUrl(helloPage);
            webView.addJavascriptInterface(androidInterface, "androidInterface");
        }, 1);
    }

}
