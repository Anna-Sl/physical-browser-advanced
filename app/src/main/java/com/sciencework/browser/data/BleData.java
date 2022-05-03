package com.sciencework.browser.data;

import java.util.Collection;
import java.util.List;

public class BleData {

    private boolean isEnabled;
    private String error;
    private List<BtApInfo> scanned;
    private long timeMillis;

    public BleData(boolean isEnabled, List<BtApInfo> scanned, long timeMillis) {
        this.isEnabled = isEnabled;
        this.scanned = scanned;
        this.timeMillis = timeMillis;
    }

    public BleData(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public BleData(String error) {
        this.error = error;
    }

    public List<BtApInfo> getScanned() {
        return scanned;
    }

    public long getTimeMillis() {
        return timeMillis;
    }
}
