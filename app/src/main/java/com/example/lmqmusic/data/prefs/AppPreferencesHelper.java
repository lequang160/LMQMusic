package com.example.lmqmusic.data.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lmqmusic.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_FILE_NAME = "LMQ Music";
    private List<Integer> favoriteList = new ArrayList<>();
    private final SharedPreferences mPref;
    private final SharedPreferences.Editor mEditor;
    private final Gson mGson;

    @SuppressLint("CommitPrefEdits")
    public AppPreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();
    }

    @Override
    public List<Integer> getAllFavorite() {
        String str = mPref.getString(Constants.Extra.FAVORITE,"");
        Type type = new TypeToken<List<Integer>>(){}.getType();
        if(str == null || str.length() == 0)
            return new ArrayList<>();
        favoriteList.clear();
        favoriteList.addAll( mGson.fromJson(str, type));
        return favoriteList;
    }

    @Override
    public void setNewFavorite(int songID) {
        favoriteList.add(songID);
        mEditor.putString(Constants.Extra.FAVORITE, mGson.toJson(favoriteList));
        mEditor.apply();
    }

    @Override
    public void unFavorite(int songId) {
        favoriteList.remove(Integer.valueOf(songId));
        mEditor.putString(Constants.Extra.FAVORITE, mGson.toJson(favoriteList));
        mEditor.apply();
    }
}
