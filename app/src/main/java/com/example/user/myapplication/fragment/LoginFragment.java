package com.example.user.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.myapplication.Presenter.ILoginPresenter;
import com.example.user.myapplication.Presenter.LoginPresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.ILoginView;
import com.example.user.myapplication.activity.LoginActivity;
import com.example.user.myapplication.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import es.dmoral.toasty.Toasty;


public class LoginFragment extends Fragment implements ILoginView {

    private View loginView;


    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseAuth firebaseAuth;

    private LoginPresenter loginPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loginView = inflater.inflate(R.layout.fragment_login, container, false);

        loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);
        initializeFirebase();

        initializeView();
        getActivity().setTitle("Login");

        return loginView;
    }

    @Override
    public void onLoginSuccess(String message) {
        Toasty.success(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginError(String message) {
        Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void initializeFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            getActivity().finish();
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }

    private void initializeView(){
        Button to_register_btn = loginView.findViewById(R.id.switch_to_reg);
        Button login_btn = loginView.findViewById(R.id.login_btn);
        editTextEmail = loginView.findViewById(R.id.login_email);
        editTextPassword = loginView.findViewById(R.id.login_password);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        to_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).onRegisterSwitchClick();
            }
        });
    }


    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        int loginCode = loginPresenter.onLogin(email, password);

        if (loginCode == 0){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (loginCode == 1){
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            getActivity().finish();
                            startActivity(new Intent(getContext(), MainActivity.class));

                        } else {
                            onLoginError("Could not register.." + task.getException());
                        }
                    }
                });
    }
}
