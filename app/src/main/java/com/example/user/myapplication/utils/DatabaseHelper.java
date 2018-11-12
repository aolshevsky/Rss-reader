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

    private DatabaseReference databaseUsers;
    private StorageReference storageRef;


    public DatabaseHelper(){
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public DatabaseReference getDatabaseUsers(){
        return databaseUsers;
    }

    public void SaveUserToDatabase(User user){
        databaseUsers.child(user.getId()).setValue(user);
    }


    public void uploadImageToFirebaseStorage(final Activity activity, final ProgressDialog progressBar, String user_id, String img_path){
        progressBar.setMessage("Uploading...");
        progressBar.show();
        Uri file = Uri.fromFile(new File(img_path));
        String new_file_path =  String.format("profile_images/users/%s/profile_icon.jpg", user_id);
        StorageReference image_storage = storageRef.child(new_file_path);

        image_storage.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(getActivity(), "Upload image success", Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "Upload image failed", Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        progressBar.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused!");
                    }
                });
    }

    public void downloadFromFirebaseStorage(final ProgressDialog progressBar, final ProfileFragment profileFragment, String user_id) {
        final Activity activity = profileFragment.getActivity();
        String new_file_path =  String.format("profile_images/users/%s/profile_icon.jpg", user_id);
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


    public void loadUserInformationMenu(final Activity activity, final String user_id){

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(user_id).getValue(User.class);
                if(user != null) {
                    TextView textViewFullName = activity.findViewById(R.id.full_name_txt);
                    TextView textViewEmail = activity.findViewById(R.id.email_txt);
                    if (textViewFullName != null)
                        textViewFullName.setText(String.format("%s %s", user.getName(), user.getSurname()));
                    textViewEmail.setText(user.getEmail());

                    File imgFile = new  File(user.getImg_path());

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
