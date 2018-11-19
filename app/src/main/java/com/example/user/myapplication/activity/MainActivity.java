package com.example.user.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.user.myapplication.Presenter.DeepLinksPresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IDeepLinksView;
import com.example.user.myapplication.utils.DatabaseHelper;
import com.example.user.myapplication.utils.PermissionsHelper;
import com.example.user.myapplication.utils.SharedPref;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity  implements IDeepLinksView {


    final String LOG_TAG = "myLogs";

    public NavController navController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    PermissionsHelper permissionsHelper;

    private DatabaseHelper databaseHelper;
    private SharedPref sharedPref;
    private DeepLinksPresenter deepLinksPresenter;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeNavigation();

        databaseHelper = DatabaseHelper.getInstance();
        permissionsHelper = PermissionsHelper.getInstance();
        sharedPref = new SharedPref(this);
        initializeTheme();
        initializeFirebase();

        deepLinksPresenter = DeepLinksPresenter.getInstance();
        deepLinksPresenter.attachView(this);
        deepLinksPresenter.uriNavigate();
        // adb shell am start -W -a android.intent.action.VIEW -d "sdapp://by.myapp/page"

        if(!permissionsHelper.hasAllPermissions(this))
            permissionsHelper.requestAllPerms(this);

    }

    private void initializeFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d(LOG_TAG, "LogoutFragment");
        if(firebaseAuth.getCurrentUser() == null){
            Log.d(LOG_TAG, "Logout1");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void initializeTheme(){
        if (sharedPref.loadNightModeState()){
            setTheme(R.style.darktheme);
        }
    }

    private void initializeNavigation(){
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.logout_item:
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        return true;
                    case R.id.settingsFragment:
                        navController.popBackStack();
                        navController.navigate(R.id.settingsFragment);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.homeFragment:
                        navController.popBackStack();
                        navController.navigate(R.id.homeFragment);
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });


        drawerLayout = findViewById(R.id.activity_view);

        View headerview = navigationView.getHeaderView(0);
        LinearLayout profile_click_place = headerview.findViewById(R.id.profile_click_place);
        initialToggle();
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
            case R.id.logout_item:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
        }
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialToggle(){
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseHelper.loadUserInformationMenu(this);
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

    @Override
    public Uri getUri() {
        return getIntent().getData();
    }

    @Override
    public void navigateTo(int fragment_id) {
        navController.navigate(fragment_id);
    }
}
