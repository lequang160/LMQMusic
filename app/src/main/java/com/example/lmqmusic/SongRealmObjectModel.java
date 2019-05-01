package com.example.lmqmusic;

public class SongRealmObjectModel {
    private String id;
    private String artist;
    private String title;
    private String displayName;
    private String streamUri;
    private String albumId;
    String album;
    String artistId;
    private long date;
    private long duration;
    private long fileSize;
    boolean isFavorite;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public SongRealmObjectModel(String id, String artist, String title, String displayName, String streamUri, String albumId, String album, String artistId, long date, long duration, long fileSize) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.displayName = displayName;
        this.streamUri = streamUri;
        this.albumId = albumId;
        this.album = album;
        this.artistId = artistId;
        this.date = date;
        this.duration = duration;
        this.fileSize = fileSize;
    }

    public SongRealmObjectModel(String id, String artist, String title, String displayName, String streamUri, String albumId, long date, long duration, long fileSize) {
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

    public SongRealmObjectModel() {

    }

    @Override
    public String toString() {
        return "SongRealmObjectModel{" +
                "id='" + id + '\'' +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", displayName='" + displayName + '\'' +
                ", streamUri='" + streamUri + '\'' +
                ", albumId='" + albumId + '\'' +
                ", album='" + album + '\'' +
                ", artistId='" + artistId + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", fileSize=" + fileSize +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
