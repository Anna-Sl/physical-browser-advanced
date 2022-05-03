package com.sciencework.browser;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sciencework.browser.manager.PbQueueManager;

import java.util.concurrent.TimeUnit;

public class PbLooper extends Thread {

    public Handler handler;

    @Override
    public void run() {
        Log.e("THREAD", "PbLooper.run: " + Thread.currentThread());
        Looper.prepare();
        handler = new Handler();
        handler.post(this::tryToPollTask);
        Looper.loop();
    }

    private void tryToPollTask() {
        Log.e("THREAD", "PbLopper.tryToPollTask: " + Thread.currentThread());
        Log.e("PbWebViewClient", "try to get a task from blocking queue");
        Runnable polledTask = null;
        try {
            polledTask = PbQueueManager.getInstance().queue().poll(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (polledTask != null) {
            polledTask.run();
            Log.e("PbWebViewClient", "succeeded to take a task from blocking queue");
        } else {
            Log.e("PbWebViewClient", "FAILED to take a task from blocking queue");
        }
    }

}
