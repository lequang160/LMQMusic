package com.example.lmqmusic.data.model;

import java.util.List;

public class PlaylistModel {
    private int id;

    private String name = "";

    private List<Integer> songList;

    public PlaylistModel(int id, String name, List<Integer> songList) {
        this.id = id;
        this.name = name;
        this.songList = songList;
    }

    public PlaylistModel(int id) {
        this.id = id;
    }

    public PlaylistModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getSongList() {
        return songList;
    }

    public void setSongList(List<Integer> songList) {
        this.songList = songList;
    }
}
