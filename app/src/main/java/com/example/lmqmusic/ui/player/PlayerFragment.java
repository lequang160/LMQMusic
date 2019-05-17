package com.example.lmqmusic.ui.player;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lmqmusic.Application;
import com.example.lmqmusic.Constants;
import com.example.lmqmusic.MediaController;
import com.example.lmqmusic.MediaService;
import com.example.lmqmusic.PlaylistDialogFragment;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.alarm.AlarmFragment;
import com.example.lmqmusic.ui.base.fragment.FragmentMVP;
import com.example.lmqmusic.ui.playlist_action.PlaylistActionBottomSheetFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerFragment extends FragmentMVP<PlayerPresenter, IPlayer> implements MediaController.PlayerListener, IPlayer {

    MediaController mediaController;

    SongModel song;
    boolean isRepeat;
    boolean isShuffle;
    boolean isPlaying;


    @BindView(R.id.seekBar)
    SeekBar mSeekBar;

    @BindView(R.id.button_pause)
    AppCompatImageButton mButtonPause;

    @BindView(R.id.button_play)
    AppCompatImageButton mButtonPlay;

    @BindView(R.id.song_name)
    TextView mTextSongName;

    @BindView(R.id.song_artist)
    TextView mTextSongArtist;

    @BindView(R.id.button_favorite)
    AppCompatImageButton mButtonFavorite;

    @BindView(R.id.button_more)
    AppCompatImageButton mButtonMore;

    @BindView(R.id.button_down)
    AppCompatImageButton mButtonDown;

    @BindView(R.id.button_playlist)
    AppCompatImageButton mButtonPlaylist;

    @BindView(R.id.text_duration)
    TextView mTextDuration;

    @BindView(R.id.text_display_time)
    TextView mTextDisplayTime;

    @BindView(R.id.song_artist_playback)
    TextView mTextSongArtistPlayBack;

    @BindView(R.id.song_name_playback)
    TextView mTextSongNamePlayBack;

    @BindView(R.id.button_pause_playback)
    AppCompatImageButton mButtonPausePlayBack;

    @BindView(R.id.button_play_playback)
    AppCompatImageButton mButtonPlayPlayBack;

    @BindView(R.id.button_repeat)
    AppCompatImageButton mButtonRepeat;

    @BindView(R.id.button_shuffle)
    AppCompatImageButton mButtonShuffle;

    @BindView(R.id.image_song)
    CircleImageView mLogo;

    @BindView(R.id.image_playback)
    AppCompatImageView mImagePlayback;

    List<SongModel> data = new ArrayList<>();

    private PlayerViewModel mViewModel;

    Handler handler = new Handler();

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.player, container, false);
        ButterKnife.bind(this, rootView);

        data.addAll(AppDataManager.getInstance().getAllSong());
        mediaController = MediaController.newInstance();
        mediaController.setListener(this);
        Application.mService.onNotify();


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaController.seekTo(seekBar.getProgress());
            }
        });
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new PlayerPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        if (song == null) {
            getSliding().hideSliding();
        } else {
            getSliding().DownSliding();
        }
        // TODO: Use the ViewModel
    }

    @OnClick(R.id.button_more)
    void moreOption() {
        showPopupOption(mButtonMore);
    }

    @OnClick(R.id.button_play)
    void playMusic() {
        runCommand(Constants.ACTION.PLAY_ACTION);
    }

    @OnClick(R.id.button_next)
    void nextSong() {
        runCommand(Constants.ACTION.NEXT_ACTION);
    }

    @OnClick(R.id.button_previous)
    void previousSong() {
        runCommand(Constants.ACTION.PREV_ACTION);
    }

    @OnClick(R.id.button_pause)
    void pauseSong() {
        runCommand(Constants.ACTION.PAUSE_ACTION);
    }

    @OnClick(R.id.button_repeat)
    void repeatSong() {
        this.isRepeat = !this.isRepeat;
        mediaController.setRepeat(this.isRepeat);
        mButtonRepeat.setImageResource(this.isRepeat ? R.drawable.ic_repeat_one : R.drawable.ic_repeat_disable);
    }

    @OnClick(R.id.button_shuffle)
    void shuffleSongs() {
        this.isShuffle = !this.isShuffle;
        mediaController.setShuffle(this.isShuffle);
        mButtonShuffle.setImageResource(this.isShuffle ? R.drawable.ic_shuffle : R.drawable.ic_shuffle_disable);
    }

    @OnClick(R.id.button_playlist)
    void showPlaylist() {
        PlaylistDialogFragment playlistDialogFragment = PlaylistDialogFragment.newInstance();
        getFragNav().showBottomSheetDialogFragment(playlistDialogFragment);
    }

    @OnClick(R.id.button_pause_playback)
    void clickButtonPausePlayBack() {
        runCommand(Constants.ACTION.PAUSE_ACTION);
    }

    @OnClick(R.id.button_play_playback)
    void clickButtonPlayPlayBack() {
        runCommand(Constants.ACTION.PLAY_ACTION);
    }

    @OnClick(R.id.button_next_playback)
    void clickButtonNextPlayBack() {
        runCommand(Constants.ACTION.NEXT_ACTION);
    }


    @OnClick(R.id.button_previous_playback)
    void clickButtonPreviousPlayBack() {
        runCommand(Constants.ACTION.PREV_ACTION);
    }

    @OnClick(R.id.button_down)
    void clickBack() {
        getSliding().DownSliding();
    }


    @Override
    public void onStateChanged(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            getSliding().showSliding();
            mButtonPausePlayBack.setVisibility(View.VISIBLE);
            mButtonPlayPlayBack.setVisibility(View.INVISIBLE);
            mButtonPlay.setVisibility(View.INVISIBLE);
            mButtonPause.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSeekBar.setProgress(mediaController.getPosition());
                    handler.postDelayed(this, 1000);
                    mTextDisplayTime.setText(new SimpleDateFormat("mm:ss", Locale.US).format(mediaController.getPosition()));
                }
            }, 1000);
        } else {
            if (handler != null)
                handler.removeCallbacksAndMessages("");
            mButtonPlay.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.INVISIBLE);
            mButtonPlayPlayBack.setVisibility(View.VISIBLE);
            mButtonPausePlayBack.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDataSourceChange(List<SongModel> songs) {
        AppDataManager.getInstance().setDataNowPlaying(songs);
    }

    @Override
    public void onObserveCurrentPositionMediaPlayer(long currentPosition, SongModel songModel) {
        if (songModel == null) return;
        this.song = songModel;
        Glide.with(this).load(songModel.getThumb()).dontTransform().dontAnimate().into(mLogo);
        Glide.with(Application.Context).load(song.getThumb()).dontTransform().dontAnimate().into(mImagePlayback);
        mTextSongName.setText(song.getTitle());
        mTextSongArtist.setText(song.getArtist());
        mSeekBar.setMax((int) song.getDuration());
        mTextDuration.setText(new SimpleDateFormat("mm:ss", Locale.US).format(song.getDuration()));
        mTextSongArtistPlayBack.setText(song.getArtist());
        mTextSongNamePlayBack.setText(song.getTitle());
        mButtonFavorite.setImageResource(song.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        mediaController.seekTo((int) currentPosition);
        runCommand(isPlaying ? Constants.ACTION.PLAY_ACTION : Constants.ACTION.PAUSE_ACTION);
        getSliding().showSliding();
    }

    @Override
    public void onSongChanged(SongModel song) {
        this.song = song;
        Glide.with(Application.Context).load(song.getThumb()).dontTransform().dontAnimate().into(mLogo);
        Glide.with(Application.Context).load(song.getThumb()).dontTransform().dontAnimate().into(mImagePlayback);

        mTextSongName.setText(song.getTitle());
        mTextSongArtist.setText(song.getArtist());
        mSeekBar.setMax((int) song.getDuration());
        mTextDuration.setText(new SimpleDateFormat("mm:ss", Locale.US).format(song.getDuration()));
        mTextSongArtistPlayBack.setText(song.getArtist());
        mTextSongNamePlayBack.setText(song.getTitle());
        mButtonFavorite.setImageResource(song.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        getSliding().showSliding();
    }

    @OnClick(R.id.button_favorite)
    void clickFavorite() {
        song.setFavorite(!song.isFavorite());
        mButtonFavorite.setImageResource(song.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        mPresenter.saveSongToFavorite(song);
    }

    @SuppressLint("RestrictedApi")
    private void showPopupOption(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.player_song_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.add_to_playlist:
                        getFragNav().showBottomSheetDialogFragment(PlaylistActionBottomSheetFragment.newInstance(song));
                        break;
                    case R.id.alarm:
                        AlarmFragment alarmFragment = AlarmFragment.newInstance();
                        getFragNav().pushFragment(alarmFragment);
                        break;
                }
                return true;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(v.getContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages("");
        }
    }

    //this func only use with media playback because my services run with action(Intent).
    private void runCommand(String command) {
        Intent serviceIntent = new Intent(Application.Context, MediaService.class);
        serviceIntent.setAction(command);
        Application.Context.startService(serviceIntent);
    }
}
