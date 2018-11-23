package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Presenter.Interface.ILoginPresenter;
import com.example.user.myapplication.View.ILoginView;
import com.example.user.myapplication.model.User;
import com.example.user.myapplication.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter extends BasePresenter<ILoginView> implements ILoginPresenter {

    private static LoginPresenter instance = new LoginPresenter();


    public static LoginPresenter getInstance(){
        return instance;
    }

    private int onLogin(String email, String password) {
       User user = new User(email, password);
       return user.isValidLoginData();
    }


    @Override
    public void userLogin(FirebaseAuth firebaseAuth) {
        String email = view.getUserLogin();
        String password = view.getUserPassword();

        int loginCode = onLogin(email, password);


        if (loginCode == Constants.VALID_EMAIL){
            view.validUserEmail("Please enter a valid email");
            return;
        }
        if (loginCode == Constants.VALID_PASSWORD){
            view.validUserPassord("Password must be at least 6 characters");
            return;
        }
        if(loginCode == Constants.LOGIN_SUCCESS) {
            view.onLoginSuccess("Login Success");
        }

        view.addListenerToFirebaseAuth(firebaseAuth.signInWithEmailAndPassword(email, password));

    }


}
