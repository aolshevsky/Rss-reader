package com.example.user.myapplication.Presenter.Interface;

import android.graphics.Bitmap;

public interface IImagePresenter {
    void showPictureDialog();
    String saveImage(Bitmap myBitmap);
}
