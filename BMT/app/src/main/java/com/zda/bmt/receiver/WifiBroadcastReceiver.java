package com.zda.bmt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.zda.bmt.wifitools.HotspotManager;

import java.util.List;

/**
 * Created by MR on 2017/5/11.
 */

public abstract class WifiBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                //监听wifi开启/关闭事件
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                    //wifi已开启
                    onWifiEnabled();
                } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    //wifi已关闭
                    onWifiDisabled();
                }
            } else if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                HotspotManager manager = new HotspotManager(context);
                List<ScanResult> scanResults = manager.getScanResults();
                if (manager.isWifiEnabled() && scanResults != null && scanResults.size() > 0) {
                    //成功扫描
                    onScanResultsAvailable(scanResults);
                    String connectedSSID = manager.getConnectedSSID();
                    onWifiConnected(connectedSSID);
                }
            } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                //网络状况改变的广播
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        //wifi已连接
                        HotspotManager manager = new HotspotManager(context);
                        String connectedSSID = manager.getConnectedSSID();
                        onWifiConnected(connectedSSID);
                    } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        //WIFI已断开来连接
                        onWifiDisconnected();
                    }
                }
            }
        }
    }

    public abstract void onWifiEnabled();

    public abstract void onWifiDisabled();

    public abstract void onScanResultsAvailable(List<ScanResult> scanResults);

    public abstract void onWifiConnected(String connectedSSID);

    public abstract void onWifiDisconnected();
}
