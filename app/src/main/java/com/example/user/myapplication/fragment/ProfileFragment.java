package com.example.user.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.example.user.myapplication.Manager.DatabaseManager;
import com.example.user.myapplication.Presenter.DatabasePresenter;
import com.example.user.myapplication.Presenter.ImagePresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IDatabaseView;
import com.example.user.myapplication.View.IImageView;
import com.example.user.myapplication.model.User;
import com.example.user.myapplication.utils.Constants;
import com.example.user.myapplication.utils.PermissionsHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_CANCELED;
import static com.example.user.myapplication.utils.Constants.CAMERA;
import static com.example.user.myapplication.utils.Constants.GALLERY;


public class ProfileFragment extends Fragment implements IImageView, IDatabaseView {


    private ImageView imageview_edit, imageview_show;
    private View profileView;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private TextView nameTextView, surnameTextView, emailTextView, phoneTextView;

    private String img_path;
    private ProgressDialog progressBar;

    private PermissionsHelper permissionsHelper;
    private ImagePresenter imagePresenter;
    private DatabasePresenter databasePresenter;
    private DatabaseManager databaseManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        permissionsHelper = PermissionsHelper.getInstance();
        imagePresenter = ImagePresenter.getInstance();
        imagePresenter.attachView(this);
        databasePresenter = new DatabasePresenter();
        databasePresenter.attachView(this);
        databaseManager = DatabaseManager.getInstance();

        initializeView();

        loadUserInformation();

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
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        databasePresenter.saveUser();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                viewSwitcher.showNext();
            }
        };

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databasePresenter.saveUser();
            }
        });
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNeedToUpdateUser()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Save user information?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } else {
                    viewSwitcher.showNext();
                }

            }
        });
        buttonSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showNext();
            }
        });
        profileView.findViewById(R.id.edit_image_touch_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePresenter.showPictureDialog();
            }
        });
    }

    @Override
    public void setUserInfo(User userInfo) {
        TextView textViewFullName = getActivity().findViewById(R.id.full_name_txt);
        TextView textViewEmail = getActivity().findViewById(R.id.email_txt);
        if (textViewFullName != null)
            textViewFullName.setText(String.format("%s %s", userInfo.getName(), userInfo.getSurname()));
        if (textViewEmail != null)
            textViewEmail.setText(userInfo.getEmail());

        File imgFile = new File(userInfo.getImg_path());

        if (imgFile.exists()) {
            Bitmap iconBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView profileImgView = getActivity().findViewById(R.id.profileImgView);
            if (profileImgView != null)
                profileImgView.setImageBitmap(iconBitmap);
        }
    }

    @Override
    public User getUserInfo() {
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String phone_number = editTextPhone.getText().toString();
        return new User(name, surname, phone_number, img_path);
    }


    @Override
    public void setProfileImg(Bitmap bmp){
        imageview_show.setImageBitmap(bmp);
        imageview_edit.setImageBitmap(bmp);
        img_path = imagePresenter.saveImage(bmp);
    }

    private void loadUserInformation(){
        databaseManager.getDatabaseRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.child(databaseManager.getAuthUserID()).getValue(User.class);
                if(userInfo != null) {
                    editTextName.setText(userInfo.getName());
                    editTextName.setSelection(editTextName.getText().length());
                    editTextSurname.setText(userInfo.getSurname());
                    editTextSurname.setSelection(editTextSurname.getText().length());
                    editTextPhone.setText(userInfo.getPhone_number());
                    editTextPhone.setSelection(editTextPhone.getText().length());

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
                        databasePresenter.downloadFromFirebaseStorage();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onErrorMessage("Failed load user information");
            }
        });
    }

    public boolean checkNeedToUpdateUser(){
        User cur_user = databaseManager.getCurrentUser();
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String phone_number = editTextPhone.getText().toString();
        if (cur_user != null)
            return !cur_user.getName().equals(name) ||
                    !cur_user.getPhone_number().equals(phone_number) ||
                    !cur_user.getSurname().equals(surname);
        else
            return false;
    }

    public void saveUser(){
        databasePresenter.saveUser();
    }

    @Override
    public void validUserName(String message) {
        editTextName.setError(message);
        editTextName.requestFocus();
    }

    @Override
    public void validUserSurname(String message) {
        editTextSurname.setError(message);
        editTextSurname.requestFocus();
    }

    @Override
    public void validUserPhone(String message) {
        editTextPhone.setError(message);
        editTextPhone.requestFocus();
    }

    @Override
    public AlertDialog.Builder createPictureDialog() {
        return new AlertDialog.Builder(getContext());
    }

    @Override
    public boolean hasNeedPermissions(int per_ind) {
        return permissionsHelper.hasNeedPermissions(getActivity(), per_ind);
    }

    @Override
    public void requestNeedPerms(int per_ind) {
        permissionsHelper.requestNeedPerms(getActivity(), per_ind);
    }

    @Override
    public void choosePhotoFromGallary() {
        Log.d("myLogs", "Image from galary");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
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
                    setProfileImg(bmp);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            setProfileImg(bmp);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return progressBar;
    }

    @Override
    public void onSuccessMessage(String message) {
        Toasty.success(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorMessage(String message) {
        Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
