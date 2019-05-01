package com.example.lmqmusic.data.database;

import android.content.Context;
import android.widget.Toast;

import com.example.lmqmusic.Application;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.data.model.realm.SongRealmObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AppDatabaseHelper implements DatabaseHelper {
    static AppDatabaseHelper INSTANCE;
    private Realm mRealm;

    public static AppDatabaseHelper newInstance(Context context) {
        if (INSTANCE == null) {
            return INSTANCE = new AppDatabaseHelper(context);
        }
        return INSTANCE;

    }

    public static AppDatabaseHelper getINSTANCE() {
        return INSTANCE;
    }

    public Realm getRealm() {
        return mRealm;
    }

    private AppDatabaseHelper(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(2)
                .migration(new Migration())
                .build();
        mRealm = Realm.getInstance(config);
    }

    @Override
    public void addPlaylistToDatabase(PlayListRealmObject playListRealmObject) {
        mRealm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                if (playListRealmObject.getId() > 0) {
                    realm.insertOrUpdate(playListRealmObject);
                } else {
                    // increment index
                    Number currentIdNum = realm.where(PlayListRealmObject.class).max("id");
                    int nextId;
                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    playListRealmObject.setId(nextId);
                    realm.insertOrUpdate(playListRealmObject);
                }
            }
        });
    }

    @Override
    public List<PlayListRealmObject> getAllPlaylist() {

        List<PlayListRealmObject> listRealmObjects = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<PlayListRealmObject> playListRealmObjects = realm.where(PlayListRealmObject.class).findAll();
                listRealmObjects.addAll(playListRealmObjects);
            }
        });
        return listRealmObjects;
    }

    @Override
    public void addSongToPlaylist(long playlistId, SongRealmObject song) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PlayListRealmObject playListRealmObject = realm.where(PlayListRealmObject.class).equalTo("id", playlistId).findFirst();
                if (playListRealmObject != null) {
                    RealmList<SongRealmObject> list = playListRealmObject.getListSong();
                    for (SongRealmObject object : list) {
                        if (object.getId() == song.getId()) {
                            Toast.makeText(Application.Context, "This song is really exits !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    list.add(song);
                    playListRealmObject.setListSong(list);
                }

            }
        });
    }

    @Override
    public PlayListRealmObject getPlaylist(long id) {
        return null;
    }

    @Override
    public void addSongToFavorite(SongRealmObject song) {

    }

    @Override
    public void saveSong(SongRealmObject song) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SongRealmObject res = realm.where(SongRealmObject.class).equalTo("id", song.getId()).findFirst();
                if (res == null) {
                    realm.insert(song);
                }
            }
        });
    }

    @Override
    public SongRealmObject getSong(long songId) {
        return null;
    }

    @Override
    public List<SongRealmObject> getAllFavoritesSong() {
        List<SongRealmObject> data = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SongRealmObject> list = realm.where(SongRealmObject.class).equalTo("isFavorite", true).findAll();
                if (list != null)
                    data.addAll(list);
            }
        });
        return data;
    }

    @Override
    public void updateSong(SongRealmObject song) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(song);
            }
        });
    }

    @Override
    public void saveSongFromWifiTransfer(SongRealmObject song) {

    }

    @Override
    public List<SongRealmObject> getAllSong() {
        return mRealm.where(SongRealmObject.class).findAll();
    }

    @Override
    public void removePlaylist(PlayListRealmObject playList) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                playList.deleteFromRealm();
            }
        });
    }

    @Override
    public List<SongRealmObject> getSongsFromPlaylist(long playlistId) {
        RealmList<SongRealmObject> data = new RealmList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PlayListRealmObject playListRealmObject = realm.where(PlayListRealmObject.class).equalTo("id", playlistId).findFirst();
                if (playListRealmObject != null && playListRealmObject.getCountSong() > 0)
                    data.addAll(playListRealmObject.getListSong());

            }
        });
        return data;
    }

    @Override
    public void removeSongOfPlaylist(long playlistId, SongRealmObject songRealmObject) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PlayListRealmObject playListRealmObject = realm.where(PlayListRealmObject.class).equalTo("id",playlistId).findFirst();
                if(playListRealmObject != null && playListRealmObject.getCountSong() > 0)
                {
                    RealmList<SongRealmObject> list = playListRealmObject.getListSong();
                    for(int i = 0; i <  playListRealmObject.getCountSong(); i++)
                    {
                        if((list.get(i) != null ? list.get(i).getId() : 0) == songRealmObject.getId())
                        {
                            list.remove(i);
                            Toast.makeText(Application.Context, "Removed !", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        });
    }
}
