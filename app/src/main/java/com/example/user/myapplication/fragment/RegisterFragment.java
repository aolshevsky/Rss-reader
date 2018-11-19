package com.example.user.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.myapplication.Presenter.RegisterPresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IRegisterView;
import com.example.user.myapplication.activity.LoginActivity;
import com.example.user.myapplication.activity.MainActivity;
import com.example.user.myapplication.model.User;
import com.example.user.myapplication.utils.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;


public class RegisterFragment extends Fragment implements IRegisterView {

    private View registerView;

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseHelper databaseHelper;

    private RegisterPresenter registerPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerView = inflater.inflate(R.layout.fragment_register, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseHelper = DatabaseHelper.getInstance();
        registerPresenter = new RegisterPresenter();
        registerPresenter.attachView(this);

        initializeView();
        getActivity().setTitle("Register");

        return registerView;
    }

    private void initializeView(){
        Button to_login_btn = registerView.findViewById(R.id.switch_to_login);
        Button register_btn = registerView.findViewById(R.id.register_btn);
        editTextName = registerView.findViewById(R.id.reg_name_txtEdit);
        editTextSurname = registerView.findViewById(R.id.reg_surname_txtEdit);
        editTextEmail = registerView.findViewById(R.id.reg_email_txtEdit);
        editTextPhone = registerView.findViewById(R.id.reg_phone_txtEdit);
        editTextPassword = registerView.findViewById(R.id.reg_password);
        editTextConfirmPassword = registerView.findViewById(R.id.reg_confirm_password);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).onLoginSwitchClick();
            }
        });
    }

    @Override
    public void onRegisterSuccess(String message) {
        Toasty.success(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterError(String message) {
        Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String con_password = editTextConfirmPassword.getText().toString().trim();

        final String name = editTextName.getText().toString();
        final String surname = editTextSurname.getText().toString();
        final String phone_number = editTextPhone.getText().toString();

        int registerCode = registerPresenter.onRegister(name, surname, phone_number, email, password, con_password);
        if (registerCode == 0){
            editTextName.setError("Please enter your Name");
            editTextName.requestFocus();
            return;
        }

        if (registerCode == 1){
            editTextSurname.setError("Please enter your Surname");
            editTextSurname.requestFocus();
            return;
        }

        if (registerCode == 2){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (registerCode == 3){
            editTextPhone.setError("Please enter your Phone number");
            editTextPhone.requestFocus();
            return;
        }

        if (registerCode == 4){
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }
        if (registerCode == 5){
            editTextConfirmPassword.setError("Password not matching");
            editTextConfirmPassword.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User userInfo = new User(name, surname,email, phone_number, "");
                    databaseHelper.SaveUserToDatabase(userInfo);
                    getActivity().finish();
                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    onRegisterError("Could not register.." + task.getException());
                }
            }
        });


    }

}

