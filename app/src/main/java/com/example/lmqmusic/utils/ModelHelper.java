package com.example.lmqmusic.utils;

import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.SongRealmObject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ModelHelper {
    public static SongModel ConvertSongRealmObjectToSongModel(SongRealmObject song) {
        return new SongModel(song.getId(), song.getArtist(), song.getTitle(), song.getDisplayName(), song.getStreamUri(), song.getAlbumId(), song.getDate(), song.getDuration(), song.getFileSize(), song.isFavorite());
    }

    public static List<SongModel> ConvertSongRealmObjectToSongModel(@Nonnull List<SongRealmObject> songs) {
        List<SongModel> list = new ArrayList<>();
        for (SongRealmObject song : songs) {
            list.add(new SongModel(song.getId(), song.getArtist(), song.getTitle(), song.getDisplayName(), song.getStreamUri(), song.getAlbumId(), song.getDate(), song.getDuration(), song.getFileSize(), song.isFavorite()));
        }
        return list;
    }

    public static SongRealmObject ConvertSongModelToSongRealmObject(SongModel song) {
        return new SongRealmObject(song.getId(), song.getArtist(), song.getTitle(), song.getDisplayName(), song.getStreamUri(), song.getAlbumId(), song.getDate(), song.getDuration(), song.getFileSize(), song.isFavorite());
    }

    public static List<SongRealmObject> ConvertSongModelToSongRealmObject(List<SongModel> songs) {
        List<SongRealmObject> list = new ArrayList<>();
        for (SongModel song : songs) {
            list.add(new SongRealmObject(song.getId(), song.getArtist(), song.getTitle(), song.getDisplayName(), song.getStreamUri(), song.getAlbumId(), song.getDate(), song.getDuration(), song.getFileSize(), song.isFavorite()));
        }
        return list;
    }
}
