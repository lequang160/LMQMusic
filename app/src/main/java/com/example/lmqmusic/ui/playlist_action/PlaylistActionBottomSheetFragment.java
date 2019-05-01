package com.example.lmqmusic.ui.playlist_action;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
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
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;
import com.example.lmqmusic.ui.adapter.PlaylistAdapter;
import com.example.lmqmusic.ui.list_playlist.PlaylistViewModel;
import com.example.lmqmusic.ui.list_playlist.dialog_new_playlist.DialogAddPlaylist;
import com.example.lmqmusic.ui.main.Main2Activity;
import com.example.lmqmusic.utils.ModelHelper;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class PlaylistActionBottomSheetFragment extends BottomSheetDialogFragment implements DialogAddPlaylist.CreatePlaylistListener {

    private PlaylistViewModel mViewModel;

    private static final String ARG_PLAYLIST_SONG = "ARG_PLAYLIST_SONG";

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    PlaylistAdapter mAdapter;

    SongModel mSongModel;

    AppDataManager mAppDataManager = AppDataManager.getInstance();

    ActionListener actionListener;


    public PlaylistActionBottomSheetFragment() {
        // Required empty public constructor
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public static PlaylistActionBottomSheetFragment newInstance(SongModel songModel) {
        PlaylistActionBottomSheetFragment fragment = new PlaylistActionBottomSheetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLAYLIST_SONG, songModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.AppTheme);

        if (getArguments() != null) {
            mSongModel = getArguments().getParcelable(ARG_PLAYLIST_SONG);
        }
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
                long playlistId = ((PlayListRealmObject)adapter.getData().get(position)).getId();
                mAppDataManager.addSongToPlaylist(playlistId, ModelHelper.ConvertSongModelToSongRealmObject(mSongModel));
                reloadData();
            }
        });
        onSubscribeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_action_playlist, container, false);
        ButterKnife.bind(this, rootView);
        setupRecycleView(inflater);
        return rootView;
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

    @SuppressLint("RestrictedApi")
    private void showPopupOption(View v, BaseQuickAdapter adapter, int position) {
        PopupMenu popup = new PopupMenu(Application.Context, v);
        popup.getMenuInflater().inflate(R.menu.playlist_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.add_to_now:
                        break;
                    case R.id.edit:
                        PlayListRealmObject object = (PlayListRealmObject) adapter.getData().get(position);
                        showPopupEditPlaylist(object);
                        break;
                    case R.id.remove_playlist:
                        mAppDataManager.removePlaylist((PlayListRealmObject) adapter.getData().get(position));
                        reloadData();
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

    private void showPopupEditPlaylist(@Nullable PlayListRealmObject playListRealmObject) {
        DialogAddPlaylist addPlaylist = DialogAddPlaylist.newInstance(playListRealmObject);
        addPlaylist.setmListener(this);
        addPlaylist.show(getChildFragmentManager(),"abc");
    }

    void reloadData() {
        mViewModel.setLiveData(mAppDataManager.getAllPlaylist());
    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onCreate(PlayListRealmObject playListRealmObject) {
        mAppDataManager.addPlaylistToDatabase(playListRealmObject);
        reloadData();
    }

    public interface ActionListener {
        void onRename(String name);

        void onDelete();
    }

}
