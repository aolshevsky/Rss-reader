package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Presenter.Interface.ILoginPresenter;
import com.example.user.myapplication.View.ILoginView;
import com.example.user.myapplication.model.User;

public class LoginPresenter extends BasePresenter<ILoginView> implements ILoginPresenter {

    private static LoginPresenter instance = new LoginPresenter();


    public static LoginPresenter getInstance(){
        return instance;
    }

    @Override
    public int onLogin(String email, String password) {
       User user = new User(email, password);
       int loginCode = user.isValidLoginData();

       if(loginCode == -1)
           view.onLoginSuccess("Login Success");
       return loginCode;
    }
}
