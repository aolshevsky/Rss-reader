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
}
