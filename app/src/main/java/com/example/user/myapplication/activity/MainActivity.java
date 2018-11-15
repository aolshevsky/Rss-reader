package com.example.user.myapplication.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.user.myapplication.R;
import com.example.user.myapplication.utils.DatabaseHelper;
import com.example.user.myapplication.utils.DeepLinksHelper;
import com.example.user.myapplication.utils.PermissionsHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {


    final String LOG_TAG = "myLogs";

    public NavController navController;
    private DrawerLayout drawerLayout;

    PermissionsHelper permissionsHelper;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeNavigation();

        databaseHelper = new DatabaseHelper();
        permissionsHelper = new PermissionsHelper();

        DeepLinksHelper.uriNavigate(navController, this);
        // adb shell am start -W -a android.intent.action.VIEW -d "sdapp://by.myapp/page"

        if(!permissionsHelper.hasAllPermissions(this))
            permissionsHelper.requestAllPerms(this);

    }

    private void initializeNavigation(){
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.nav_view);

        NavigationUI.setupWithNavController(navigationView, navController);
        drawerLayout = findViewById(R.id.activity_view);

        View headerview = navigationView.getHeaderView(0);
        LinearLayout profile_click_place = headerview.findViewById(R.id.profile_click_place);
        profile_click_place.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onProfileClick(v);
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
    protected void onResume() {
        super.onResume();
        databaseHelper.loadUserInformationMenu(this, "local_user");
        Log.d(LOG_TAG, "onStart");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void onProfileClick(View view) {
        navController.popBackStack();
        navController.navigate(R.id.profileFragment);
        drawerLayout.closeDrawers();
    }
}
