package com.hamado.wifitransfer.callback;

import android.support.annotation.MainThread;

/**
 * Created by Thang Tran on 2/23/19.
 * Copyright © 2019 Hamado. All rights reserved.
 */
public interface WifiTransferListener {

    @MainThread
    void wifiTransferStarted(int port, String formatIpAddress);

    @MainThread
    void wifiTransferStopped();

    @MainThread
    void onLostConnection();

    @MainThread
    void onConnectionReconnected();
}
