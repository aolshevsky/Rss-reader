package com.example.user.myapplication.View;

import android.net.Uri;

public interface IDeepLinksView {
    Uri getUri();
    void navigateTo(int fragment_id);
}
