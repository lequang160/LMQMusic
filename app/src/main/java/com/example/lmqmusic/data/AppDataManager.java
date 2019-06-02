package com.example.lmqmusic.data;


import com.example.lmqmusic.Application;
import com.example.lmqmusic.data.database.AppDatabaseHelper;
import com.example.lmqmusic.data.database.DatabaseHelper;
import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.example.lmqmusic.data.prefs.AppPreferencesHelper;
import com.example.lmqmusic.data.prefs.PreferencesHelper;
import com.example.lmqmusic.utils.ModelHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDataManager implements DataManager {
    private List<SongModel> dataLocalSong = new ArrayList<>();
    private List<SongModel> dataNowPlaying;
    private List<SongModel> favoriteList = new ArrayList<>();
    private static AppDataManager INSTANCE;
    private final Gson mGson;
    private List<Item> itemListDashBoard;
    DatabaseHelper databaseHelper;
    PreferencesHelper mPreferencesHelper;
    List<PlayListRealmObject> listPlaylist;

    private AppDataManager() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();
        databaseHelper = AppDatabaseHelper.getINSTANCE();
        dataLocalSong.addAll(getAllSong());
        dataNowPlaying = dataLocalSong;
        mPreferencesHelper = new AppPreferencesHelper(Application.Context);
        listPlaylist = getAllPlaylist();
    }

    public static AppDataManager getInstance() {
        if (INSTANCE == null) {
            return INSTANCE = new AppDataManager();
        }
        return INSTANCE;
    }

    public List<SongModel> getDataLocalSong() {
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
    public void saveSong(SongModel song) {
        dataLocalSong.add(song);
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

    @Override
    public void setDataSongLocal(List<SongModel> songs) {
        if (songs != null) {
            this.dataLocalSong = songs;
            getFavoriteList();
        }
    }

    @Override
    public List<SongModel> getFavoriteList() {
        favoriteList.clear();
        List<Integer> temp = mPreferencesHelper.getAllFavorite();
        Map<Integer, Integer> mMap = new HashMap<>();
        if (temp.size() > 0) {
            for(Integer id : temp)
            {
                mMap.put(id,id);
            }
            for(SongModel song : dataLocalSong)
            {
                if(mMap.get((int)song.getId()) != null && song.getId() == mMap.get((int)song.getId())){
                    song.setFavorite(true);
                    favoriteList.add(song);
                }
                else{
                    song.setFavorite(false);
                }
            }
            return favoriteList;
        }
        return favoriteList;
    }

    @Override
    public void setFavorite(int songId) {
        mPreferencesHelper.setNewFavorite(songId);
    }

    @Override
    public void unFavorite(int songId) {
        mPreferencesHelper.unFavorite(songId);
    }
}
