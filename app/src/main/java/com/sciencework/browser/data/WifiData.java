package com.sciencework.browser.data;

import java.util.List;

public class WifiData {
    private boolean isEnabled;
    private String error;
    private List<WfApInfo> scanned;
    private long timeMillis;

    public WifiData(boolean isEnabled, List<WfApInfo> scanned, long timeMillis) {
        this.isEnabled = isEnabled;
        this.scanned = scanned;
        this.timeMillis = timeMillis;
    }

    public WifiData(String error) {
        this.error = error;
    }

    public WifiData(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public List<WfApInfo> getScanned() {
        return scanned;
    }
}
