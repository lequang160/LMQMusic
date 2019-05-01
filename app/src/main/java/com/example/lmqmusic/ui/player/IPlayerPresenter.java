package com.example.lmqmusic.ui.player;

import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.base.activity.MvpPresenter;

public interface IPlayerPresenter{
    void saveSongToFavorite(SongModel song);
}
