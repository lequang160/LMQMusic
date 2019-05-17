package com.example.lmqmusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SongModel implements Parcelable {
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
    private String thumb;


    public SongModel(long id, String artist, String title, String displayName, String streamUri, String albumId, long date, long duration, long fileSize, boolean isFavorite, String thumb) {
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
        this.thumb = thumb;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.artist);
        dest.writeString(this.title);
        dest.writeString(this.displayName);
        dest.writeString(this.streamUri);
        dest.writeString(this.albumId);
        dest.writeLong(this.date);
        dest.writeLong(this.duration);
        dest.writeLong(this.fileSize);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.thumb);
    }

    protected SongModel(Parcel in) {
        this.id = in.readLong();
        this.artist = in.readString();
        this.title = in.readString();
        this.displayName = in.readString();
        this.streamUri = in.readString();
        this.albumId = in.readString();
        this.date = in.readLong();
        this.duration = in.readLong();
        this.fileSize = in.readLong();
        this.isFavorite = in.readByte() != 0;
        this.thumb = in.readString();
    }

    public static final Creator<SongModel> CREATOR = new Creator<SongModel>() {
        @Override
        public SongModel createFromParcel(Parcel source) {
            return new SongModel(source);
        }

        @Override
        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };
}
