package com.example.lmqmusic.data.model.realm;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlayListRealmObject extends RealmObject implements Parcelable {
    @PrimaryKey
    private long id;

    private String name = "";

    private String thumb = "";

    private RealmList<SongRealmObject> listSong = new RealmList<>();

    public PlayListRealmObject(long id) {
        this.id = id;
    }

    public PlayListRealmObject(String name) {
        this.name = name;
    }

    public PlayListRealmObject() {
    }

    public PlayListRealmObject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PlayListRealmObject(String name, String thumb, RealmList<SongRealmObject> listSong) {
        this.name = name;
        this.thumb = thumb;
        this.listSong = listSong;
    }

    protected PlayListRealmObject(Parcel in) {
        id = in.readLong();
        name = in.readString();
        thumb = in.readString();
    }

    public static final Creator<PlayListRealmObject> CREATOR = new Creator<PlayListRealmObject>() {
        @Override
        public PlayListRealmObject createFromParcel(Parcel in) {
            return new PlayListRealmObject(in);
        }

        @Override
        public PlayListRealmObject[] newArray(int size) {
            return new PlayListRealmObject[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public RealmList<SongRealmObject> getListSong() {
        return listSong;
    }

    public void setListSong(RealmList<SongRealmObject> listSong) {
        this.listSong = listSong;
    }

    public int getCountSong()
    {
        return listSong.size();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(thumb);
    }
}
