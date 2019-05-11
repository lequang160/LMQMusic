package com.hamado.wifitransfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hamado.wifitransfer.builder.HtmlDescription;
import com.hamado.wifitransfer.callback.OnFileCallback;
import com.hamado.wifitransfer.callback.WifiTransferListener;
import com.hamado.wifitransfer.core.AndroidWebServer;
import com.hamado.wifitransfer.utils.FileHelper;
import com.hamado.wifitransfer.utils.WifiTransferUtils;

import java.io.IOException;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class WifiTransferControllerImpl implements WifiTransferController {

    private static volatile WifiTransferControllerImpl Instance = null;
    private static final int DEFAULT_PORT = 8080;

    private Context context;
    private AndroidWebServer webServer;
    private String formatIpAddress;
    private String storeFilePath;
    private HtmlDescription htmlDescription;
    private WifiTransferListener wifiTransferListener;
    private OnFileCallback fileCallback;

    /**
     * Constructor is private (Singleton Pattern)
     *
     * @param context Required Application Context to avoid leak memory
     */
    private WifiTransferControllerImpl(@NonNull Context context, @NonNull HtmlDescription htmlDescription,
                                       @NonNull String appName) {
        this.context = context;
        storeFilePath = FileHelper.getAppDir(appName);
        this.htmlDescription = htmlDescription;
        subscribeNetworkChange(context);

    }

    //Singleton pattern
    public static WifiTransferControllerImpl getInstance() {
        WifiTransferControllerImpl localInstance = Instance;
        if (localInstance == null) {
            synchronized (WifiTransferControllerImpl.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    throw new NullPointerException("Make sure call CloudsController.createInstance() in your Application before using");
                }
            }
        }
        return localInstance;
    }

    //Singleton pattern
    public static WifiTransferControllerImpl getInstance(@NonNull Context context, @NonNull HtmlDescription htmlDescription,
                                                         @NonNull String appName) {
        WifiTransferControllerImpl localInstance = Instance;
        if (localInstance == null) {
            synchronized (WifiTransferControllerImpl.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = new WifiTransferControllerImpl(context, htmlDescription, appName);
                    return Instance;
                }
            }
        }
        return localInstance;
    }

    /**
     * This method need calling in application once
     *
     * @param context Required Application Context to avoid leak memory
     */
    public static void createInstance(@NonNull Context context, @NonNull HtmlDescription htmlDescription,
                                      @NonNull String appName) {
        Instance = new WifiTransferControllerImpl(context, htmlDescription, appName);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void startWifiTransfer() {
        if (!WifiTransferUtils.isWifiConnected(context)) {
            if (wifiTransferListener != null)
                wifiTransferListener.onLostConnection();
            return;
        }

        if (webServer != null && webServer.isAlive()) return;
        AsyncTask.execute(() -> {
            try {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                formatIpAddress = String.format("%d.%d.%d.%d",
                        (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                        (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
                webServer = new AndroidWebServer(context, DEFAULT_PORT,
                        htmlDescription, storeFilePath);
                webServer.setOnFileCallback(fileCallback);
                webServer.start();
                if (wifiTransferListener != null) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> wifiTransferListener.wifiTransferStarted(DEFAULT_PORT, formatIpAddress));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    }

    private NetworkRequest getNetworkRequest() {
        return new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();
    }

    private void subscribeNetworkChange(Context context) {
        getConnectivityManager(context).registerNetworkCallback(getNetworkRequest(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if (wifiTransferListener != null) {
                    new Handler(Looper.getMainLooper()).post(() -> wifiTransferListener.onConnectionReconnected());
                }
            }

            @Override
            public void onLost(Network network) {
                if (wifiTransferListener != null) {
                    new Handler(Looper.getMainLooper()).post(() -> wifiTransferListener.onLostConnection());
                }
            }
        });
    }


    @Override
    public void stopWifiTransfer() {
        if (webServer == null) return;
        webServer.stop();
        webServer = null;
        if (wifiTransferListener != null) wifiTransferListener.wifiTransferStopped();
    }

    @Override
    public void setWifiTransferListener(WifiTransferListener wifiTransferListener) {
        this.wifiTransferListener = wifiTransferListener;
    }

    @Override
    public void setFileCallback(OnFileCallback fileCallback) {
        this.fileCallback = fileCallback;
    }

    @Override
    public boolean isWifiConnected() {
        return WifiTransferUtils.isWifiConnected(context);
    }
}
