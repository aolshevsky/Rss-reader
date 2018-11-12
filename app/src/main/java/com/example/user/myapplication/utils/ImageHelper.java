package com.example.user.myapplication.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.user.myapplication.fragment.ProfileFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static com.example.user.myapplication.utils.RequestCode.CAMERA;
import static com.example.user.myapplication.utils.RequestCode.GALLERY;
import static com.example.user.myapplication.utils.RequestCode.IMAGE_DIRECTORY;

public class ImageHelper {

    public void showPictureDialog(final Activity activity, final PermissionsHelper permissionsHelper){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (permissionsHelper.hasNeedPermissions(activity, 1)) {
                                    choosePhotoFromGallary(activity);
                                }
                                else {
                                    permissionsHelper.requestNeedPerms(activity, 1);
                                }
                                break;
                            case 1:
                                if (permissionsHelper.hasNeedPermissions(activity, 2)) {
                                    takePhotoFromCamera(activity);
                                }
                                else {
                                    permissionsHelper.requestNeedPerms(activity, 2);
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallary(Activity activity) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        activity.startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera(Activity activity) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, CAMERA);
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (myBitmap.getByteCount() > 200 * 1024) {
            Bitmap resized = Bitmap.createScaledBitmap(myBitmap, (int) (myBitmap.getWidth() * 0.5), (int) (myBitmap.getHeight() * 0.5), true);
            resized.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        } else {
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            // Calendar.getInstance().getTime()
            File f = new File(wallpaperDirectory, "profile_icon.jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            Log.d("myLogs", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void onActivityResult(ProfileFragment profileFragment, int requestCode, int resultCode, Intent data) {

        Activity activity = profileFragment.getActivity();
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), contentURI);
                    profileFragment.SetProfileImg(bmp);
                    Toast.makeText(profileFragment.getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            profileFragment.SetProfileImg(bmp);
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
