package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Presenter.Interface.IRegisterPresenter;
import com.example.user.myapplication.View.IRegisterView;
import com.example.user.myapplication.model.User;

public class RegisterPresenter extends BasePresenter<IRegisterView> implements IRegisterPresenter {

    private static RegisterPresenter instance = new RegisterPresenter();


    public static RegisterPresenter getInstance(){
        return instance;
    }

    @Override
    public int onRegister(String name, String surname, String phone_number, String email, String password, String con_password) {
        User user = new User(name, surname, phone_number, email, password, con_password);
        int registerCode = user.isValidRegisterData();

        if(registerCode == -1)
            view.onRegisterSuccess("Register Success");
        return registerCode;
    }
}
