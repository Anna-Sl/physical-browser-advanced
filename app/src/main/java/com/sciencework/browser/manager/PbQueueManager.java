package com.sciencework.browser.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PbQueueManager {

    private static final PbQueueManager INSTANCE = new PbQueueManager();

    private final BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(15);

    private PbQueueManager() {}

    public static PbQueueManager getInstance() {
        return(INSTANCE);
    }

    public BlockingQueue<Runnable> queue() {
        return taskQueue;
    }

}
