package com.example.user.myapplication.Presenter;

import android.net.Uri;

import com.example.user.myapplication.Presenter.Interface.IDeepLinksPresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IDeepLinksView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinksPresenter extends BasePresenter<IDeepLinksView> implements IDeepLinksPresenter{

    private static DeepLinksPresenter instance = new DeepLinksPresenter();


    public static DeepLinksPresenter getInstance(){
        return instance;
    }

    @Override
    public void uriNavigate() {
        Uri data = view.getUri();
        String text = data == null? null : data.getLastPathSegment();
        Pattern p = Pattern.compile("^/page/\\d*$");


        int pageNum = 0;
        try {
            Matcher m = p.matcher(data.getPath());
            if(m.matches())
                pageNum = Integer.parseInt(text);
        }
        catch (NumberFormatException|NullPointerException ignored) {
        }
        switch (pageNum) {
            case 1: {
                view.navigateTo(R.id.homeFragment);
                break;
            }
            case 2: {
                view.navigateTo(R.id.profileFragment);
                break;
            }
            case 3: {
                view.navigateTo(R.id.aboutFragment);
                break;
            }
            default:
                break;
        }

    }
}
