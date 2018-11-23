package com.example.user.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.myapplication.Presenter.DatabasePresenter;
import com.example.user.myapplication.Presenter.RegisterPresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IDatabaseView;
import com.example.user.myapplication.View.IRegisterView;
import com.example.user.myapplication.activity.LoginActivity;
import com.example.user.myapplication.activity.MainActivity;
import com.example.user.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;


public class RegisterFragment extends Fragment implements IRegisterView, IDatabaseView {

    private View registerView;

    private EditText editTextEmail, editTextName, editTextSurname,
            editTextPhone, editTextPassword, editTextConfirmPassword;

    private FirebaseAuth firebaseAuth;
    private DatabasePresenter databasePresenter;

    private RegisterPresenter registerPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerView = inflater.inflate(R.layout.fragment_register, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        databasePresenter = new DatabasePresenter();
        databasePresenter.attachView(this);
        registerPresenter = RegisterPresenter.getInstance();
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
                registerPresenter.registerUser(firebaseAuth);
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

    @Override
    public User getUserRegInfo() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String con_password = editTextConfirmPassword.getText().toString().trim();

        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String phone_number = editTextPhone.getText().toString();

        return new User(name, surname, phone_number, email, password, con_password);
    }


    @Override
    public void addListenerToFirebaseAuth(Task<AuthResult> authResultTask, final User user) {
        authResultTask.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User userInfo = new User(user.getName(), user.getSurname(), user.getEmail(), user.getPhone_number(), "");
                    databasePresenter.saveUserToDatabase(userInfo);
                    getActivity().finish();
                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    onRegisterError("Could not register.." + task.getException());
                }
            }
        });
    }

    @Override
    public void validUserName(String message) {
        editTextName.setError(message);
        editTextName.requestFocus();
    }

    @Override
    public void validUserSurname(String message) {
        editTextSurname.setError(message);
        editTextSurname.requestFocus();
    }

    @Override
    public void validUserEmail(String message) {
        editTextEmail.setError(message);
        editTextEmail.requestFocus();
    }

    @Override
    public void validUserPhone(String message) {
        editTextPhone.setError(message);
        editTextPhone.requestFocus();
    }

    @Override
    public void validUserPassord(String message) {
        editTextPassword.setError(message);
        editTextPassword.requestFocus();
    }

    @Override
    public void validUserConfPassord(String message) {
        editTextConfirmPassword.setError(message);
        editTextConfirmPassword.requestFocus();
    }

    //<editor-fold desc="Empty implement methods">
    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }
    @Override
    public void onSuccessMessage(String message) {

    }
    @Override
    public void onErrorMessage(String message) {

    }
    @Override
    public void setProfileImg(Bitmap bmp) {

    }
    @Override
    public void saveUser() {

    }
    @Override
    public void setUserInfo(User userInfo) {

    }
    //</editor-fold>
}

