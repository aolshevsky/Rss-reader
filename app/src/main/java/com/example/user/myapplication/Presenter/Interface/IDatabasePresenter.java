package com.example.user.myapplication.Presenter.Interface;

import com.example.user.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public interface IDatabasePresenter {
    DatabaseReference getDatabaseUsers();
    FirebaseAuth getFirebaseAuth();
    User getCurrentUser();
    void saveUserToDatabase(User userInfo);
    void uploadImageToFirebaseStorage(String img_path);
    void downloadFromFirebaseStorage();
    void loadUserInformationMenu();
    void saveUser(String name, String surname, String phone_number, String img_path);
    int validEditData(String name, String surname, String phone_number);
}
