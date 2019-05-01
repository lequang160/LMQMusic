package com.example.lmqmusic.data.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_FILE_NAME = "KatrinaMusic";

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
}
