package com.example.lmqmusic.ui.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.SongRealmObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NowPlayListAdapter extends BaseQuickAdapter<SongModel, BaseViewHolder> {
    public NowPlayListAdapter(@Nullable List<SongModel> data) {
        super(R.layout.item_playlist_nowplaying, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SongModel item) {
        helper.setText(R.id.song_name, item.getTitle());
        helper.setText(R.id.song_artist, item.getArtist());

        SimpleDateFormat newFormat = new SimpleDateFormat("mm:ss", Locale.US);
        String finalString = newFormat.format(item.getDuration());
        helper.setText(R.id.duration, finalString);
        helper.addOnClickListener(R.id.image_more);
        ImageView imageView = helper.getView(R.id.image);
        Glide.with(mContext)
                .load(Uri.parse(item.getThumb()))
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.ic_music_player_song)
                .into(imageView);
    }

}
