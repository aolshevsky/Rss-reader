package com.example.user.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.myapplication.BuildConfig;
import com.example.user.myapplication.R;
import com.example.user.myapplication.activity.MainActivity;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    private View phoneStateView;

    public static final String TAG = "SecondFragmentTag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null &&
                savedInstanceState.getString("IMEI") != null) {
            String saved_imei = savedInstanceState.getString("IMEI");
            TextView imei_txt = (TextView) getView().findViewById(R.id.imei_view);
            imei_txt.setText(String.format("IMEI: %s",saved_imei));
        }

        phoneStateView = inflater.inflate(R.layout.fragment_about, container, false);

        getActivity().setTitle("About");

        return phoneStateView;
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView ver_view = phoneStateView.findViewById(R.id.version_name);
        String ver_name = getResources().getString(R.string.app_ver) + BuildConfig.VERSION_NAME;
        ver_view.setText(ver_name);

        Button get_imei_btn = phoneStateView.findViewById(R.id.start_btn);
        get_imei_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGetIMEI();
            }
        });

        if (((MainActivity)getActivity()).hasNeedPermissions(0)) {
            showPhoneState();
        }

    }

    private void showPhoneState() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        TextView imei_txt = (TextView) phoneStateView.findViewById(R.id.imei_view);
        try {
            imei_txt.setText(String.format("IMEI: %s", tm.getDeviceId()));
        } catch (SecurityException e) {
            imei_txt.setText(getResources().getString(R.string.msg_no_per));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("IMEI", phoneStateView.findViewById(R.id.imei_view).toString());
        Log.d("myLog", "onSaveInstanceState");
    }

    private void onClickGetIMEI(){
        if (((MainActivity)getActivity()).hasNeedPermissions(0)) {
            showPhoneState();
        }
        else {
            ((MainActivity)getActivity()).requestNeedPerms(0);
        }
    }

}
