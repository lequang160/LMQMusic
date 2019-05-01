package com.example.lmqmusic.ui.dashboard;

import android.content.Intent;

import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.ui.base.activity.BasePresenter;
import com.example.lmqmusic.ui.song.SongFragment;

import java.util.List;

public class DashBoardPresenter<V extends IView> extends BasePresenter<V> implements IPresenter<V> {
    @Override
    public List<Item> getAllItemDashBoard() {
        return getDataManager().getAllItem();
    }

    @Override
    public void goSongs() {
        Intent intent = new Intent(getMvpView().getActivity(), SongFragment.class);
        getMvpView().pushIntent(intent);
    }

    @Override
    public void goFavorite() {

    }

    @Override
    public void goPlaylist() {

    }

    @Override
    public void goWifiTransfer() {

    }

    @Override
    public void goArtist() {

    }
}
