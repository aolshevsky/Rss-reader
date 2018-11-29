package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Manager.DatabaseManager;
import com.example.user.myapplication.Presenter.Interface.ILoginPresenter;
import com.example.user.myapplication.View.ILoginView;
import com.example.user.myapplication.Model.User;
import com.example.user.myapplication.Utils.Constants;

public class LoginPresenter extends BasePresenter<ILoginView> implements ILoginPresenter {

    private static LoginPresenter instance = new LoginPresenter();
    private DatabaseManager databaseManager;

    public static LoginPresenter getInstance(){
        return instance;
    }

    private LoginPresenter(){
        databaseManager = DatabaseManager.getInstance();
    }

    private int onLogin(String email, String password) {
       User user = new User(email, password);
       return user.isValidLoginData();
    }


    @Override
    public void userLogin() {
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

        view.addListenerToFirebaseAuth(databaseManager.getAuth().signInWithEmailAndPassword(email, password));

    }


}
