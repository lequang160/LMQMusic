package com.hamado.wifitransfer.callback;

import android.support.annotation.MainThread;


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
