package com.example.user.myapplication.model;

public class User {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String phone_number;
    private String img_path;

    public User(){}

    public User(String id, String name, String surname, String email, String phone_number, String img_path) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
        this.img_path = img_path;
    }

    public String getId() {
        return id;
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

    public String getPhone_number() {
        return phone_number;
    }

    public String getImg_path() {
        return img_path;
    }
}
