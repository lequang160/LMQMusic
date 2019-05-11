package com.hamado.wifitransfer;

import com.hamado.wifitransfer.callback.OnFileCallback;
import com.hamado.wifitransfer.callback.WifiTransferListener;

public interface WifiTransferController {

    void startWifiTransfer();

    void stopWifiTransfer();

    void setWifiTransferListener(WifiTransferListener wifiTransferListener);

    void setFileCallback(OnFileCallback fileCallback);

    boolean isWifiConnected();
}
