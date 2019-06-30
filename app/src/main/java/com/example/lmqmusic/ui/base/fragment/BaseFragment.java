package com.example.lmqmusic.ui.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lmqmusic.ui.main.MainActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public abstract class BaseFragment extends DialogFragment {

    protected FragNavListener mFragNav;

    protected MainActivity mActivity;

    protected  SlidingUpPanelLayoutListener mSlidingup;

    public FragNavListener getFragNav() {
        return mFragNav;
    }

    public SlidingUpPanelLayoutListener getSliding() {
        return mSlidingup;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView != null) {
            rootView.setClickable(true);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mFragNav = (FragNavListener) context;
            mSlidingup = (SlidingUpPanelLayoutListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragNavListener");
        }

        mActivity = (MainActivity) context;

    }

    public interface FragNavListener {
        void pushFragment(Fragment fragment);

        void popFragment();

        void showDialogFragment(DialogFragment dialogFragment);

        void showBottomSheetDialogFragment(BottomSheetDialogFragment bottomSheetDialogFragment);

        void startActivity(Class clazz);
    }

    public interface SlidingUpPanelLayoutListener{
        void DownSliding();
        void hideSliding();
        void showSliding();
        SlidingUpPanelLayout.PanelState getSlideState();
    }
}
