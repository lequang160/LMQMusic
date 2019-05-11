package com.example.lmqmusic.ui.list_favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lmqmusic.Constants;
import com.example.lmqmusic.MediaController;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.AppDataManager;
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

public class FavoriteFragment extends FragmentMVP<FavoritePresenter, IFavoriteView> implements IFavoriteView {

    private List<SongModel> mData = new ArrayList<>();

    private FavoriteViewModel mViewModel;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_song)
    SearchView mSearchView;

    Handler handler = new Handler();

    private NowPlayListAdapter mAdapter;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
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
        mPresenter = new FavoritePresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        // TODO: Use the ViewModel
        mData = mPresenter.getDataFavorite();
        mViewModel.setData(mData);
        mViewModel.getData().observe(this, new Observer<List<SongModel>>() {
            @Override
            public void onChanged(@Nullable List<SongModel> songModels) {
                if (songModels != null) {
                    mAdapter.replaceData(songModels);
                    mData.clear();
                    mData.addAll(songModels);
                }

            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mViewModel.search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                handler.removeCallbacksAndMessages("");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.search(s);
                    }
                }, 300);
                return false;
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


    }

    private void setupRecycleView() {
        mAdapter = new NowPlayListAdapter(mData);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.button_back)
    void clickBack()
    {
        getFragNav().popFragment();
    }

}
