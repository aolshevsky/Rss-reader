package com.example.user.myapplication.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface ILoginView {
    void onLoginSuccess(String message);
    void onLoginError(String message);
    String getUserLogin();
    String getUserPassword();
    void validUserEmail(String message);
    void validUserPassord(String message);
    void addListenerToDatabaseAuth(Task<AuthResult> authResultTask);
}
