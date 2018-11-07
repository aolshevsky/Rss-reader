package com.example.user.myapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.myapplication.BuildConfig;
import com.example.user.myapplication.R;

public class SecondFragment extends Fragment {

    private View phoneStateView;

    public static final String TAG = "SecondFragmentTag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        if (savedInstanceState != null &&
                savedInstanceState.getString("IMEI") != null) {
            String saved_imei = savedInstanceState.getString("IMEI");
            String imei_str = "IMEI: ";
            TextView imei_txt = (TextView) getView().findViewById(R.id.imei_view);
            imei_txt.setText(imei_str.concat(saved_imei));
        }
         */
        phoneStateView = inflater.inflate(R.layout.fragment_second, container, false);

        return phoneStateView;
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView ver_view = phoneStateView.findViewById(R.id.version_name);
        String ver_name = getResources().getString(R.string.app_ver) + BuildConfig.VERSION_NAME;
        ver_view.setText(ver_name);

        if (hasNeedPermissions()) {
            showPhoneState();
        }

    }

    private void showPhoneState() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "IMEI: ";
        TextView imei_txt = (TextView) phoneStateView.findViewById(R.id.imei_view);
        try {
            imei += tm.getDeviceId();
            imei_txt.setText(imei);
        } catch (SecurityException e) {
            imei_txt.setText(getResources().getString(R.string.msg_no_per));
        }
    }


    private boolean hasNeedPermissions(){
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};

        for (String perms : permissions){
            res = getActivity().checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }
}
