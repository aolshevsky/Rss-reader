package com.example.user.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.user.myapplication.R;


public class SettingsFragment extends Fragment {

    private Switch theme_switch;
    private View settingsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeView();

        getActivity().setTitle("Settings");
        return settingsFragment;
    }

    private void initializeView(){
        theme_switch = settingsFragment.findViewById(R.id.theme_switch);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            theme_switch.setChecked(true);
        }
        theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });
    }

    private void restartApp(){
        Intent i = new Intent(getActivity().getApplicationContext(), getActivity().getClass());
        getActivity().startActivity(i);
        getActivity().finish();
    }

}
