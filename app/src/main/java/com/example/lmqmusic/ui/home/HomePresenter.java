package com.example.lmqmusic.ui.home;

import com.example.lmqmusic.data.AppDataManager;
import com.example.lmqmusic.data.model.Item;
import com.example.lmqmusic.ui.base.fragment.FragmentPresenterMVP;

import java.util.List;

public class HomePresenter extends FragmentPresenterMVP<IHome> implements IHomePresenter {
    @Override
    public List<Item> getAllItemDashBoard() {
        return AppDataManager.getInstance().getAllItem();
    }



}
