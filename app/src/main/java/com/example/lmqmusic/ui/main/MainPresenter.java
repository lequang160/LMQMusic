package com.example.lmqmusic.ui.main;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.DataManager;
import com.example.lmqmusic.ui.base.activity.BasePresenter;


public class MainPresenter<V extends IMain> extends BasePresenter<V> implements IMainPresenter<V> {
    private DataManager mDataManager;


    MainPresenter() {
        mDataManager = AppDataManager.getInstance();
    }


}
