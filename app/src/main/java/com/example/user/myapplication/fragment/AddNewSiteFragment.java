package com.example.user.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.activity.MainActivity;
import com.example.user.myapplication.utils.Parser;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;


public class AddNewSiteFragment extends Fragment {

    private View view;
    private EditText editTextUrl;


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

        editTextUrl = view.findViewById(R.id.new_site_txtUrl);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editTextUrl.getText().toString();

                int valid_val = Parser.ValidUrl(url);

                if(valid_val == 0){
                    //new loadRSSFeed().execute(url);
                } else if(valid_val == 1){
                    onErrorMessage("Please enter a valid url");
                } else {
                    onErrorMessage("Please enter website url");
                }

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


    public void onErrorMessage(String message) {
        Toasty.error(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
