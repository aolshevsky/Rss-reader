package com.example.user.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.user.myapplication.R;
import com.example.user.myapplication.model.User;
import com.example.user.myapplication.utils.DatabaseHelper;
import com.example.user.myapplication.utils.ImageHelper;
import com.example.user.myapplication.utils.PermissionsHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static com.example.user.myapplication.utils.RequestCode.CAMERA;
import static com.example.user.myapplication.utils.RequestCode.GALLERY;
import static com.example.user.myapplication.utils.RequestCode.IMAGE_DIRECTORY;


public class ProfileFragment extends Fragment {


    private ImageView imageview_edit;
    private ImageView imageview_show;
    private View profileView;

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private String img_path;
    private String user_id = "local_user";
    private ProgressDialog progressBar;

    private DatabaseHelper databaseHelper;
    private PermissionsHelper permissionsHelper;
    private ImageHelper imageHelper;

    private ProfileFragment profileFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper();
        permissionsHelper = new PermissionsHelper();
        imageHelper = new ImageHelper();

        initializeView();

        loadUserInformation();

        profileFragment = this;
        getActivity().setTitle("Profile");

        return profileView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause(){
        super.onPause();
        // saveUser();
    }


    private void initializeView(){
        imageview_edit = profileView.findViewById(R.id.avatarImgView);
        imageview_show = profileView.findViewById(R.id.avatarImgView1);

        editTextName = profileView.findViewById(R.id.name_txtEdit);
        editTextSurname = profileView.findViewById(R.id.surname_txtEdit);
        editTextEmail = profileView.findViewById(R.id.email_txtEdit);
        editTextPhone = profileView.findViewById(R.id.phone_txtEdit);
        nameTextView = profileView.findViewById(R.id.name_txtView);
        surnameTextView = profileView.findViewById(R.id.surname_txtView);
        emailTextView = profileView.findViewById(R.id.email_txtView);
        phoneTextView = profileView.findViewById(R.id.phone_txtView);
        Button buttonSave = profileView.findViewById(R.id.save_btn);
        ImageButton buttonSwitch = profileView.findViewById(R.id.switch_btn);
        ImageButton buttonSwitch1 = profileView.findViewById(R.id.switch_btn1);
        final ViewSwitcher viewSwitcher = profileView.findViewById(R.id.profile_switcher);
        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        viewSwitcher.setInAnimation(in);

        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        viewSwitcher.setOutAnimation(out);

        progressBar = new ProgressDialog(getActivity());
        img_path = "";

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showNext();
            }
        });
        buttonSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showNext();
            }
        });
        imageview_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHelper.showPictureDialog(getActivity(), permissionsHelper);
            }
        });

    }

    public void saveUser(){
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String phone_number = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(name)){
            editTextName.setError("Please enter your Name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(surname)){
            editTextSurname.setError("Please enter your Surname");
            editTextSurname.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone_number)){
            editTextPhone.setError("Please enter your Phone number");
            editTextPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(img_path)){
            Toast.makeText(getActivity(), "Please load or create image", Toast.LENGTH_LONG).show();
            return;
        }

        // String id = databaseUsers.push().getKey();
        User user = new User(user_id, name, surname, email, phone_number, img_path);
        databaseHelper.uploadImageToFirebaseStorage(getActivity(), progressBar, user_id, img_path);
        databaseHelper.SaveUserToDatabase(user);
        // Toast.makeText(getActivity(), "Save User", Toast.LENGTH_LONG).show();

    }


    public void SetProfileImg(Bitmap bmp){
        imageview_show.setImageBitmap(bmp);
        imageview_edit.setImageBitmap(bmp);
        img_path = imageHelper.saveImage(bmp);
    }

    private void loadUserInformation(){
        databaseHelper.getDatabaseUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(user_id).getValue(User.class);
                if(user != null) {
                    editTextName.setText(user.getName());
                    editTextSurname.setText(user.getSurname());
                    editTextEmail.setText(user.getEmail());
                    editTextPhone.setText(user.getPhone_number());

                    nameTextView.setText(user.getName());
                    surnameTextView.setText(user.getSurname());
                    emailTextView.setText(user.getEmail());
                    phoneTextView.setText(user.getPhone_number());


                    File imgFile = new  File(user.getImg_path());
                    if(imgFile.exists()){
                        Bitmap iconBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageview_show.setImageBitmap(iconBitmap);
                        imageview_edit.setImageBitmap(iconBitmap);
                        img_path = user.getImg_path();
                    } else {
                        databaseHelper.downloadFromFirebaseStorage(progressBar, profileFragment, user_id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        imageHelper.onActivityResult(this, requestCode, resultCode, data);
    }

}
