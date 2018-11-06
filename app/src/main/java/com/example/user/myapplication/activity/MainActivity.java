package com.example.user.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.BuildConfig;
import com.example.user.myapplication.R;
import com.example.user.myapplication.fragment.ProfileFragment;
import com.example.user.myapplication.fragment.SecondFragment;
import com.example.user.myapplication.util.RequestCode;

import java.util.HashMap;
import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.Manifest.permission;

public class MainActivity extends AppCompatActivity {


    final String LOG_TAG = "myLogs";

    private NavController navController;

    private ProfileFragment profileFragment;
    private SecondFragment secondFragment;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        manager = getSupportFragmentManager();

        profileFragment = new ProfileFragment();
        secondFragment = new SecondFragment();

        if(!hasNeedPermissions())
            requestPerms();
/*
        if (savedInstanceState != null &&
                savedInstanceState.getString("IMEI") != null) {
            String saved_imei = savedInstanceState.getString("IMEI");
            String imei_str = "IMEI: ";
            TextView imei_txt = (TextView) findViewById(R.id.imei_view);
            imei_txt.setText(imei_str.concat(saved_imei));
        }
        Log.d(LOG_TAG, "onCreate");
*/
/*
        TextView ver_view = findViewById(R.id.version_name);
        String ver_name = getResources().getString(R.string.app_ver) + BuildConfig.VERSION_NAME;
        ver_view.setText(ver_name);
        showPhoneState();
        Button btn = (Button) findViewById(R.id.start_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNeedPermissions()) {
                    showPhoneState();
                }
                else {
                    requestPerms();
                }
            }
        });
*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume ");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("IMEI", findViewById(R.id.imei_view).toString());
        Log.d(LOG_TAG, "onSaveInstanceState");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    private void showPhoneState() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "IMEI: ";
        TextView imei_txt = (TextView) findViewById(R.id.imei_view);
        try {
            imei += tm.getDeviceId();
            imei_txt.setText(imei);
        } catch (SecurityException e) {
            imei_txt.setText(getResources().getString(R.string.msg_no_per));
        }
    }


    private boolean hasNeedPermissions(){
        int res = 0;
        String[] permissions = new String[]{permission.READ_PHONE_STATE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{permission.READ_PHONE_STATE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,RequestCode.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Map<String, Boolean> allowed = new HashMap<>();
        switch (requestCode) {
            case RequestCode.PERMISSION_REQUEST_CODE: {
                for (int i = 0, len = grantResults.length; i < len; i++) {
                    allowed.put(permissions[i] ,(grantResults[i] == PackageManager.PERMISSION_GRANTED));
                }
                break;
            }
        }
        if (allowed.get(permission.READ_PHONE_STATE)){
            showPhoneState();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (shouldShowRequestPermissionRationale(permission.READ_PHONE_STATE)) {
                requestPerms();
                Toast.makeText(this, getResources().getString(R.string.msg_ph_per_den), Toast.LENGTH_SHORT).show();
            }
            else {
                showNoPhonePermissionSnackbar();
            }
        }

    }


    public void showNoPhonePermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), getResources().getString(R.string.msg_ph_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.msg_open_per),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, RequestCode.PERMISSION_REQUEST_CODE);
    }

    public void onClickGetIMEI(View view){
        if (hasNeedPermissions()) {
            showPhoneState();
        }
        else {
            requestPerms();
        }
    }


    public void onFragmentSecondNextClick(View view) {
        navController.navigate(R.id.profileFragment);
    }

    public void onFragmentSecondBackClick(View view) {}


    public void onFragmentProfileNextClick(View view) {
    }

    public void onFragmentProfileBackClick(View view) {
        navController.popBackStack();
    }

    /*
    public void onClickFragment(View view){
        transaction = manager.beginTransaction();

        switch (view.getId()){
            case R.id.btnAdd:
                if (manager.findFragmentByTag(ProfileFragment.TAG) == null) {
                    transaction.add(R.id.container, profileFragment, ProfileFragment.TAG);
                }
                break;
            case R.id.btnDelete:
                if (manager.findFragmentByTag(ProfileFragment.TAG) != null) {
                    transaction.remove(profileFragment);
                }
                if (manager.findFragmentByTag(SecondFragment.TAG) != null) {
                    transaction.remove(secondFragment);
                }
                break;
            case R.id.btnReplace:
                if (manager.findFragmentByTag(ProfileFragment.TAG) != null) {
                    transaction.replace(R.id.container, secondFragment, SecondFragment.TAG);
                }
                if (manager.findFragmentByTag(SecondFragment.TAG) != null) {
                    transaction.replace(R.id.container, profileFragment, ProfileFragment.TAG);
                }
                break;
        }

        transaction.commit();
    }
    */



}
