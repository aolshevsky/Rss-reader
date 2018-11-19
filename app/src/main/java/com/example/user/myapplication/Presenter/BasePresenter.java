package com.example.user.myapplication.Presenter;

public class BasePresenter<V> {
    protected V view;

    public void attachView(V view){
        this.view = view;
    }

}
