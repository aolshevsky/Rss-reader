package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Presenter.Interface.IRegisterPresenter;
import com.example.user.myapplication.View.IRegisterView;
import com.example.user.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPresenter extends BasePresenter<IRegisterView> implements IRegisterPresenter {

    private static RegisterPresenter instance = new RegisterPresenter();


    public static RegisterPresenter getInstance(){
        return instance;
    }

    @Override
    public void registerUser(FirebaseAuth firebaseAuth) {
        User user = view.getUserRegInfo();
        int registerCode = user.isValidRegisterData();


        if (registerCode == 0){
            view.validUserName("Please enter your Name");
            return;
        }

        if (registerCode == 1){
            view.validUserSurname("Please enter your Surname");
            return;
        }

        if (registerCode == 2){
            view.validUserEmail("Please enter a valid email");
            return;
        }

        if (registerCode == 3){
            view.validUserPhone("Please enter your Phone number");
            return;
        }

        if (registerCode == 4){
            view.validUserPassord("Password must be at least 6 characters");
            return;
        }
        if (registerCode == 5){
            view.validUserConfPassord("Password not matching");
            return;
        }
        if(registerCode == -1) {
            view.onRegisterSuccess("Register Success");
        }

        view.addListenerToFirebaseAuth(
                firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()),
                user);
    }
}
