package com.example.lmqmusic.ui.list_playlist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.lmqmusic.data.model.realm.PlayListRealmObject;

import java.util.List;

public class PlaylistViewModel extends ViewModel {
    MutableLiveData<List<PlayListRealmObject>> liveData = new MutableLiveData<>();


    public MutableLiveData<List<PlayListRealmObject>> getLiveData() {
        return liveData;
    }

    public void setLiveData(List<PlayListRealmObject> liveData) {
        this.liveData.setValue(liveData);
    }

}
