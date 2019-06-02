package com.example.lmqmusic.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.lmqmusic.Application;
import com.example.lmqmusic.MediaService;
import com.example.lmqmusic.R;
import com.example.lmqmusic.ui.base.activity.BaseActivity;
import com.example.lmqmusic.ui.base.fragment.BaseFragment;
import com.example.lmqmusic.ui.home.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class Main2Activity extends BaseActivity implements IMain, OnTabReselectListener, OnTabSelectListener, BaseFragment.FragNavListener, FragNavController.RootFragmentListener, BaseFragment.SlidingUpPanelLayoutListener {


    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;

    @BindView(R.id.bottomBar)
    BottomBar bottomBarMain;

    @BindView(R.id.contentContainer)
    FrameLayout contentMain;

    @BindView(R.id.layout_bottom_bar)
    LinearLayout layoutBottomBar;

    HomeFragment homeFragment = HomeFragment.newInstance();
    HomeFragment wifiTransferFragment = HomeFragment.newInstance();
    HomeFragment settingsFragment = HomeFragment.newInstance();
    private final int TAB_HOME = FragNavController.TAB1;
    private final int TAB_WIFI_TRANSFER = FragNavController.TAB2;
    private final int TAB_SETTINGS = FragNavController.TAB3;
    private List<Fragment> fragments = new ArrayList<>();
    private FragNavController mNavController;

    @Override
    protected int getContentView() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        initBottomBar(savedInstanceState);
        initSlidingLayout();

        Gson gson = new Gson();
        List<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.add(4);

        String s = gson.toJson(arr);
        Type type = new TypeToken<List<Integer>>(){}.getType();
        List<Integer> arr1 = gson.fromJson(s,type);
        Log.e("hihi",s);
        Log.e("hihi",arr1 + "");


    }

    @Override
    protected void initControls() {
        bottomBarMain.setOnTabSelectListener(this);
        bottomBarMain.setOnTabReselectListener(this);
    }

    @Override
    public int getNumberOfRootFragments() {
        return 3;
    }

    @NotNull
    @Override
    public Fragment getRootFragment(int i) {
        switch (i) {

            case TAB_HOME:
                return homeFragment;

            case TAB_WIFI_TRANSFER:
                return homeFragment;

            case TAB_SETTINGS:
                return settingsFragment;

            default:
                return homeFragment;
        }
    }

    @Override
    public void onTabReSelected(int tabId) {
        if (mNavController.isRootFragment()) {
            return;
        }

        FragNavTransactionOptions.Builder builder = new FragNavTransactionOptions.Builder();
        builder.setPopExitAnimation(R.anim.pop_exit);
        builder.setPopEnterAnimation(R.anim.pop_enter);
        FragNavTransactionOptions fragNavTransactionOptions = builder.build();
        mNavController.clearStack(fragNavTransactionOptions);
    }

    @Override
    public void onTabSelected(int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                mNavController.switchTab(TAB_HOME);
                break;

            case R.id.tab_wifi_transfer:
                mNavController.switchTab(TAB_WIFI_TRANSFER);
                break;

            case R.id.tab_settings:
                mNavController.switchTab(TAB_SETTINGS);
                break;
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (slidingLayout != null && slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mNavController.pushFragment(fragment);
    }

    @Override
    public void popFragment() {
        mNavController.popFragment();
    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment) {
        mNavController.showDialogFragment(dialogFragment);
    }

    @Override
    public void showBottomSheetDialogFragment(BottomSheetDialogFragment bottomSheetDialogFragment) {
        mNavController.showDialogFragment(bottomSheetDialogFragment);
    }

    @Override
    public void startActivity(Class clazz) {

    }

    private void initBottomBar(Bundle savedInstanceState) {

        fragments.add(homeFragment);
        fragments.add(wifiTransferFragment);
        fragments.add(settingsFragment);
        mNavController = new FragNavController(getSupportFragmentManager(), R.id.contentContainer);
        mNavController.setFragmentHideStrategy(FragNavController.HIDE);
        mNavController.setCreateEager(true);
        mNavController.setRootFragments(fragments);

        mNavController.initialize(TAB_HOME, savedInstanceState);

        for (int i = 0; i < bottomBarMain.getTabCount(); i++) {
            BottomBarTab tab = bottomBarMain.getTabAtPosition(i);
            tab.setGravity(Gravity.CENTER);
        }

    }


    private void initSlidingLayout() {

    }


    @Override
    public void DownSliding() {
        if (slidingLayout != null)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void hideSliding() {
        if (slidingLayout != null)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void showSliding() {
        if (slidingLayout != null)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);


    }

    @Override
    public SlidingUpPanelLayout.PanelState getSlideState() {
        return slidingLayout.getPanelState();
    }

    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(mNavController.getCurrentStack()).size() > 1) {
            FragNavTransactionOptions.Builder builder = new FragNavTransactionOptions.Builder();
            builder.allowStateLoss(true);
            FragNavTransactionOptions fragNavTransactionOptions = builder.build();
            mNavController.popFragment(fragNavTransactionOptions);
        } else {
            super.onBackPressed();
        }
    }

    //this func only use with media playback because my services run with action(Intent).
    public void runCommand(String command) {
        Intent serviceIntent = new Intent(Application.Context, MediaService.class);
        serviceIntent.setAction(command);
        Application.Context.startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
