package com.example.user.myapplication.fragment;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.user.myapplication.R;
import com.example.user.myapplication.model.User;
import com.example.user.myapplication.utils.DatabaseHelper;
import com.example.user.myapplication.utils.ImageHelper;
import com.example.user.myapplication.utils.PermissionsHelper;
import com.google.firebase.auth.FirebaseUser;
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
    private ProgressDialog progressBar;

    private DatabaseHelper databaseHelper;
    private PermissionsHelper permissionsHelper;
    private ImageHelper imageHelper;

    private ProfileFragment profileFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = DatabaseHelper.getInstance();
        permissionsHelper = PermissionsHelper.getInstance();
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
                imageHelper.showPictureDialog(profileFragment, permissionsHelper);
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
        User userInfo = new User(name, surname, email, phone_number, img_path);
        databaseHelper.uploadImageToFirebaseStorage(getActivity(), progressBar, img_path);
        databaseHelper.SaveUserToDatabase(userInfo);
        // Toast.makeText(getActivity(), "Save User", Toast.LENGTH_LONG).show();

    }


    public void SetProfileImg(Bitmap bmp){
        imageview_show.setImageBitmap(bmp);
        imageview_edit.setImageBitmap(bmp);
        img_path = imageHelper.saveImage(bmp);
    }

    private void loadUserInformation(){
        final FirebaseUser user = databaseHelper.getFirebaseAuth().getCurrentUser();
        databaseHelper.getDatabaseUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.child(user.getUid()).getValue(User.class);
                if(userInfo != null) {
                    editTextName.setText(userInfo.getName());
                    editTextSurname.setText(userInfo.getSurname());
                    editTextEmail.setText(userInfo.getEmail());
                    editTextPhone.setText(userInfo.getPhone_number());

                    nameTextView.setText(userInfo.getName());
                    surnameTextView.setText(userInfo.getSurname());
                    emailTextView.setText(userInfo.getEmail());
                    phoneTextView.setText(userInfo.getPhone_number());


                    File imgFile = new  File(userInfo.getImg_path());
                    if(imgFile.exists()){
                        Bitmap iconBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageview_show.setImageBitmap(iconBitmap);
                        imageview_edit.setImageBitmap(iconBitmap);
                        img_path = userInfo.getImg_path();
                    } else {
                        databaseHelper.downloadFromFirebaseStorage(progressBar, profileFragment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void choosePhotoFromGallary() {
        Log.d("myLogs", "Image from galary");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    public void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("myLogs", "Boom");
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    profileFragment.SetProfileImg(bmp);
                    Toast.makeText(profileFragment.getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            profileFragment.SetProfileImg(bmp);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

}
