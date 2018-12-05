package com.example.user.myapplication.Presenter;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.user.myapplication.Manager.DatabaseManager;
import com.example.user.myapplication.Presenter.Interface.IDatabasePresenter;
import com.example.user.myapplication.View.IDatabaseView;
import com.example.user.myapplication.Model.User;
import com.example.user.myapplication.Utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;

public class DatabasePresenter extends BasePresenter<IDatabaseView> implements IDatabasePresenter{

    private DatabaseManager manager;

    public DatabasePresenter(){
        manager = DatabaseManager.getInstance();
    }


    @Override
    public void saveUserToDatabase(User userInfo){
        manager.savaUser(userInfo);
    }

    @Override
    public void uploadImageToFirebaseStorage(String img_path){
        final ProgressDialog pDialog = view.getProgressDialog();
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setMessage("Uploading...");
        pDialog.show();
        Uri file = Uri.fromFile(new File(img_path));
        StorageReference image_storage = manager.getImageStorage();

        image_storage.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (view != null)
                            view.onSuccessMessage("Upload image success");
                        pDialog.dismiss();
                        view.unableOrientation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        view.onErrorMessage("Upload image failed");
                        pDialog.dismiss();
                        view.unableOrientation();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        pDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused!");
                    }
                });
    }

    @Override
    public void downloadFromFirebaseStorage() {
        final ProgressDialog pDialog = view.getProgressDialog();
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        StorageReference image_storage = manager.getImageStorage();
        if (image_storage != null) {
            pDialog.setTitle("Downloading...");
            pDialog.setMessage(null);
            pDialog.show();
            try {
                final File localFile = File.createTempFile("images", ".jpg");

                image_storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        view.setProfileImg(bmp);
                        saveUser();
                        pDialog.dismiss();
                        view.unableOrientation();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pDialog.dismiss();
                        view.unableOrientation();
                        view.onErrorMessage("Download failed. Check internet connection");
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        pDialog.setMessage("Downloaded " + ((int) progress) + "%...");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            view.onErrorMessage("Upload file before downloading");
        }
    }

    @Override
    public void loadUserInformationMenu(){
        manager.loadUserInformation(view);
    }

    @Override
    public void saveUser(){
        User userInfo = view.getUserInfo();

        int editCode = validEditData(userInfo.getName(), userInfo.getSurname(), userInfo.getPhone_number());

        if (editCode == Constants.EMPTY_NAME){
            view.validUserName("Please enter your Name");
            return;
        }

        if (editCode == Constants.EMPTY_SURNAME){
            view.validUserSurname("Please enter your Surname");
            return;
        }

        if (editCode == Constants.EMPTY_PHONE_NUMBER){
            view.validUserPhone("Please enter your Phone number");
            return;
        }

        String email = manager.getAuthUserEmail();
        userInfo.setEmail(email);
        if(!userInfo.getImg_path().equals(""))
            uploadImageToFirebaseStorage(userInfo.getImg_path());
        saveUserToDatabase(userInfo);
        loadUserInformationMenu();
    }

    @Override
    public int validEditData(String name, String surname, String phone_number){
        User user = new User(name, surname, phone_number);
        return user.isValidEditData();
    }

}
