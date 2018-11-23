package com.example.user.myapplication.model;

import android.text.TextUtils;
import android.util.Patterns;

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

    public int isValidLoginData(){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return 0;
        if(password != null && TextUtils.isEmpty(password) && password.length() < 6)
            return 1;
        else
            return -1;
    }

    public int isValidRegisterData(){
        if(TextUtils.isEmpty(name))
            return 0;
        if(TextUtils.isEmpty(surname))
            return 1;
        if(TextUtils.isEmpty(phone_number))
            return 2;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return 3;
        if(password != null && TextUtils.isEmpty(password) && password.length() < 6)
            return 4;
        if(password != null && password.equals(confirm_password))
            return 5;
        else
            return -1;
    }

    public int isValidEditData() {
        if (TextUtils.isEmpty(name))
            return 0;
        if (TextUtils.isEmpty(surname))
            return 1;
        if (TextUtils.isEmpty(phone_number))
            return 2;
        else
            return -1;
    }
}
