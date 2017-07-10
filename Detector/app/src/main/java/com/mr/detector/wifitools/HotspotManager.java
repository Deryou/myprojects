package com.mr.detector.wifitools;

import android.content.Context;
import android.media.audiofx.BassBoost;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.mr.detector.bean.WifiItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by MR on 2017/4/27.
 * 热点管理工具类
 */

public class HotspotManager {

    //过滤免密码连接的WiFi
    public static final String NO_PASSWORD = "";
    public static final String NO_PASSWORD_ESS = "[ESS]";
    public static final String NO_PASSWORD_WPS = "[WPS][ESS]";

    private Context mContext;
    private WifiManager mWifiManager;

    public HotspotManager(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 设置有密码的热点信息
     *
     * @param SSID 热点名称
     * @param pwd  热点密码
     * @return 返回配置信息
     */
    private static WifiConfiguration getHotspotConfig(String SSID, String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return null;
        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = SSID;
        config.preSharedKey = pwd;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        return config;
    }

    /**
     * 判断热点是否开启
     *
     * @return true代表开启
     */
    public boolean isHotspotOn() {
        try {
            Method method = mWifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开启热点
     *
     * @param SSID     热点名称
     * @param password 热点密码
     * @return 开启成功返回true
     */
    public boolean openHotspot(String SSID, String password) {
        if (TextUtils.isEmpty(SSID)) {
            SSID = Build.MODEL;
        }

        //检测wifi是否已打开，打开的话关闭
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }

        WifiConfiguration wifiConfiguration = getHotspotConfig(SSID, password);
        try {
            if (isHotspotOn()) {
                closeHotspot();
            }
            //使用反射开启wifi热点
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, wifiConfiguration, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取开启热点后，自身热点的IP地址
     *
     * @return IP地址，字符串类型
     */
    public String getHotspotLocalIpAddress() {
        DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();

        Log.e(TAG, "getHotspotLocalIpAddress: " + dhcpInfo.serverAddress + "格式转换后：" + Formatter
                .formatIpAddress(dhcpInfo.serverAddress));
        if (dhcpInfo != null) {
            int address = dhcpInfo.serverAddress;
            return ((address & 0xFF)
                    + "." + ((address >> 8) & 0xFF)
                    + "." + ((address >> 16) & 0xFF)
                    + "." + ((address >> 24) & 0xFF));
        }
        return null;
    }

    /**
     * 获取已连接SSID
     *
     * @return
     */
    public String getConnectedSSID() {
        WifiInfo info = mWifiManager.getConnectionInfo();
        return info != null ? info.getSSID().replaceAll("\"", "") : "";
    }

    /**
     * 关闭热点
     */
    public void closeHotspot() {
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前wifi是否开启
     *
     * @return
     */
    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 开启wifi
     */
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 获取周围可用wifi扫描结果
     *
     * @return
     */
    public boolean startScan() {
        if (isWifiEnabled()) {
            return mWifiManager.startScan();
        }
        return false;
    }

    /**
     * 关闭WIFI
     */
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获得周围可用wifi扫描结果
     *
     * @return
     */
    public List<ScanResult> getScanResults() {
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        if (scanResults != null && scanResults.size() > 0) {
            return filterScanResult(scanResults);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 对扫描后的wifi进行排序
     *
     * @param scanResults 扫描后的结果
     * @return 排序后结果
     */
    public List<ScanResult> filterScanResult(List<ScanResult> scanResults) {
        List<ScanResult> results = new ArrayList<>();
        if (scanResults == null) {
            return results;
        }
        for (ScanResult scanResult : scanResults) {
            if (!TextUtils.isEmpty(scanResult.BSSID) && scanResult.level > -100) {
                results.add(scanResult);
            }
        }

        for (int i = 1; i < results.size(); i++) {
            for (int j = 0; j < results.size() - i; j++) {
                //将搜索到的wifi根据信号强度从强到弱进行排序
                if (results.get(j).level < results.get(j + 1).level) {
                    ScanResult temp = results.get(j);
                    results.set(j, results.get(j + 1));
                    results.set(j + 1, temp);
                }
            }
        }
        return results;
    }

    private boolean isAdHoc(ScanResult scanResult) {
        return scanResult.capabilities.indexOf("IBSS") != -1;
    }

    public boolean connectWifi(String ssid, String pwd, List<WifiItem> scanResults) {
        if (scanResults == null || scanResults.size() == 0) {
            return false;
        }
        //匹配SSID相同的Wifi
        ScanResult result = null;
        for (WifiItem tempResult : scanResults) {
            if (tempResult.getScanResult().SSID.equals(ssid)) {
                result = tempResult.getScanResult();
                break;
            }
        }
        if (result == null) {
            return false;
        }

        if (isAdHoc(result)) {
            return false;
        }

        String security = Wifi.ConfigSec.getScanResultSecurity(result);
        WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, result, security);
        if (config == null) {
            //连接新wifi
            boolean connResult;
            int numOpenNetworksKept = Settings.Secure.getInt(mContext.getContentResolver(),
                    Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, 10);
            String scanResultSecurity = Wifi.ConfigSec.getScanResultSecurity(result);
            boolean isOpenNetwork = Wifi.ConfigSec.isOpenNetwork(scanResultSecurity);
            if (isOpenNetwork) {
                connResult = Wifi.connectToNewNetwork(mContext, mWifiManager, result, null,
                        numOpenNetworksKept);
            } else {
                connResult = Wifi.connectToNewNetwork(mContext, mWifiManager, result, pwd,
                        numOpenNetworksKept);
            }
            return connResult;
        } else {
            final boolean isCurrentNetwork_ConfigurationStatus = config.status == WifiConfiguration.Status.CURRENT;
            final WifiInfo info = mWifiManager.getConnectionInfo();
            final boolean isCurrentNetwork_WifiInfo = info != null
                    && TextUtils.equals(info.getSSID(), result.SSID)
                    && TextUtils.equals(info.getBSSID(), result.BSSID);
            if(!isCurrentNetwork_ConfigurationStatus && !isCurrentNetwork_WifiInfo) {
                //连接已保存的WiFi
                String scanResultSecurity = Wifi.ConfigSec.getScanResultSecurity(result);
                final WifiConfiguration wcg = Wifi.getWifiConfiguration(mWifiManager, result, scanResultSecurity);
                boolean connResult = false;
                if(wcg != null) {
                    connResult = Wifi.connectToConfiguredNetwork(mContext, mWifiManager, wcg, false);
                }
                return connResult;
            } else {
                //点击的是当前已连接的WiFi
                return true;
            }
        }
    }
}
