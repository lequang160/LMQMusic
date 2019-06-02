package com.example.lmqmusic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.SongModel;
import com.example.lmqmusic.ui.adapter.NowPlayListAdapter;
import com.example.lmqmusic.ui.main.Main2Activity;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;


public class PlaylistDialogFragment extends BottomSheetDialogFragment {


    private Listener mListener;
    RecyclerView mRecyclerView;
    private List<SongModel> mData = new ArrayList<>();
    private NowPlayListAdapter mAdapter;
    private int currentIndex = 0;


    // TODO: Customize parameters
    public static PlaylistDialogFragment newInstance() {
        final PlaylistDialogFragment fragment = new PlaylistDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
        mRecyclerView = root.findViewById(R.id.list);
        currentIndex = Application.mService.getCurrentIndex();
        ButterKnife.bind(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mData.addAll(AppDataManager.getInstance().getDataNowPlaying());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NowPlayListAdapter(mData, NowPlayListAdapter.AdapterType.NOW_PLAYING);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.item_dashboard, null, false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addFooterView(view1);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaController.newInstance().setDataSource(mData);
                MediaController.newInstance().PlayAtPosition(position);
                ((Main2Activity) Objects.requireNonNull(getActivity())).runCommand(Constants.ACTION.PLAY_ACTION);
            }
        });
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                View imageSync = mAdapter.getViewByPosition(mRecyclerView, currentIndex, R.id.image_sync);
                if(imageSync != null)imageSync.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        final Fragment parent = getParentFragment();
//        if (parent != null) {
//            mListener = (Listener) parent;
//        } else {
//            mListener = (Listener) context;
//        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(int position);
    }
}
