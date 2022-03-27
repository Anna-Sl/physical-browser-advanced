package com.sciencework.browser;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PbLooper extends Thread {

    Integer number;
    public Handler mHandler;
    final private BlockingQueue<Runnable> queue;

    public PbLooper(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new MyHandler();
        mHandler.post(this::myRun);
        Looper.loop();
    }

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // process incoming messages here
        }
    }

    private void myRun() {
        Log.e("PbWebViewClient", "try to get a task from blocking queue");
        Runnable polledTask = null;
        try {
            polledTask = queue.poll(30, TimeUnit.SECONDS);
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
