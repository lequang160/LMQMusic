package com.example.lmqmusic.ui.list_playlist;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.ui.base.fragment.FragmentPresenterMVP;

import java.util.List;

public class PlaylistPresenter extends FragmentPresenterMVP<IPlaylist> {
    AppDataManager mAppDataManager = AppDataManager.getInstance();
    void removePlaylist(PlayListRealmObject playListRealmObject){
        mAppDataManager.removePlaylist(playListRealmObject);
    }
    List<PlayListRealmObject> getAllPlaylist(){
        return mAppDataManager.getAllPlaylist();
    }

    void addPlaylistToDatabase(PlayListRealmObject playlist)
    {
        mAppDataManager.addPlaylistToDatabase(playlist);
    }

}
