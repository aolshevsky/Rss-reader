package com.example.user.myapplication.utils;

import android.app.Activity;
import android.net.Uri;

import com.example.user.myapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.navigation.NavController;

public class DeepLinksHelper {
    public static void uriNavigate(NavController navController, Activity activity) {
        Uri data = activity.getIntent().getData();
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
                navController.navigate(R.id.homeFragment);
                break;
            }
            case 2: {
                navController.navigate(R.id.profileFragment);
                break;
            }
            case 3: {
                navController.navigate(R.id.aboutFragment);
                break;
            }
            default:
                break;
        }

    }
}
