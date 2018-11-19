package com.example.user.myapplication.View;

import androidx.appcompat.app.AlertDialog;

public interface IImageView {
    AlertDialog.Builder createPictureDialog();
    boolean hasNeedPermissions(int per_ind);
    void requestNeedPerms(int per_ind);
    void choosePhotoFromGallary();
    void takePhotoFromCamera();
}
