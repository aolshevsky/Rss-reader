package com.example.user.myapplication.Presenter;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.example.user.myapplication.Presenter.Interface.IImagePresenter;
import com.example.user.myapplication.View.IImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AlertDialog;

import static com.example.user.myapplication.utils.RequestCode.IMAGE_DIRECTORY;

public class ImagePresenter extends BasePresenter<IImageView> implements IImagePresenter {

    private static ImagePresenter instance = new ImagePresenter();


    public static ImagePresenter getInstance(){
        return instance;
    }

    @Override
    public void showPictureDialog(){
        AlertDialog.Builder pictureDialog = view.createPictureDialog();
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
                                if (view.hasNeedPermissions(1)) {
                                    view.choosePhotoFromGallary();
                                }
                                else {
                                    view.requestNeedPerms(1);
                                }
                                break;
                            case 1:
                                if (view.hasNeedPermissions(2)) {
                                    view.takePhotoFromCamera();
                                }
                                else {
                                    view.requestNeedPerms(2);
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
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
}
