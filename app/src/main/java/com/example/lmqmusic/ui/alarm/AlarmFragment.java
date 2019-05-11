package com.example.lmqmusic.ui.alarm;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lmqmusic.Application;
import com.example.lmqmusic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmFragment extends Fragment {
    @BindView(R.id.check_0)
    ImageView mCheck0;
    @BindView(R.id.check_1)
    ImageView mCheck1;
    @BindView(R.id.check_2)
    ImageView mCheck2;
    @BindView(R.id.check_3)
    ImageView mCheck3;
    @BindView(R.id.check_4)
    ImageView mCheck4;
    @BindView(R.id.button_save)
    ImageButton mSaveBtn;

    private int position = -1;

    @OnClick({R.id.end_of_this_song_bt})
    void endOfThisSongClick() {
        position = 0;
        showCheck(position);
    }

    @OnClick(R.id.minutes_15_bt)
    void minutes15Click() {
        position = 1;
        showCheck(position);
    }

    @OnClick(R.id.minutes_20_bt)
    void minutes20Click() {
        position = 2;
        showCheck(position);
    }

    @OnClick(R.id.minutes_30_bt)
    void minutes30Click() {
        position = 3;
        showCheck(position);
    }

    @OnClick(R.id.minutes_60_bt)
    void minutes60Click() {
        position = 4;
        showCheck(position);
    }

    private void showCheck(int position) {
        mSaveBtn.setVisibility(View.VISIBLE);
        mCheck0.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
        mCheck1.setVisibility(position == 1 ? View.VISIBLE : View.INVISIBLE);
        mCheck2.setVisibility(position == 2 ? View.VISIBLE : View.INVISIBLE);
        mCheck3.setVisibility(position == 3 ? View.VISIBLE : View.INVISIBLE);
        mCheck4.setVisibility(position == 4 ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.button_save)
    void saveButtonClick() {
        switch (position) {
            case 0:
                Application.mService.isEndOfSong(true);
                break;
            case 1:
                Application.mService.setTimeAlarm(900000);
                break;
            case 2:
                Application.mService.setTimeAlarm(1200000);
                break;
            case 3:
                Application.mService.setTimeAlarm(1800000);
                break;
            case 4:
                Application.mService.setTimeAlarm(3600000);
                break;
            default:
                break;
        }
        mSaveBtn.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.button_back)
    void back() {
        getActivity().onBackPressed();
    }

    private AlarmViewModel mViewModel;

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alarm_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        showCheck(position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        // TODO: Use the ViewModel
    }

}
