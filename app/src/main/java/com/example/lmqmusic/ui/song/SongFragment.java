package com.example.lmqmusic.ui.song;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lmqmusic.Constants;
import com.example.lmqmusic.MediaController;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.SongRealmObject;
import com.example.lmqmusic.ui.adapter.NowPlayListAdapter;
import com.example.lmqmusic.ui.base.fragment.FragmentMVP;
import com.example.lmqmusic.ui.list_favorite.FavoriteViewModel;
import com.example.lmqmusic.ui.main.Main2Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class SongFragment extends FragmentMVP<SongPresenter, ISongView> implements ISongView {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_song)
    SearchView mSearchView;

    SongViewModel mViewModel;
    Handler handler = new Handler();

    private NowPlayListAdapter mAdapter;
    List<SongModel> data = new ArrayList<>();

    public static SongFragment newInstance() {
        Bundle bundle = new Bundle();
        SongFragment songFragment = new SongFragment();
        songFragment.setArguments(bundle);
        return songFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_song, container, false);
        ButterKnife.bind(this, rootView);

        setupRecycleView();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaController.newInstance().setDataSource(data);
                MediaController.newInstance().PlayAtPosition(position);
                ((Main2Activity) Objects.requireNonNull(getActivity())).runCommand(Constants.ACTION.PLAY_ACTION);
            }
        });
        return rootView;
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new SongPresenter();
    }

    private void setupRecycleView() {
        mAdapter = new NowPlayListAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.setData(AppDataManager.getInstance().getDataLocalSong());
        mViewModel.getData().observe(this, new Observer<List<SongModel>>() {
            @Override
            public void onChanged(@Nullable List<SongModel> songModels) {
                if(songModels != null) {
                    mAdapter.replaceData(songModels);
                    data.clear();
                    data.addAll(songModels);
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
                },300);
                return false;
            }
        });

    }

    @OnClick(R.id.button_back)
    void clickBack()
    {
        getFragNav().popFragment();
    }
}
