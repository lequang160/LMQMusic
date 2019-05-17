package com.example.lmqmusic.ui.wifi_transfer;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lmqmusic.Application;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.example.lmqmusic.ui.base.fragment.PermissionFragmentMVP;
import com.example.lmqmusic.utils.LocalSongsHelper;
import com.hamado.wifitransfer.WifiTransferController;
import com.hamado.wifitransfer.WifiTransferControllerImpl;
import com.hamado.wifitransfer.builder.HtmlDescription;
import com.hamado.wifitransfer.callback.OnFileCallback;
import com.hamado.wifitransfer.callback.WifiTransferListener;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class WifiTransferFragment extends PermissionFragmentMVP<WifiTransferPresenter, IWifiTransfer> implements IWifiTransfer {

    private static final int PORT = 8080;

    @BindView(R.id.text_view_title)
    TextView textViewTitle;

    @BindView(R.id.button_back)
    AppCompatImageButton buttonBack;

    @BindView(R.id.text_view_link_address)
    TextView textViewLinkAddress;

    WifiTransferController wifiTransferController;

    public WifiTransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_wifi_transfer, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        requiresPermissionReadWriteExternalStorage();
        return rootView;
    }

    private void startWifiTransfer() {
        wifiTransferController = WifiTransferControllerImpl.getInstance(Application.Context, new HtmlDescription("device", "LMQ Music", "LMQ Music", "LMQ Music", "footer"), "LMQ Music");
        wifiTransferController.setWifiTransferListener(new WifiTransferListener() {
            @Override
            public void wifiTransferStarted(int port, String formatIpAddress) {
                Toast.makeText(mActivity, "wifiTransferStarted", Toast.LENGTH_SHORT).show();
                textViewLinkAddress.setText(getString(R.string.link_connect_wifi_transfer, formatIpAddress, PORT + ""));
            }

            @Override
            public void wifiTransferStopped() {
                Toast.makeText(mActivity, "wifiTransferStopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLostConnection() {
                Toast.makeText(mActivity, "onLostConnection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionReconnected() {
                Toast.makeText(mActivity, "onConnectionReconnected", Toast.LENGTH_SHORT).show();
            }
        });
        wifiTransferController.setFileCallback(new OnFileCallback() {
            @Override
            public void onUploadFileSuccess(File file) {

                LocalSongsHelper.loadLocalSong(file.getPath(), Application.Context, new LocalSongsHelper.ReadFileListener() {
                    @Override
                    public void onDoneScanFile(Object o) {
                        if(o instanceof SongRealmObject)
                        {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppDataManager.getInstance().saveSong((SongRealmObject) o);
                                    new CountDownTimer(1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            AppDataManager.getInstance().getDataLocalSong();
                                        }
                                    }.start();
                                }
                            });


                        }

                    }
                });

            }

            @Override
            public void onFileDeleted(File file) {
                Toast.makeText(getApplicationContext(), "File Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        wifiTransferController.startWifiTransfer();
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new WifiTransferPresenter();
    }

    private void initViews() {
        textViewTitle.setText(R.string.wifi_transfer);
        buttonBack.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_back)
    void back() {
        mActivity.onBackPressed();
    }

    @OnClick(R.id.button_finish)
    void disconnect() {
        wifiTransferController.stopWifiTransfer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void permissionGranted() {
        startWifiTransfer();
    }
}
