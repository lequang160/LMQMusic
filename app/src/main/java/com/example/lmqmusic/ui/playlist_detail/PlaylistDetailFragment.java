package com.example.lmqmusic.ui.playlist_detail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lmqmusic.Application;
import com.example.lmqmusic.Constants;
import com.example.lmqmusic.MediaController;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.adapter.NowPlayListAdapter;
import com.example.lmqmusic.ui.base.fragment.FragmentMVP;
import com.example.lmqmusic.ui.main.Main2Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class PlaylistDetailFragment extends FragmentMVP<PlaylistDetailPresenter, IPlaylistDetail> implements IPlaylistDetail {

    private PlaylistDetailViewModel mViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar_title)
    TextView mTitleTV;

    NowPlayListAdapter mAdapter;
    static String PLAYLIST_DETAIL_ARGUMENT_ID = "PLAYLIST_DETAIL_ARGUMENT_ID";
    static String PLAYLIST_DETAIL_ARGUMENT_NAME = "PLAYLIST_DETAIL_ARGUMENT_NAME";
    long mPlaylistId;
    String mTitle;

    List<SongModel> mData = new ArrayList<>();

    public static PlaylistDetailFragment newInstance(long playListId, String name) {

        Bundle bundle = new Bundle();
        bundle.putLong(PLAYLIST_DETAIL_ARGUMENT_ID, playListId);
        bundle.putString(PLAYLIST_DETAIL_ARGUMENT_NAME, name);
        PlaylistDetailFragment playlistDetailFragment = new PlaylistDetailFragment();
        playlistDetailFragment.setArguments(bundle);
        return playlistDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.AppTheme);

        if (getArguments() != null) {
            mPlaylistId = getArguments().getLong(PLAYLIST_DETAIL_ARGUMENT_ID);
            mTitle = getArguments().getString(PLAYLIST_DETAIL_ARGUMENT_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlist_detail_fragment, container, false);

        ButterKnife.bind(this, rootView);
        mTitleTV.setText(mTitle);
        setupRecycleView(inflater);
        return rootView;
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new PlaylistDetailPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlaylistDetailViewModel.class);
        mViewModel.setData(mPresenter.getSongsFromPlaylist(mPlaylistId));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPopupOption(view, adapter, position);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaController.newInstance().setDataSource(mData);
                MediaController.newInstance().PlayAtPosition(position);
                ((Main2Activity) Objects.requireNonNull(getActivity())).runCommand(Constants.ACTION.PLAY_ACTION);
            }
        });
        onSubscribeData();
    }

    private void setupRecycleView(LayoutInflater inflater) {
        mAdapter = new NowPlayListAdapter(null, NowPlayListAdapter.AdapterType.PLAY_LIST);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    void onSubscribeData() {
        mViewModel.getData().observe(this, new Observer<List<SongModel>>() {
            @Override
            public void onChanged(@Nullable List<SongModel> playListRealmObjects) {
                if (playListRealmObjects == null) return;
                mAdapter.replaceData(playListRealmObjects);
                mData.clear();
                mData.addAll(playListRealmObjects);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showPopupOption(View v, BaseQuickAdapter adapter, int position) {

        SongModel song = ((SongModel) adapter.getData().get(position));
        PopupMenu popup = new PopupMenu(Application.Context, v);
        popup.getMenuInflater().inflate(R.menu.playlist_detail, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.remove_song:
                        mPresenter.removeSong(mPlaylistId, song);
                        reloadData();
                        break;
                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(v.getContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
    }

    @OnClick(R.id.button_back)
    void clickBack() {
        getFragNav().popFragment();
    }

    void reloadData() {
        mViewModel.setData(mPresenter.getSongsFromPlaylist(mPlaylistId));
    }
}
