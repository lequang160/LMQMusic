package com.example.lmqmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.database.AppDatabaseHelper;

import io.realm.Realm;

public class Application extends android.app.Application {
    public static android.content.Context Context;
    public static MediaService mService;


    @Override
    public void onCreate() {
        super.onCreate();
        Context = this;
        AppDatabaseHelper.newInstance(Context);
        AppDataManager.getInstance();
        startService();

    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public void startService() {
        Intent serviceIntent = new Intent(this, MediaService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        bindService(serviceIntent, serviceConnection,Context.BIND_AUTO_CREATE);
    }
}
