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


    public SongModel(long id, String artist, String title, String displayName, String streamUri, String albumId, long date, long duration, long fileSize, boolean isFavorite) {
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

    protected SongModel(Parcel in) {
        id = in.readLong();
        artist = in.readString();
        title = in.readString();
        displayName = in.readString();
        streamUri = in.readString();
        albumId = in.readString();
        date = in.readLong();
        duration = in.readLong();
        fileSize = in.readLong();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<SongModel> CREATOR = new Creator<SongModel>() {
        @Override
        public SongModel createFromParcel(Parcel in) {
            return new SongModel(in);
        }

        @Override
        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(displayName);
        dest.writeString(streamUri);
        dest.writeString(albumId);
        dest.writeLong(date);
        dest.writeLong(duration);
        dest.writeLong(fileSize);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
