package com.example.lmqmusic.data.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SongRealmObject extends RealmObject {

    @PrimaryKey
    public long id;
    private String artist;
    private String title;
    private String displayName;
    private String streamUri;
    private String albumId;
    private long date;
    private long duration;
    private long fileSize;
    private boolean isFavorite;

    public SongRealmObject(long id, String artist, String title, String displayName, String streamUri, String albumId, long date, long duration, long fileSize, boolean isFavorite) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.displayName = displayName;
        this.streamUri = streamUri;
        this.albumId = albumId;
        this.date = date;
        this.duration = duration;
        this.fileSize = fileSize;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public SongRealmObject() {
    }

    public SongRealmObject(long id, String artist, String title, String displayName, String streamUri, String albumId, long date, long duration, long fileSize) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.displayName = displayName;
        this.streamUri = streamUri;
        this.albumId = albumId;
        this.date = date;
        this.duration = duration;
        this.fileSize = fileSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
