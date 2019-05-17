package com.example.lmqmusic.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.ui.adapter.DashBoardMultiItemAdapter;
import com.example.lmqmusic.ui.base.fragment.BaseFragment;
import com.example.lmqmusic.ui.base.fragment.FragmentMVP;
import com.example.lmqmusic.ui.list_favorite.FavoriteFragment;
import com.example.lmqmusic.ui.list_playlist.PlaylistFragment;
import com.example.lmqmusic.ui.song.SongFragment;
import com.example.lmqmusic.ui.wifi_transfer.WifiTransferFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class HomeFragment extends FragmentMVP<HomePresenter,IHome> implements IHome {

    private HomeViewModel mViewModel;
    DashBoardMultiItemAdapter mAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    List<Item> mListItemDashBoard = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setupRecycleView();
        return rootView;
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new HomePresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        // TODO: Use the ViewModel
    }

    private void setupRecycleView() {

        mListItemDashBoard = mPresenter.getAllItemDashBoard();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DashBoardMultiItemAdapter(mListItemDashBoard);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 2:
                        SongFragment songFragment = SongFragment.newInstance();
                        getFragNav().pushFragment(songFragment);
                        break;
                    case 3:
                        PlaylistFragment playlistFragment = PlaylistFragment.newInstance();
                        getFragNav().pushFragment(playlistFragment);
                        break;
                    case 4:
                        FavoriteFragment favoriteFragment = FavoriteFragment.newInstance();
                        getFragNav().pushFragment(favoriteFragment);
                        break;
                    case 6:
                        WifiTransferFragment wifiTransferFragment = new WifiTransferFragment();
                        getFragNav().pushFragment(wifiTransferFragment);
                        break;
                }
            }
        });
    }



}
