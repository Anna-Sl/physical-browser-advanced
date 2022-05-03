package com.sciencework.browser;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PbWebViewClient extends WebViewClient {

    private final Runnable processorOnPageStarted;
    private final Runnable processorOnPageFinished;

    public PbWebViewClient(Runnable processorOnPageStarted, Runnable processorOnPageFinished) {
        this.processorOnPageStarted = processorOnPageStarted;
        this.processorOnPageFinished = processorOnPageFinished;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e("PbWebViewClient", "shouldOverrideUrlLoading. Url: " + url);
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.e("THREAD", "PbWebViewClient.onPageStarted: " + Thread.currentThread());
        processorOnPageStarted.run();
        Log.e("PbWebViewClient", "page is started. Url: " + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, final String url) {
        Log.e("THREAD", "PbWebViewClient.onPageFinished: " + Thread.currentThread());
        super.onPageFinished(view, url);
        Log.e("PbWebViewClient", "page is finished. Url: " + url);
        processorOnPageFinished.run();

    }

}
