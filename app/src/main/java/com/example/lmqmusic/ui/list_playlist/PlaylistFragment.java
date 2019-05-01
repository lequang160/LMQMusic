package com.example.lmqmusic.ui.list_playlist;

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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lmqmusic.Application;
import com.example.lmqmusic.R;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.ui.adapter.PlaylistAdapter;
import com.example.lmqmusic.ui.base.fragment.FragmentMVP;
import com.example.lmqmusic.ui.list_playlist.dialog_new_playlist.DialogAddPlaylist;
import com.example.lmqmusic.ui.playlist_detail.PlaylistDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class PlaylistFragment extends FragmentMVP<PlaylistPresenter, IPlaylist> implements DialogAddPlaylist.CreatePlaylistListener, IPlaylist {

    private PlaylistViewModel mViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    PlaylistAdapter mAdapter;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlist_fragment, container, false);

        ButterKnife.bind(this, rootView);
        setupRecycleView(inflater);
        return rootView;
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new PlaylistPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        mViewModel.setLiveData(AppDataManager.getInstance().getAllPlaylist());
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPopupOption(view, adapter, position);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                long playlistId = ((PlayListRealmObject) adapter.getData().get(position)).getId();
                String playlistName = ((PlayListRealmObject) adapter.getData().get(position)).getName();
                showPlaylistDetail(playlistId, playlistName);
            }
        });
        onSubscribeData();
    }

    private void showPlaylistDetail(long playlistId, String playlistName) {
        PlaylistDetailFragment playlistDetailFragment = PlaylistDetailFragment.newInstance(playlistId, playlistName);
        getFragNav().pushFragment(playlistDetailFragment);
    }

    private void setupRecycleView(LayoutInflater inflater) {
        mAdapter = new PlaylistAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());

        View header = inflater.inflate(R.layout.item_create_new_playlist, null, false);
        mAdapter.addHeaderView(header);
        mAdapter.getHeaderLayout().findViewById(R.id.button_add_new_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupEditPlaylist(null);
            }
        });
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onCreate(PlayListRealmObject playListRealmObject) {
        mPresenter.addPlaylistToDatabase(playListRealmObject);
        mViewModel.setLiveData(mPresenter.getAllPlaylist());
        Toast.makeText(mActivity, "Created", Toast.LENGTH_SHORT).show();
    }

    void onSubscribeData() {
        mViewModel.getLiveData().observe(this, new Observer<List<PlayListRealmObject>>() {
            @Override
            public void onChanged(@Nullable List<PlayListRealmObject> playListRealmObjects) {
                if (playListRealmObjects == null) return;
                mAdapter.replaceData(playListRealmObjects);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showPopupOption(View v, BaseQuickAdapter adapter, int position) {
        PopupMenu popup = new PopupMenu(Application.Context, v);
        popup.getMenuInflater().inflate(R.menu.playlist_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.add_to_now:
                        Toast.makeText(mActivity, "add nek", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit:
                        PlayListRealmObject object = (PlayListRealmObject) adapter.getData().get(position);
                        showPopupEditPlaylist(object);
                        break;
                    case R.id.remove_playlist:
                        mPresenter.removePlaylist((PlayListRealmObject) adapter.getData().get(position));
                        reloadData();
                        Toast.makeText(mActivity, "Removed", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
    }

    @OnClick(R.id.button_back)
    void clickBack() {
        getFragNav().popFragment();
    }

    private void showPopupEditPlaylist(@Nullable PlayListRealmObject playListRealmObject) {
        if (playListRealmObject != null) {
            DialogAddPlaylist addPlaylist = DialogAddPlaylist.newInstance(playListRealmObject);
            addPlaylist.setmListener(PlaylistFragment.this);
            getFragNav().showDialogFragment(addPlaylist);
        } else {
            DialogAddPlaylist addPlaylist = DialogAddPlaylist.newInstance();
            addPlaylist.setmListener(PlaylistFragment.this);
            getFragNav().showDialogFragment(addPlaylist);
        }
    }

    void reloadData() {
        mViewModel.setLiveData(mPresenter.getAllPlaylist());
    }
}
