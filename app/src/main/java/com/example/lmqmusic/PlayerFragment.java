//package com.example.lmqmusic;
//
//import android.Manifest;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.AppCompatImageButton;
//import android.view.View;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.example.lmqmusic.data.AppDataManager;
//import com.example.lmqmusic.data.model.realm.SongRealmObject;
//import com.example.lmqmusic.ui.base.activity.BaseActivity;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.AppSettingsDialog;
//import pub.devrel.easypermissions.EasyPermissions;
//
//public class PlayerFragment extends BaseActivity implements EasyPermissions.PermissionCallbacks,
//
//        EasyPermissions.RationaleCallbacks, MediaController.PlayerListener {
//
//    private static final int REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE = 12;
//    private static final String[] READ_AND_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//
//    List<SongRealmObject> data = new ArrayList<>();
//    MediaController mediaController;
//
//
//
//    @BindView(R.id.seekBar)
//    SeekBar mSeekBar;
//
//    @BindView(R.id.button_pause)
//    AppCompatImageButton mButtonPause;
//
//    @BindView(R.id.button_play)
//    AppCompatImageButton mButtonPlay;
//
//    @BindView(R.id.song_name)
//    TextView mTextSongName;
//
//    @BindView(R.id.song_artist)
//    TextView mTextSongArtist;
//
//    @BindView(R.id.button_favorite)
//    AppCompatImageButton mButtonFavorite;
//
//    @BindView(R.id.button_more)
//    AppCompatImageButton mButtonMore;
//
//    @BindView(R.id.button_down)
//    AppCompatImageButton mButtonDown;
//
//    @BindView(R.id.button_playlist)
//    AppCompatImageButton mButtonPlaylist;
//
//    @BindView(R.id.text_duration)
//    TextView mTextDuration;
//
//    @BindView(R.id.text_display_time)
//    TextView mTextDisplayTime;
//
//
//    @Override
//    protected int getContentView() {
//        return R.layout.player;
//    }
//
//
//    @Override
//    protected void initControls() {
//
//    }
//
//    @Override
//    protected void initViews(@Nullable Bundle savedInstanceState) {
//        readAndWriteStore();
//        loadLocalSongs();
//
//        mediaController = MediaController.newInstance();
//        mediaController.setListener(this);
//        mediaController.setSongSource(data);
//        mediaController.PlayAtPosition(0);
//
//        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaController.seekTo(seekBar.getProgress());
//            }
//        });
//        AppDataManager.getInstance().getAllItem();
//    }
//
//
//    @AfterPermissionGranted(REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE)
//    public void readAndWriteStore() {
//        if (hasLocationAndContactsPermissions()) {
//            // Have permissions, do the thing!
//        } else {
//            // Ask for both permissions
//            EasyPermissions.requestPermissions(
//                    this,
//                    getString(R.string.we_need_permissions_to_read_storage),
//                    REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE,
//                    READ_AND_WRITE);
//        }
//    }
//
//
//    private boolean hasLocationAndContactsPermissions() {
//        return EasyPermissions.hasPermissions(this, READ_AND_WRITE);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        // EasyPermissions handles the request result.
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @Override
//    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
//    }
//
//    @Override
//    public void onRationaleAccepted(int requestCode) {
//
//    }
//
//    @Override
//    public void onRationaleDenied(int requestCode) {
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
//            String yes = getString(R.string.yes);
//            String no = getString(R.string.no);
//        }
//    }
//
//    private void loadLocalSongs() {
//
//
//        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Application.Context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//
//        //Some audio may be explicitly marked as not being music
//        String[] projection = {
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.ALBUM_ID,
//                MediaStore.Audio.Media.DATE_ADDED,
//                MediaStore.Audio.Media.SIZE,
//
//
//        };
//
//        ContentResolver cr = Application.Context.getContentResolver();
//
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//
//
//        String appPath = Environment.getExternalStorageDirectory().toString() + "/" + getApplicationContext().getString(R.string.app_name);
//        selection = selection + " AND (" + MediaStore.Audio.Media.DATA + " NOT LIKE '" + appPath + "/%')";
//
//
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//        Cursor cursor = cr.query(uri, projection, selection, null, sortOrder);
//
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
//                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
//                String streamUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                long date = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
//                long duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                long fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//
//                data.add(new SongRealmObject(Integer.valueOf(id), artist, title, displayName, streamUri, albumId, date, duration, fileSize));
//            }
//            cursor.close();
//        }
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @OnClick(R.id.button_play)
//    void playMusic() {
//
//        mediaController.Play();
//    }
//
//    @OnClick(R.id.button_next)
//    void nextSong() {
//        mediaController.Next();
//    }
//
//    @OnClick(R.id.button_previous)
//    void previousSong() {
//        mediaController.Previous();
//    }
//
//    @OnClick(R.id.button_pause)
//    void pauseSong() {
//        mediaController.Pause();
//    }
//
//    @OnClick(R.id.button_repeat)
//    void repeatSong() {
//        mediaController.setRepeat(true);
//    }
//
//    @OnClick(R.id.button_shuffle)
//    void shuffleSongs() {
//        mediaController.setShuffle(true);
//    }
//
//    @OnClick(R.id.button_playlist)
//    void showPlaylist() {
//        PlaylistDialogFragment playlistDialogFragment = PlaylistDialogFragment.newInstance();
//        playlistDialogFragment.show(getSupportFragmentManager(), "playlist");
//    }
//
//
//    @Override
//    public void onStateChanged(boolean isPlaying) {
//        if (isPlaying) {
//
//            mButtonPlay.setVisibility(View.INVISIBLE);
//            mButtonPause.setVisibility(View.VISIBLE);
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mSeekBar.setProgress(mediaController.getPosition());
//                    handler.postDelayed(this, 1000);
//                    mTextDisplayTime.setText(new SimpleDateFormat("mm:ss", Locale.US).format(mediaController.getPosition()));
//                }
//            }, 1000);
//        } else {
//            mButtonPlay.setVisibility(View.VISIBLE);
//            mButtonPause.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    @Override
//    public void onDataSourceChange(SongRealmObject song) {
//        mTextSongName.setText(song.getTitle());
//        mTextSongArtist.setText(song.getArtist());
//        mSeekBar.setMax((int) song.getDuration());
//        mTextDuration.setText(new SimpleDateFormat("mm:ss", Locale.US).format(song.getDuration()));
//
//    }
//}
