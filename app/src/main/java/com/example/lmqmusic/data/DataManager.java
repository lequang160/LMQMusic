package com.example.lmqmusic.data;

import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.data.model.realm.SongRealmObject;

import java.util.List;

public interface DataManager {

    List<Item> getAllItem();

    void addPlaylistToDatabase(PlayListRealmObject playListRealmObject);

    List<PlayListRealmObject> getAllPlaylist();

    void addSongToPlaylist(long playlistId, SongRealmObject song);

    void saveSong(SongModel song);

    List<SongModel> getAllSong();

    void updateSong(SongModel song);

    void removePlaylist(PlayListRealmObject playlist);

    List<SongModel> getSongsFromPlaylist(long playlistId);

    void removeSongOfPlaylist(long playlistId, SongModel songModel);

    void setDataSongLocal(List<SongModel> songs);

    List<SongModel> getFavoriteList();

    void setFavorite(int songId);

    void unFavorite(int songId);
}
