package com.example.user.myapplication.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Parser {

    public static String formatDate(String currentDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        try {
            Date newDate = format.parse(currentDate);
            format = new SimpleDateFormat("dd-MM-yyyy, h:mm a");
            format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            currentDate = format.format(newDate);
        } catch (ParseException e) {
            Log.e("Adapter", "Problem with parsing the date format");
        }
        return currentDate;
    }

    public static int ValidUrl(String url){
        if (url.length() > 0) {
            String urlPattern = "^http(s?)://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
            if (url.matches(urlPattern)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }
}
