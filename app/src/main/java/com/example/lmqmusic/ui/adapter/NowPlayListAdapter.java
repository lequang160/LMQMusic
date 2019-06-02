package com.example.lmqmusic.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.model.SongModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NowPlayListAdapter extends BaseQuickAdapter<SongModel, BaseViewHolder> {
    private int mType;

    public NowPlayListAdapter(@Nullable List<SongModel> data, int type) {
        super(R.layout.item_playlist_nowplaying, data);
        mType = type;
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
                .load(item.getThumb())
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.no_image)
                .into(imageView);

        switch (mType) {
            case AdapterType.SONG_FRAGMENT:
                helper.setVisible(R.id.image_more, true);
                break;
            case AdapterType.PLAY_LIST:
                helper.setVisible(R.id.image_more, true);
                break;
            case AdapterType.FAVORITE:
                helper.setVisible(R.id.image_more, true);
                break;

        }
        helper.addOnClickListener(R.id.image_more);
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getType() {
        return mType;
    }

    public @interface AdapterType {
        int SONG_FRAGMENT = 0;
        int NOW_PLAYING = 1;
        int FAVORITE = 2;
        int PLAY_LIST = 3;
    }

}
