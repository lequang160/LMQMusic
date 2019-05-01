package com.example.lmqmusic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.main.Main2Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediaService extends Service {
    private final IBinder mBinder = new MediaBinder();
    private Notification status;
    private static final String CHANNEL_ID = "TEST_CHANNEL";
    private List<SongModel> songList = new ArrayList<>();
    private MediaPlayer mMediaPlayer;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private int currentIndex = 0;
    private MediaController.PlayerListener mListener;
    private boolean isPlaying = false;
    private long currentPosition;
    private Handler handler = new Handler();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
        createNotificationChannel();
    }

    private void initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentPosition = 0;
                    onCompleteMedia(isRepeat, isShuffle);
                }
            });
        } else {
            mMediaPlayer.release();
        }
    }

    private void onCompleteMedia(boolean isRepeat, boolean isShuffle) {
        if(songList.size() > 0) {
            //if isRepeat = true --> MediaPlayer.start()
            //else Next()
            if (isRepeat) {
                mMediaPlayer.start();
            } else {
                if (isShuffle) {
                    //random index
                    int position = new Random().nextInt(songList.size());
                    currentIndex = position;
                    PlayAtPosition(position);
                } else {
                    Next();
                }
            }
        }
        //if isShuffle = true --> setDataSource() (Random)
    }

    public void Next() {
        currentIndex = (currentIndex + 1) % songList.size();
        PlayAtPosition(currentIndex);
    }

    public void Previous() {
        currentIndex = (currentIndex + songList.size() - 1) % songList.size();
        PlayAtPosition(currentIndex);
    }

    public void Prepare(int position) {
        SongModel song = songList.get(position);
        setDataSource(song);
        isPlaying = false;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    public void Pause() {
        mMediaPlayer.pause();
        isPlaying = false;
        realTimePositionOfSong(isPlaying);
        if(mListener != null)
            mListener.onStateChanged(false);
    }

    public void setShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
    }

    public void PlayAtPosition(int position) {
        currentIndex = position;
        SongModel song = songList.get(position);
        setDataSource(song);
        Play();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setDataSource(SongModel song) {

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.getStreamUri());
            mMediaPlayer.prepare();
            mListener.onDataSourceChange(song);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Play() {
        mMediaPlayer.start();
        while (!isPlaying) {
            isPlaying = mMediaPlayer.isPlaying();
        }
        realTimePositionOfSong(isPlaying);
        mListener.onStateChanged(isPlaying);
    }

    public void setDataSource(List<SongModel> dataSource) {
        this.songList.clear();
        this.songList.addAll(dataSource);
    }

    public void setListener(MediaController.PlayerListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) return START_STICKY;
        switch (intent.getAction()) {
            case Constants.ACTION.PREV_ACTION:
                Previous();
                break;
            case Constants.ACTION.PLAY_ACTION:
                Play();
                break;
            case Constants.ACTION.NEXT_ACTION:
                Next();
                break;
            case Constants.ACTION.STOPFOREGROUND_ACTION:
                if(mMediaPlayer != null)
                    Pause();
                stopForeground(true);
                stopSelf();
                break;
            case Constants.ACTION.PAUSE_ACTION:
                Pause();
                break;
        }
        if(intent.getAction().equals(Constants.ACTION.MAIN_ACTION) || intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION))
        {
            if(mMediaPlayer != null && mListener != null) {
                SongModel song;
                song = songList.get(currentIndex);
                mListener.onStateChanged(isPlaying);
                mListener.onDataSourceChange(song);
                mListener.onObserveCurrentPositionMediaPlayer(currentPosition);
                createNotification(song.getDisplayName(), song.getArtist(), "", isPlaying);
            }
            return START_STICKY;
        }
        if(!intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            SongModel song;
            song = songList.get(currentIndex);
            createNotification(song.getDisplayName(), song.getArtist(), "", isPlaying);
        }
        return START_STICKY;
    }

    MediaPlayer getMediaPlayer() {
        return this.mMediaPlayer;
    }

    public int getPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    private void createNotification(String songName, String artist, String playlist, boolean isPlaying) {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);

        Intent notificationIntent = new Intent(this, Main2Activity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MediaService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MediaService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent pauseIntent = new Intent(this, MediaService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);

        Intent nextIntent = new Intent(this, MediaService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, MediaService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        if (!isPlaying) {
            views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        } else {
            views.setOnClickPendingIntent(R.id.status_bar_play, ppauseIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_play, ppauseIntent);
        }


        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        bigViews.setImageViewResource(R.id.status_bar_play,
                isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);

        views.setTextViewText(R.id.status_bar_track_name, songName);
        bigViews.setTextViewText(R.id.status_bar_track_name, songName);

        views.setTextViewText(R.id.status_bar_artist_name, artist);
        bigViews.setTextViewText(R.id.status_bar_artist_name, artist);

        bigViews.setTextViewText(R.id.status_bar_album_name, playlist == null ? "" : playlist);

        if (status == null)
            status = new NotificationCompat.Builder(this, CHANNEL_ID).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void realTimePositionOfSong(boolean isPlaying)
    {
        currentPosition = 0;
        if(isPlaying)
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentPosition = mMediaPlayer.getCurrentPosition();
                    handler.postDelayed(this,1000);
                }
            },1000);
        }
        else
        {
            handler.removeCallbacksAndMessages("");
        }
    }

    private void updateNotificationBigView(String songName, String artist, String playlist, boolean isPlaying) {
        createNotification(songName, artist, playlist, isPlaying);
    }

    public class MediaBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

}
