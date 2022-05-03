package com.sciencework.browser.data;

public class ConnectedWfInfo extends WfApInfo {

    public String status;
    public int linkSpeed;

    public ConnectedWfInfo(String status, int linkSpeed, WfApInfo wfApInfo) {
        super(wfApInfo);
        this.status = status;
        this.linkSpeed = linkSpeed;
    }
}
