package com.example.user.myapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.example.user.myapplication.fragment.ProfileFragment;
import com.example.user.myapplication.fragment.SecondFragment;
import com.example.user.myapplication.fragment.SimpleFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.util.RequestCode;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {


    final String LOG_TAG = "myLogs";

    private NavController navController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeNavigation();


        if(!hasAllPermissions())
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

    private void initializeNavigation(){
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavigationUI.setupWithNavController(navigationView, navController);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_view);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        initialToggle();
    }

    private void initialToggle(){
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        // outState.putString("IMEI", findViewById(R.id.imei_view).toString());
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


    private boolean hasAllPermissions(){
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    public boolean hasNeedPermissions(int per_ind){
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        res = checkCallingOrSelfPermission(permissions[per_ind]);
        return res == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,RequestCode.PERMISSION_REQUEST_CODE);
        }
    }

    public void requestNeedPerms(int per_ind){
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{permissions[per_ind]},RequestCode.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Map<String, Boolean> allowed = new HashMap<>();
        Boolean is_any_perms = false;
        Boolean val;
        switch (requestCode) {
            case RequestCode.PERMISSION_REQUEST_CODE: {
                for (int i = 0, len = grantResults.length; i < len; i++) {
                    allowed.put(permissions[i] ,(grantResults[i] == PackageManager.PERMISSION_GRANTED));
                }
                break;
            }
        }
        val = allowed.get(Manifest.permission.READ_PHONE_STATE);
        if (val != null && !val){
            // we will give warning to user that they haven't granted phone permission.
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                is_any_perms = true;
            }
            else {
                showNoPhonePermissionSnackbar();
            }
        }
        val = allowed.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (val != null && !val){
            // we will give warning to user that they haven't granted storage permission.
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                is_any_perms = true;
            }
            else {
                showNoStoragePermissionSnackbar();
            }

        }
        val = allowed.get(Manifest.permission.CAMERA);
        if (val != null && !val){
            // we will give warning to user that they haven't granted camera permission.
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                is_any_perms = true;
            }
            else {
                showNoCameraPermissionSnackbar();
            }
        }

        if (is_any_perms){
            requestPerms();
        }

    }


    public void showNoPhonePermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), getResources().getString(R.string.msg_ph_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.msg_open_ph_per),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    public void showNoCameraPermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), getResources().getString(R.string.msg_cam_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.msg_open_cam_per),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), getResources().getString(R.string.msg_store_per_no_grtd) , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.msg_open_store_per),
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




    public void onFragmentSecondNextClick(View view) {
        navController.navigate(R.id.profileFragment);
    }

    public void onFragmentSecondBackClick(View view) {}


    public void onFragmentProfileNextClick(View view) {
    }

    public void onFragmentProfileBackClick(View view) {
        navController.popBackStack();
    }


}
