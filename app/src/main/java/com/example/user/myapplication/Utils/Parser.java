package com.example.user.myapplication.Utils;

import android.util.Log;

import com.example.user.myapplication.Model.RSSItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Parser {

    public static String formatDate(String currentDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        try {
            Date newDate = format.parse(currentDate);
            format = new SimpleDateFormat("dd-MM-yyyy, h:mm a", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            currentDate = format.format(newDate);
        } catch (ParseException e) {
            Log.e("Adapter", "Problem with parsing the date format");
        }
        return currentDate;
    }

    public static String transformDescription(String description){
        if(description.length() > 100){
            description = description.substring(0, 96).trim() + "...";
        }
        return description;
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

    public static void sortDates(ArrayList<RSSItem> rssItems){
        Collections.sort(rssItems, new Comparator<RSSItem>() {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            @Override
            public int compare(RSSItem o1, RSSItem o2) {
                try {
                    return format.parse(o2.getPubDate()).compareTo(format.parse(o1.getPubDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }
}
