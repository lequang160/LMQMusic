package com.example.lmqmusic.data.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import io.realm.RealmObject;

public class PlaylistModel implements MultiItemEntity{
    String name;
    int countSong;

    int type;

    public PlaylistModel() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PlaylistModel(String name, int countSong, int type) {
        this.name = name;
        this.countSong = countSong;
        this.type = type;
    }

    public int getCountSong() {
        return countSong;
    }

    public void setCountSong(int countSong) {
        this.countSong = countSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
