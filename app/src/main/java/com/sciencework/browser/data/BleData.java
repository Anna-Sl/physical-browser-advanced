package com.sciencework.browser.data;

import java.util.List;

public class BleData {

    private boolean isEnabled;
    private String error;
    private List<BtApInfo> points;
    private long timeMillis;

    public BleData(boolean isEnabled, List<BtApInfo> points, long timeMillis) {
        this.isEnabled = isEnabled;
        this.points = points;
        this.timeMillis = timeMillis;
    }

    public BleData(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public BleData(String error) {
        this.error = error;
    }

    public List<BtApInfo> getPoints() {
        return points;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
