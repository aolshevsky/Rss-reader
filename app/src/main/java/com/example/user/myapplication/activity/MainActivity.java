package com.example.user.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.example.user.myapplication.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.util.RequestCode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {


    final String LOG_TAG = "myLogs";

    public NavController navController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;


    private String user_id = "local_user";


    private DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeNavigation();

        initializeDatabase();

        if(!hasAllPermissions())
            requestAllPerms();
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

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
        loadUserInformation();
        Log.d(LOG_TAG, "onStart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }


    private  void initializeDatabase(){
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    }

    private void loadUserInformation(){

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(user_id).getValue(User.class);
                if(user != null) {
                    TextView textViewFullName = (TextView) findViewById(R.id.full_name_txt);
                    TextView textViewEmail = (TextView) findViewById(R.id.email_txt);
                    textViewFullName.setText(String.format("%s %s", user.getName(), user.getSurname()));
                    textViewEmail.setText(user.getEmail());

                    File imgFile = new  File(user.getImg_path());

                    if(imgFile.exists()){
                        Bitmap iconBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ImageView profileImgView = (ImageView) findViewById(R.id.profileImgView);
                        profileImgView.setImageBitmap(iconBitmap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


    private void requestAllPerms(){
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
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                is_any_perms = true;
            }
            else {
                showNoPhonePermissionSnackbar();
            }
        }
        val = allowed.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (val != null && !val){
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                is_any_perms = true;
            }
            else {
                showNoStoragePermissionSnackbar();
            }

        }
        val = allowed.get(Manifest.permission.CAMERA);
        if (val != null && !val){
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                is_any_perms = true;
            }
            else {
                showNoCameraPermissionSnackbar();
            }
        }

        if (is_any_perms){
            requestAllPerms();
        }

    }

    public void onFragmentProfileCancelClick(View view) {
        navController.navigate(R.id.ho);
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

}
