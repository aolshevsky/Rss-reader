package com.example.user.myapplication.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class PermissionsHelper {

    private static PermissionsHelper instance = new PermissionsHelper();

    public static PermissionsHelper getInstance(){
        return instance;
    }

    public boolean hasAllPermissions(Activity activity){
        int res;
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        for (String perms : permissions){
            res = activity.checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }
    public boolean hasNeedPermissions(Activity activity, int per_ind){
        int res;
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        res = activity.checkCallingOrSelfPermission(permissions[per_ind]);
        return res == PackageManager.PERMISSION_GRANTED;
    }


    public void requestAllPerms(Activity activity){
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(permissions,Constants.PERMISSION_REQUEST_CODE);
        }
    }
    public void requestNeedPerms(Activity activity, int per_ind){
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(new String[]{permissions[per_ind]},Constants.PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Map<String, Boolean> allowed = new HashMap<>();
        Boolean is_any_perms = false;
        Boolean val;
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CODE: {
                for (int i = 0, len = grantResults.length; i < len; i++) {
                    allowed.put(permissions[i] ,(grantResults[i] == PackageManager.PERMISSION_GRANTED));
                }
                break;
            }
        }
        val = allowed.get(Manifest.permission.READ_PHONE_STATE);
        if (val != null && !val){
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                is_any_perms = true;
            }
            else {
                showNoPhonePermissionSnackbar(activity);
            }
        }
        val = allowed.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (val != null && !val){
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                is_any_perms = true;
            }
            else {
                showNoStoragePermissionSnackbar(activity);
            }

        }
        val = allowed.get(Manifest.permission.CAMERA);
        if (val != null && !val){
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                is_any_perms = true;
            }
            else {
                showNoCameraPermissionSnackbar(activity);
            }
        }

        if (is_any_perms){
            requestAllPerms(activity);
        }
    }


    private void showNoPhonePermissionSnackbar(final Activity activity) {
        Snackbar.make(activity.findViewById(R.id.activity_view), activity.getResources().getString(R.string.msg_ph_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings(activity);

                        Toast.makeText(activity.getApplicationContext(),
                                activity.getResources().getString(R.string.msg_open_ph_per),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }
    private void showNoCameraPermissionSnackbar(final Activity activity) {
        Snackbar.make(activity.findViewById(R.id.activity_view), activity.getResources().getString(R.string.msg_cam_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings(activity);

                        Toast.makeText(activity.getApplicationContext(),
                                activity.getResources().getString(R.string.msg_open_cam_per),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }
    private void showNoStoragePermissionSnackbar(final Activity activity) {
        Snackbar.make(activity.findViewById(R.id.activity_view), activity.getResources().getString(R.string.msg_store_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings(activity);

                        Toast.makeText(activity.getApplicationContext(),
                                activity.getResources().getString(R.string.msg_open_store_per),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    private void openApplicationSettings(Activity activity) {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(appSettingsIntent, Constants.PERMISSION_REQUEST_CODE);
    }
}
