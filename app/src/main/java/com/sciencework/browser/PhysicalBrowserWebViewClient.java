package com.sciencework.browser;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PhysicalBrowserWebViewClient extends WebViewClient {

    private final ProgressDialog progDialog;

    public PhysicalBrowserWebViewClient(ProgressDialog progDialog) {
        this.progDialog = progDialog;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        progDialog.show();
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, final String url) {
        progDialog.dismiss();
        super.onPageFinished(view, url);
    }
}
