package com.example.user.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.myapplication.R;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class LoginFragment extends Fragment {

    private View loginView;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loginView = inflater.inflate(R.layout.fragment_login, container, false);

        initializeView();

        return loginView;
    }

    private void initializeView(){
        navController =  Navigation.findNavController(getActivity(), R.id.nav_log_reg_fragment);
        Button to_register_btn = loginView.findViewById(R.id.switch_to_reg);
        to_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterSwitchClick();
            }
        });
    }

    private void onRegisterSwitchClick() {
        navController.popBackStack();
        navController.navigate(R.id.registerFragment);
    }

}
