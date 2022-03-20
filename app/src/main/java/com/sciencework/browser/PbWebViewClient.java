package com.sciencework.browser;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.util.Consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PbWebViewClient extends WebViewClient {

    private final ProgressDialog progDialog;
    private final Consumer<WebView> processorOnPageStarted;
    private final AtomicInteger onPageFinishedCount;
    private final BlockingQueue<Runnable> queue;

    public PbWebViewClient(ProgressDialog progDialog, Consumer<WebView> processorOnPageStarted, AtomicInteger onPageFinishedCount,
                           BlockingQueue<Runnable> queue) {
        this.progDialog = progDialog;
        this.processorOnPageStarted = processorOnPageStarted;
        this.onPageFinishedCount = onPageFinishedCount;
        this.queue = queue;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e("PbWebViewClient", "shouldOverrideUrlLoading. Url: " + url);
        processorOnPageStarted.accept(view);
        progDialog.show();
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.e("PbWebViewClient", "page is started. Url: " + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, final String url) {
        Log.e("PbWebViewClient", "page is finished. Url: " + url);
        progDialog.dismiss();
        onPageFinishedCount.incrementAndGet();
        Log.e("PbWebViewClient", "onPageFinishedCount is incremented to " + onPageFinishedCount.get());
        super.onPageFinished(view, url);
        if (!queue.isEmpty()) {
            try {
                Log.e("PbWebViewClient", "queue is NOT empty");
                queue.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("PbWebViewClient", "queue is empty");
        }
    }

}
