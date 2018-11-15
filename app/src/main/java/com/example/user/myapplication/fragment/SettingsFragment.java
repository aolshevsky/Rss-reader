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
import com.example.user.myapplication.utils.SharedPref;


public class SettingsFragment extends Fragment {

    private Switch theme_switch;
    private SharedPref sharedPref;
    private View settingsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPref = new SharedPref(getContext());
        initializeView();

        getActivity().setTitle("Settings");
        return settingsFragment;
    }

    private void initializeView(){
        theme_switch = settingsFragment.findViewById(R.id.theme_switch);

        if (sharedPref.loadNightModeState()){
            theme_switch.setChecked(true);
        }
        theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPref.setNightModeState(true);
                    restartApp();
                }
                else {
                    sharedPref.setNightModeState(false);
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
