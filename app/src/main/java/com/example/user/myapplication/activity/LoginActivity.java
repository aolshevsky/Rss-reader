package com.example.user.myapplication.activity;

import android.os.Bundle;

import com.example.user.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class LoginActivity extends AppCompatActivity {


    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();


    }


    private void initializeView() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.nav_log_reg_fragment);
    }

    public void onRegisterSwitchClick() {
        navController.popBackStack();
        navController.navigate(R.id.registerFragment);
    }

    public void onLoginSwitchClick() {
        navController.popBackStack();
        navController.navigate(R.id.loginFragment);
    }
}
