package com.example.lmqmusic.data;


import com.example.lmqmusic.Application;
import com.example.lmqmusic.data.database.AppDatabaseHelper;
import com.example.lmqmusic.data.database.DatabaseHelper;
import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.example.lmqmusic.utils.ModelHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppDataManager implements DataManager {
    private List<SongModel> dataLocalSong = new ArrayList<>();
    private List<SongModel> dataNowPlaying;
    private static AppDataManager INSTANCE;
    private final Gson mGson;
    private List<Item> itemListDashBoard;
    DatabaseHelper databaseHelper;
    List<PlayListRealmObject> listPlaylist;
    List<SongRealmObject> listSong;

    private AppDataManager() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();
        databaseHelper = AppDatabaseHelper.getINSTANCE();
        dataLocalSong.addAll(getAllSong());
        dataNowPlaying = dataLocalSong;
        listPlaylist = getAllPlaylist();
    }

    public static AppDataManager getInstance() {
        if (INSTANCE == null) {
            return INSTANCE = new AppDataManager();
        }
        return INSTANCE;
    }

    public List<SongModel> getDataLocalSong() {
        this.dataLocalSong.clear();
        this.dataLocalSong.addAll(ModelHelper.ConvertSongRealmObjectToSongModel(databaseHelper.getAllSong()));
        return this.dataLocalSong;
    }

    public List<SongModel> getDataNowPlaying() {
        return dataNowPlaying;
    }

    public void setDataNowPlaying(List<SongModel> dataPlaylist) {
        this.dataNowPlaying = dataPlaylist;
    }

    @Override
    public List<Item> getAllItem() {
        if (itemListDashBoard == null) {

            try {
                String jsonLetters = "";
                InputStream inputStream = Application.Context.getAssets().open("data.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                jsonLetters = new String(buffer, "UTF-8");

                Type listType = new TypeToken<List<Item>>() {
                }.getType();

                return itemListDashBoard = mGson.fromJson(jsonLetters, listType);
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return itemListDashBoard;
    }

    @Override
    public void addPlaylistToDatabase(PlayListRealmObject playListRealmObject) {
        databaseHelper.addPlaylistToDatabase(playListRealmObject);
    }

    @Override
    public List<PlayListRealmObject> getAllPlaylist() {
        return databaseHelper.getAllPlaylist();
    }

    @Override
    public void addSongToPlaylist(long playlistId, SongRealmObject song) {
        databaseHelper.addSongToPlaylist(playlistId, song);
    }

    @Override
    public PlayListRealmObject getPlaylist(long id) {
        return null;
    }

    @Override
    public void addSongToFavorite(SongModel song) {

        SongRealmObject object = ModelHelper.ConvertSongModelToSongRealmObject(song);
        databaseHelper.updateSong(object);
    }

    @Override
    public void saveSong(SongRealmObject song) {
        databaseHelper.saveSong(song);
    }

    @Override
    public SongModel getSong(long songId) {
        return ModelHelper.ConvertSongRealmObjectToSongModel(databaseHelper.getSong(songId));
    }

    @Override
    public List<SongModel> getAllFavoritesSong() {
        return ModelHelper.ConvertSongRealmObjectToSongModel(databaseHelper.getAllFavoritesSong());
    }

    @Override
    public List<SongModel> getAllSong() {
        return ModelHelper.ConvertSongRealmObjectToSongModel(databaseHelper.getAllSong());
    }

    @Override
    public void updateSong(SongModel song) {
        SongRealmObject object = ModelHelper.ConvertSongModelToSongRealmObject(song);
        databaseHelper.updateSong(object);
    }

    @Override
    public void removePlaylist(PlayListRealmObject playlist) {
        databaseHelper.removePlaylist(playlist);
    }

    @Override
    public List<SongModel> getSongsFromPlaylist(long playlistId) {
        return ModelHelper.ConvertSongRealmObjectToSongModel(databaseHelper.getSongsFromPlaylist(playlistId));
    }

    @Override
    public void removeSongOfPlaylist(long playlistId, SongModel songModel) {
        databaseHelper.removeSongOfPlaylist(playlistId, ModelHelper.ConvertSongModelToSongRealmObject(songModel));
    }
}
