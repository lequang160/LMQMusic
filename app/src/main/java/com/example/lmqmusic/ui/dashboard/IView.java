package com.example.lmqmusic.ui.dashboard;

import android.app.Activity;
import android.content.Intent;

import com.example.lmqmusic.ui.base.activity.MvpView;

public interface IView extends MvpView {
    Activity getActivity();

    void pushIntent(Intent intent);
}
