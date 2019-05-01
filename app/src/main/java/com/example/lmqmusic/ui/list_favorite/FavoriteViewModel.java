package com.example.lmqmusic.ui.list_favorite;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.lmqmusic.data.model.SongModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteViewModel extends ViewModel {
    MutableLiveData<List<SongModel>> data = new MutableLiveData<>();
    List<SongModel> temp = new ArrayList<>();

    public MutableLiveData<List<SongModel>> getData() {
        return data;
    }

    public void setData(List<SongModel> data) {
        this.data.postValue(data);
        temp.clear();
        temp.addAll(data);
    }

    void search(String query) {
        if (query == null) {
            this.data.postValue(temp);
        } else {
            List<SongModel> res = new ArrayList<>();
            for (SongModel songModel : temp) {
                if (songModel.getDisplayName().toLowerCase().contains(query.toLowerCase())) {
                    res.add(songModel);
                }
            }
            this.data.postValue(res);
        }
    }
}
