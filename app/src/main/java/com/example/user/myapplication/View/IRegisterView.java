package com.example.user.myapplication.View;

import com.example.user.myapplication.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface IRegisterView {
    void onRegisterSuccess(String message);
    void onRegisterError(String message);
    User getUserRegInfo();
    void validUserName(String message);
    void validUserSurname(String message);
    void validUserEmail(String message);
    void validUserPhone(String message);
    void validUserPassword(String message);
    void validUserConfPassword(String message);
    void addListenerToFirebaseAuth(Task<AuthResult> authResultTask, User user);
}
