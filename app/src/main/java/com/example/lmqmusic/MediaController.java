package com.example.lmqmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.lmqmusic.data.model.SongModel;

import java.util.List;

public class MediaController {
    private static MediaController INSTANCE;
    private MediaPlayer mMediaPlayer;
    private PlayerListener mListener;
    MediaService mMediaService;

    public static MediaController newInstance() {
        if (INSTANCE == null) {
            return INSTANCE = new MediaController();
        }
        return INSTANCE;
    }

    private MediaController() {
        mMediaService = Application.mService;
    }

    public int getCurrentIndex() {
        return mMediaService.getCurrentIndex();
    }

    public void setListener(PlayerListener mListener) {

        mMediaService.setListener(mListener);

    }


    public void setDataSource(List<SongModel> mDataSource) {

        mMediaService.setDataSource(mDataSource);

    }

    private void setDataSource(SongModel song) {

        mMediaService.setDataSource(song);

    }

    public void Play() {
        mMediaService.Play();
        //mListener.onStateChanged(isPlaying);
    }

    public void Pause() {
        mMediaService.Pause();
    }

    public void setShuffle(boolean isShuffle) {
        mMediaService.setShuffle(isShuffle);
    }


    public void Next() {
        mMediaService.Next();
    }

    public void Previous() {
        mMediaService.Previous();
    }

    public void setRepeat(boolean isRepeat) {
        mMediaService.setRepeat(isRepeat);
    }

    public void seekTo(int position) {
        mMediaService.seekTo(position);
    }


    public int getPosition() {
        return mMediaService.getPosition();
    }

    public void PlayAtPosition(int position) {
        mMediaService.PlayAtPosition(position);
    }

    public void Prepare(int position) {
        mMediaService.Prepare(position);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            mMediaService = binder.getService();
            mMediaPlayer = mMediaService.getMediaPlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMediaService = null;
        }
    };

    public void createNotification() {
        Intent serviceIntent = new Intent(Application.Context, MediaService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        Application.Context.startService(serviceIntent);
    }

    public interface PlayerListener {
        void onStateChanged(boolean isPlaying);

        void onDataSourceChange(SongModel duration);

        void onObserveCurrentPositionMediaPlayer(long currentPosition);
    }
}
