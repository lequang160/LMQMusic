package com.example.lmqmusic.ui.base.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.lmqmusic.R;
import com.example.lmqmusic.ui.base.activity.MvpView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public abstract class PermissionFragmentMVP<P extends FragmentPresenterMVP, V extends MvpView> extends FragmentMVP<P, V>
        implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private static final int REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE = 12;

    String[] mPermissionsReadWriteStrograge = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE)
    protected void requiresPermissionReadWriteExternalStorage() {
        if (getActivity() == null) return;

        if (EasyPermissions.hasPermissions(getActivity(), mPermissionsReadWriteStrograge)) {
            permissionGranted();
        } else {
            // Do not have mPermissionsReadWriteStrograge, request them now
            EasyPermissions.requestPermissions(
                    new PermissionRequest
                            .Builder(this, REQUEST_PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE, mPermissionsReadWriteStrograge)
                            .setRationale(R.string.we_need_permissions_to_download)
                            .setPositiveButtonText(R.string.ok)
                            .setNegativeButtonText(R.string.cancel)
                            .build());
        }
    }

    protected abstract void permissionGranted();

    protected void permissionDenied() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

        permissionDenied();

        Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
//        mFragNav.popFragment();
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
//        mFragNav.popFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE && getContext() != null) {
            if (EasyPermissions.hasPermissions(getContext(), mPermissionsReadWriteStrograge)) {
                permissionGranted();
            } else {
                Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                permissionDenied();
//                mFragNav.popFragment();
            }
        }
    }
}
