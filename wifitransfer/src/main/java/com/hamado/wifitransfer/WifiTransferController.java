package com.hamado.wifitransfer;

import com.hamado.wifitransfer.callback.OnFileCallback;
import com.hamado.wifitransfer.callback.WifiTransferListener;

/**
 * Created by Thang Tran on 2/23/19.
 * Copyright © 2019 Hamado. All rights reserved.
 */
public interface WifiTransferController {

    void startWifiTransfer();

    void stopWifiTransfer();

    void setWifiTransferListener(WifiTransferListener wifiTransferListener);

    void setFileCallback(OnFileCallback fileCallback);

    boolean isWifiConnected();
}
