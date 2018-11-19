package com.example.user.myapplication.View;

import android.app.ProgressDialog;
import android.graphics.Bitmap;

import com.example.user.myapplication.model.User;

public interface IDatabaseView {
    ProgressDialog getProgressDialog();
    void onSuccessMessage(String message);
    void onErrorMessage(String message);
    void setProfileImg(Bitmap bmp);
    void saveUser();
    void setUserInfo(User userInfo);
}
