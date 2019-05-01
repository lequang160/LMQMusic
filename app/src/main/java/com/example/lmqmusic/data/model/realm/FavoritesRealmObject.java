package com.example.lmqmusic.data.model.realm;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class FavoritesRealmObject {
    @PrimaryKey
    private long id;

    private String name = "";

    private String thumb = "";

    private RealmList<SongRealmObject> listSong = new RealmList<>();

    public FavoritesRealmObject() {
    }
}
