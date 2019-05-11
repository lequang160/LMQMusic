package com.hamado.wifitransfer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class WifiTransferUtils {
    /**
     * @param context to use to check for network connectivity.
     * @return true if connected, false otherwise.
     */
    public static boolean isWifiConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
