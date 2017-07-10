package com.mr.bst.bean;

import android.net.wifi.ScanResult;

/**
 * Created by MR on 2017/5/17.
 */

public class WifiItem {
    private boolean isConn;
    private ScanResult mScanResult;

    public boolean isConn() {
        return isConn;
    }

    public WifiItem setConn(boolean conn) {
        isConn = conn;
        return this;
    }

    public ScanResult getScanResult() {
        return mScanResult;
    }

    public WifiItem setScanResult(ScanResult scanResult) {
        mScanResult = scanResult;
        return this;
    }
}
