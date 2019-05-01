package com.example.lmqmusic.ui.splash;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lmqmusic.Application;
import com.example.lmqmusic.R;
import com.example.lmqmusic.SongRealmObjectModel;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.example.lmqmusic.ui.main.Main2Activity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,

        EasyPermissions.RationaleCallbacks {

    private static final int REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE = 22;
    private static final String[] READ_AND_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    List<SongRealmObject> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        readAndWriteStore();

    }

    private void goMain()
    {
        Intent intent = new Intent(this, Main2Activity.class);
        this.startActivity(intent);
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE)
    public void readAndWriteStore() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            loadLocalSongs();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMain();
                    finish();
                }
            },2000);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.we_need_permissions_to_read_storage),
                    REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE,
                    READ_AND_WRITE);
        }
    }


    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, READ_AND_WRITE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);
        }
    }

    private void loadLocalSongs() {


        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Application.Context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        //Some audio may be explicitly marked as not being music
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST_ID,
        };

        ContentResolver cr = Application.Context.getContentResolver();

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";


        String appPath = Environment.getExternalStorageDirectory().toString() + "/" + getApplicationContext().getString(R.string.app_name);
        selection = selection + " AND (" + MediaStore.Audio.Media.DATA + " NOT LIKE '" + appPath + "/%')";


        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = cr.query(uri, projection, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String streamUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artistId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                long date = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                long duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                data.add(new SongRealmObject(Integer.valueOf(id),artist,title,displayName,streamUri,albumId,date,duration,fileSize));
                AppDataManager.getInstance().saveSong(data.get(data.size()-1));
            }
            cursor.close();
        }


    }
}
