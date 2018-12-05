package com.example.user.myapplication.Model;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.user.myapplication.Utils.Constants;

public class User {

    private String name;
    private String surname;
    private String email;
    private String phone_number;
    private String img_path;
    private String password;
    private String confirm_password;

    public User(){}

    public User(String name, String surname, String email, String phone_number, String img_path) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
        this.img_path = img_path;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String name, String surname, String email, String phone_number, String password, String confirm_password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public User(String name, String surname, String phone_number){
        this.name = name;
        this.surname = surname;
        this.phone_number = phone_number;
    }

    public User(String name, String surname, String phone_number, String img_path){
        this.name = name;
        this.surname = surname;
        this.phone_number = phone_number;
        this.img_path = img_path;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int isValidLoginData(){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Constants.VALID_EMAIL;
        if(password == null || password.length() < Constants.MIN_PASSWORD_LENGTH)
            return Constants.VALID_PASSWORD;
        return Constants.LOGIN_SUCCESS;

    }

    public int isValidRegisterData(){
        if(TextUtils.isEmpty(name))
            return Constants.EMPTY_NAME;
        if(TextUtils.isEmpty(surname))
            return Constants.EMPTY_SURNAME;
        if(TextUtils.isEmpty(phone_number))
            return Constants.EMPTY_PHONE_NUMBER;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Constants.VALID_EMAIL;
        if(password == null || password.length() < Constants.MIN_PASSWORD_LENGTH)
            return Constants.VALID_PASSWORD;
        if(!password.equals(confirm_password))
            return Constants.VALID_CONF_PASSWORD;
        else
            return Constants.REGISTER_SUCCESS;
    }

    public int isValidEditData() {
        if (TextUtils.isEmpty(name))
            return Constants.EMPTY_NAME;
        if (TextUtils.isEmpty(surname))
            return Constants.EMPTY_SURNAME;
        if (TextUtils.isEmpty(phone_number))
            return Constants.EMPTY_PHONE_NUMBER;
        else
            return Constants.EDIT_SUCCESS;
    }
}
