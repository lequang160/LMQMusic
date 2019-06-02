package com.example.lmqmusic.ui.list_favorite;

import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.base.fragment.FragmentPresenterMVP;
import com.example.lmqmusic.ui.player.IPlayer;
import com.example.lmqmusic.ui.player.IPlayerPresenter;

import java.util.List;

public interface IFavoritePresenter  {
    List<SongModel> getDataFavorite();
    void updateSong(SongModel songModel);
}
