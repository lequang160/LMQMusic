package com.example.lmqmusic.data.database;

import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.data.model.realm.SongRealmObject;

import java.util.List;

public interface DatabaseHelper {

    void addPlaylistToDatabase(PlayListRealmObject playListRealmObject);

    List<PlayListRealmObject> getAllPlaylist();

    void addSongToPlaylist(long playlistId, SongRealmObject song);

    PlayListRealmObject getPlaylist(long id);

    void addSongToFavorite(SongRealmObject song);

    void saveSong(SongRealmObject song);

    SongRealmObject getSong(long songId);

    List<SongRealmObject> getAllFavoritesSong();

    void updateSong(SongRealmObject song);

    void saveSongFromWifiTransfer(SongRealmObject song);

    List<SongRealmObject> getAllSong();

    void removePlaylist(PlayListRealmObject playList);

    List<SongRealmObject> getSongsFromPlaylist(long playlistId);

    void removeSongOfPlaylist(long playlistId, SongRealmObject songRealmObject);

    void updatePlaylist(PlayListRealmObject playListRealmObject);
}
