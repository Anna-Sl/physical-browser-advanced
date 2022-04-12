package com.sciencework.browser;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PbNetworkManager {

    private static final PbNetworkManager INSTANCE = new PbNetworkManager();

    private final BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(15);

    private PbNetworkManager() {}

    public static PbNetworkManager getInstance() {
        return(INSTANCE);
    }

    public BlockingQueue<Runnable> queue() {
        return taskQueue;
    }

}
