package com.example.user.myapplication.Activity;

import android.os.Bundle;

import com.example.user.myapplication.Fragment.Interface.ILoginListener;
import com.example.user.myapplication.Fragment.Interface.IRegisterListener;
import com.example.user.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class LoginActivity extends AppCompatActivity implements ILoginListener, IRegisterListener {


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

    @Override
    public void onRegisterSwitchClick() {
        navController.popBackStack();
        navController.navigate(R.id.registerFragment);
    }

    @Override
    public void onLoginSwitchClick() {
        navController.popBackStack();
        navController.navigate(R.id.loginFragment);
    }
}
