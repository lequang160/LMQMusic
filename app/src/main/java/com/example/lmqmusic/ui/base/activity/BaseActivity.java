package com.example.lmqmusic.ui.base.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements MvpView {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        initViews(savedInstanceState);

        initControls();


    }

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void initViews(@Nullable Bundle savedInstanceState);

    protected abstract void initControls();


    public AppCompatActivity getActivity() {
        return this;
    }

    public void pushIntent(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

        }

    }

    public void pushIntentForResult(Intent intent, int requestCode) {
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void goBackHome() {

        finish();
    }
}
