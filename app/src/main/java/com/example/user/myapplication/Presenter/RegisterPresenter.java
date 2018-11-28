package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Presenter.Interface.IRegisterPresenter;
import com.example.user.myapplication.View.IRegisterView;
import com.example.user.myapplication.model.User;
import com.example.user.myapplication.utils.Constants;
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


        if (registerCode == Constants.EMPTY_NAME){
            view.validUserName("Please enter your Name");
            return;
        }

        if (registerCode == Constants.EMPTY_SURNAME){
            view.validUserSurname("Please enter your Surname");
            return;
        }

        if (registerCode == Constants.VALID_EMAIL){
            view.validUserEmail("Please enter a valid email");
            return;
        }

        if (registerCode == Constants.EMPTY_PHONE_NUMBER){
            view.validUserPhone("Please enter your Phone number");
            return;
        }

        if (registerCode == Constants.VALID_PASSWORD){
            view.validUserPassword("Password must be at least 6 characters");
            return;
        }
        if (registerCode == Constants.VALID_CONF_PASSWORD){
            view.validUserConfPassword("Password not matching");
            return;
        }

        view.addListenerToFirebaseAuth(
                firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()),
                user);
    }
}
