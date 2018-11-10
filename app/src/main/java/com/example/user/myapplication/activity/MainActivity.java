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

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


    private String user_id = "local_user";


    private DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeNavigation();

        initializeDatabase();

        if(!hasAllPermissions())
            requestAllPerms();

    }
    // упорядочить код,вынести все в отдельные классы кнопка имя
    private void initializeNavigation(){
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavigationUI.setupWithNavController(navigationView, navController);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_view);

        View headerview = navigationView.getHeaderView(0);
        LinearLayout profile_click_place = (LinearLayout) headerview.findViewById(R.id.profile_click_place);
        profile_click_place.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onEditClick(v);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.about_toolbar_button:
                navController.navigate(R.id.aboutFragment);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadUserInformation();
        Log.d(LOG_TAG, "onStart");
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
        navController.popBackStack();
        navController.navigate(R.id.homeFragment);
        drawerLayout.closeDrawers();
    }

    public void onEditClick(View view) {
        navController.popBackStack();
        navController.navigate(R.id.profileFragment);
        drawerLayout.closeDrawers();
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
