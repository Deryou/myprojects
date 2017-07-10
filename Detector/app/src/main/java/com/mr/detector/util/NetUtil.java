package com.mr.detector.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 网络工具类
 * 
 */
public final class NetUtil {

	/**
	 * ֻ只是判断WIFI
	 * 
	 * @param context
	 *            上下文
	 * @return 是否打开wifi
	 */
	public static boolean isWiFi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return true;
		} else {
			return false;
		}
	}

	// ==================================================================IP====================================================================//
	/**
	 * Gets the local ip address
	 * 
	 * @return local ip adress or null if not found
	 */
	public static InetAddress getLocalInetAddress(Context context) {
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (isWiFi(context)) {
			int ipAsInt = wifiMgr.getConnectionInfo().getIpAddress();
			if (ipAsInt == 0) {
				return null;
			} else {
				return intToInet(ipAsInt);
			}
		} else {
			try {
				Enumeration<NetworkInterface> netinterfaces = NetworkInterface
						.getNetworkInterfaces();
				while (netinterfaces.hasMoreElements()) {
					NetworkInterface netinterface = netinterfaces.nextElement();
					Enumeration<InetAddress> adresses = netinterface
							.getInetAddresses();
					while (adresses.hasMoreElements()) {
						InetAddress address = adresses.nextElement();
						if (!address.isLoopbackAddress()
								&& !address.isLinkLocalAddress()) {
							return address;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static InetAddress intToInet(int value) {
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = byteOfInt(value, i);
		}
		try {
			return InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			return null;
		}
	}

	private static byte byteOfInt(int value, int which) {
		int shift = which * 8;
		return (byte) (value >> shift);
	}

}
