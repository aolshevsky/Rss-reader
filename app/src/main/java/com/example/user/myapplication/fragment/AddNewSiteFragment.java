package com.example.user.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.myapplication.R;
import com.example.user.myapplication.activity.MainActivity;

import androidx.fragment.app.Fragment;


public class AddNewSiteFragment extends Fragment {

    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_site, container, false);
        initializeView();

        return view;
    }

    private void initializeView(){
        Button submit = view.findViewById(R.id.new_site_btnSubmit);
        Button cancel = view.findViewById(R.id.new_site_btnCancel);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onSubmitSiteClick();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onSubmitSiteClick();
            }
        });

    }

}
