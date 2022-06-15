package com.sciencework.browser.data;

import java.util.List;

public class WifiData {
    private boolean isEnabled;
    private String error;
    private List<WfApInfo> points;
    private long timeMillis;

    public WifiData(boolean isEnabled, List<WfApInfo> points, long timeMillis) {
        this.isEnabled = isEnabled;
        this.points = points;
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

    public List<WfApInfo> getPoints() {
        return points;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
