package com.example.lmqmusic.data.prefs;

import java.util.List;

public interface PreferencesHelper {
    List<Integer> getAllFavorite();

    void setNewFavorite(int songId);

    void unFavorite(int songId);
}
