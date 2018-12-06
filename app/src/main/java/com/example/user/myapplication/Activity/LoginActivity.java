package com.example.user.myapplication.Activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.user.myapplication.Fragment.Interface.ILoginListener;
import com.example.user.myapplication.Fragment.Interface.IRegisterListener;
import com.example.user.myapplication.R;

import androidx.annotation.NonNull;
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
        dispatchTouchEvent();
        navController.popBackStack();
        navController.navigate(R.id.registerFragment);
    }

    @Override
    public void onLoginSwitchClick() {
        dispatchTouchEvent();
        navController.popBackStack();
        navController.navigate(R.id.loginFragment);
    }

    private void dispatchTouchEvent() {
        View v = getCurrentFocus();
        if (v instanceof EditText) {
            v.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
