package com.example.user.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "onCreate");

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
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Map<String, Boolean> allowed = new HashMap<>();
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
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
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }


}
