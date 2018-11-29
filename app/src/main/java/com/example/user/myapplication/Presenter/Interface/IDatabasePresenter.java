package com.example.user.myapplication.Presenter.Interface;

import com.example.user.myapplication.Model.User;

public interface IDatabasePresenter {
    void saveUserToDatabase(User userInfo);
    void uploadImageToFirebaseStorage(String img_path);
    void downloadFromFirebaseStorage();
    void loadUserInformationMenu();
    void saveUser();
    int validEditData(String name, String surname, String phone_number);
}
