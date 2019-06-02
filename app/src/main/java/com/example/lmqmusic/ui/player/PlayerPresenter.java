package com.example.lmqmusic.ui.player;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.base.fragment.FragmentPresenterMVP;

public class PlayerPresenter extends FragmentPresenterMVP<IPlayer> implements IPlayerPresenter {
    AppDataManager mDataManager = AppDataManager.getInstance();
    @Override
    public void saveSongToFavorite(SongModel song){
        mDataManager.setFavorite((int) song.getId());
    }

    @Override
    public void unFavorite(SongModel song) {
        mDataManager.unFavorite((int) song.getId());
    }
}
