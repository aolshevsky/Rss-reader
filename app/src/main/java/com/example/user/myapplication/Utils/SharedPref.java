package com.example.user.myapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private SharedPreferences sharedPreferences;

    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    public Boolean loadNightModeState(){
        Boolean state = sharedPreferences.getBoolean("NightMode", false);
        return state;
    }

}
