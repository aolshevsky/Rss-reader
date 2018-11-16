package com.example.user.myapplication.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.fragment.ProfileFragment;
import com.example.user.myapplication.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;

public class DatabaseHelper {

    private static DatabaseHelper instance = new DatabaseHelper();

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;
    private StorageReference storageRef;


    private DatabaseHelper(){
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static DatabaseHelper getInstance(){
        return instance;
    }


    public DatabaseReference getDatabaseUsers(){
        return databaseUsers;
    }
    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }

    public void SaveUserToDatabase(User userInfo){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseUsers.child(user.getUid()).setValue(userInfo);
    }


    public void uploadImageToFirebaseStorage(final Activity activity, final ProgressDialog progressBar, String img_path){
        //progressBar.setMessage("Uploading...");
        //progressBar.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        Uri file = Uri.fromFile(new File(img_path));
        String new_file_path =  String.format("profile_images/users/%s/profile_icon.jpg", user.getUid());
        StorageReference image_storage = storageRef.child(new_file_path);

        image_storage.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(activity, "Upload image success", Toast.LENGTH_LONG).show();
                        //progressBar.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "Upload image failed", Toast.LENGTH_LONG).show();
                        //progressBar.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //progressBar.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused!");
                    }
                });
    }

    public void downloadFromFirebaseStorage(final ProgressDialog progressBar, final ProfileFragment profileFragment) {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final Activity activity = profileFragment.getActivity();
        String new_file_path =  String.format("profile_images/users/%s/profile_icon.jpg", user.getUid());
        StorageReference image_storage = storageRef.child(new_file_path);
        if (image_storage != null) {
            progressBar.setTitle("Downloading...");
            progressBar.setMessage(null);
            progressBar.show();
            try {
                final File localFile = File.createTempFile("images", ".jpg");

                image_storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        profileFragment.SetProfileImg(bmp);
                        profileFragment.saveUser();
                        progressBar.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.dismiss();
                        Toast.makeText(activity, "Download failed. Check internet connection", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        progressBar.setMessage("Downloaded " + ((int) progress) + "%...");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "Upload file before downloading", Toast.LENGTH_LONG).show();
        }
    }


    public void loadUserInformationMenu(final Activity activity){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.child(user.getUid()).getValue(User.class);
                if(userInfo != null) {
                    TextView textViewFullName = activity.findViewById(R.id.full_name_txt);
                    TextView textViewEmail = activity.findViewById(R.id.email_txt);
                    if (textViewFullName != null)
                        textViewFullName.setText(String.format("%s %s", userInfo.getName(), userInfo.getSurname()));
                    textViewEmail.setText(userInfo.getEmail());

                    File imgFile = new  File(userInfo.getImg_path());

                    if(imgFile.exists()){
                        Bitmap iconBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ImageView profileImgView = activity.findViewById(R.id.profileImgView);
                        profileImgView.setImageBitmap(iconBitmap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
