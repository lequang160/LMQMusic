package com.example.lmqmusic.ui.dashboard;

import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.ui.base.activity.MvpPresenter;

import java.util.List;

public interface IPresenter<V extends IView> extends MvpPresenter<V> {
    List<Item> getAllItemDashBoard();
    void goSongs();
    void goFavorite();
    void goPlaylist();
    void goWifiTransfer();
    void goArtist();
}
