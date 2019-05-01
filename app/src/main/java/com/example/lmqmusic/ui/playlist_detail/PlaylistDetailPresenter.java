package com.example.lmqmusic.ui.playlist_detail;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.example.lmqmusic.ui.base.fragment.FragmentPresenterMVP;

import java.util.List;

public class PlaylistDetailPresenter extends FragmentPresenterMVP<IPlaylistDetail> {
    AppDataManager mAppDataManager = AppDataManager.getInstance();

    List<SongModel> getSongsFromPlaylist(long playlistId){
        return mAppDataManager.getSongsFromPlaylist(playlistId);
    }

    void removeSong(long playlistId, SongModel song){
        mAppDataManager.removeSongOfPlaylist(playlistId, song);
    }
}
