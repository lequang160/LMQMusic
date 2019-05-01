package com.example.lmqmusic.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;


public class PlaylistAdapter extends BaseQuickAdapter<PlayListRealmObject, BaseViewHolder> {

    public PlaylistAdapter() {
        super(R.layout.item_folder_playlist);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlayListRealmObject item) {
        setupItem(helper,item);
    }


    void setupItem(BaseViewHolder helper, PlayListRealmObject item) {
        helper.setText(R.id.playlist_name, item.getName());
        helper.setText(R.id.count_song, item.getCountSong() + "");
        helper.addOnClickListener(R.id.button_more);
    }
}
