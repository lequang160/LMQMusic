package com.example.lmqmusic.ui.base.fragment;

import android.support.v4.app.Fragment;

import com.example.lmqmusic.ui.base.activity.MvpPresenter;
import com.example.lmqmusic.ui.base.activity.MvpView;

public class FragmentPresenterMVP<V extends MvpView> implements MvpPresenter<V> {

    protected V mView;


    public void onReadyUI(Fragment fragment) {
    }

    public void onStart(Fragment fragment) {
    }

    public void onResume(Fragment fragment) {
    }

    public void onPause(Fragment fragment) {
    }

    public void onDestroyView(Fragment fragment) {
    }

    public void onDestroy(Fragment fragment) {
    }


    @Override
    public void onAttach(V mvpView) {
        mView = mvpView;
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}