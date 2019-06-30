package com.example.lmqmusic.ui.list_favorite;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.base.fragment.FragmentPresenterMVP;

import java.util.List;

public class FavoritePresenter extends FragmentPresenterMVP<IFavoriteView> implements IFavoritePresenter {
    AppDataManager mDataManager = AppDataManager.getInstance();
    @Override
    public List<SongModel> getDataFavorite() {
        return mDataManager.getFavoriteList();
    }

    @Override
    public void unFavorite(SongModel songModel) {
        mDataManager.unFavorite((int) songModel.getId());
    }
}
