package com.traffic.pd.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    /**
     * 判断网络连接是否可用
     *
     * @param context
     * @return
     */
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo info = connectivity.getActiveNetworkInfo();
//            if (info != null && info.isConnected()) {
//                // 当前网络是连接的
//                return info.getState() == NetworkInfo.State.CONNECTED;
//            }
//        }
//        return false;
//    }

    // 0,没有任何网络，1，wifi, 2,手机网络
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * WIFI是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager conMann = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo != null && wifiNetworkInfo.isConnected() && wifiNetworkInfo.isAvailable() && wifiNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

}
