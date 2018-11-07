package com.example.user.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;


public class ProfileFragment extends Fragment {


    private ImageView imageview;
    private View profileView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        imageview = profileView.findViewById(R.id.avatarImgView);

        return profileView;
    }

}
