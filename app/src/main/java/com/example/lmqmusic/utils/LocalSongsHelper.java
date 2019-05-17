package com.example.lmqmusic.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.lmqmusic.Application;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.hamado.wifitransfer.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;

public final class LocalSongsHelper {


    public static void loadLocalSongs(ReadFileListener readFileListener) {

        List<SongRealmObject> data = new ArrayList<>();
        String appPath = Environment.getExternalStorageDirectory().toString() + "/" + "LMQ Music";
        boolean deleted = FileUtils.deleteFile(appPath + "/.nomedia");
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
                String thumb = getPathAlbumArt(albumId, Application.Context);
                long date = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                long duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                data.add(new SongRealmObject(Integer.valueOf(id), artist, title, displayName, streamUri, albumId, date, duration, fileSize, thumb));
                Log.d("data", new SongRealmObject(Integer.valueOf(id), artist, title, displayName, streamUri, albumId, date, duration, fileSize, thumb).toString());
                AppDataManager.getInstance().saveSong(new SongRealmObject(Integer.valueOf(id), artist, title, displayName, streamUri, albumId, date, duration, fileSize, thumb));
            }
            cursor.close();
            readFileListener.onDoneScanFile(null);
        }
    }

    private static String getPathAlbumArt(String albumId, Context context) {
        if (albumId == null) return "";
        Uri externalContentUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART};
        String selection = MediaStore.Audio.Albums._ID + "=?";
        String[] selectionArgs = {albumId};
        Cursor cursor = context.getContentResolver().query(externalContentUri,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor == null) return "";

        if (cursor.moveToFirst()) {
            String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            return albumArt == null ? "" : albumArt;
        }
        cursor.close();
        return "";
    }

    public static void loadLocalSong(final String localPath, final Context context, ReadFileListener readFileListener) {

        boolean deleted = FileUtils.deleteFile(FileHelper.getAppDir("LMQ Music") + "/.nomedia");

        MediaScannerConnection.scanFile(context, new String[]{localPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(final String path, Uri uri) {


                SongRealmObject realmObject = null;

                //Some audio may be explicitly marked as not being music
                String[] projection = {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.DATE_ADDED,
                        MediaStore.Audio.Media.SIZE
                };

                ContentResolver cr = context.getContentResolver();
                Uri uri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                String selection = MediaStore.Audio.Media.DATA + "= '" + path + "' ";

                String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
                Cursor cursor = cr.query(uri1, projection, selection, null, sortOrder);

                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String streamUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        long date = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                        long duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                        realmObject = new SongRealmObject(Long.valueOf(id), artist, title, displayName, streamUri, albumId, date, duration, fileSize, false, getPathAlbumArt(albumId, Application.Context));
                    }
                    cursor.close();
                    readFileListener.onDoneScanFile(realmObject);
                }
            }



    });

}

public interface ReadFileListener {
    void onDoneScanFile(Object o);
}

}

