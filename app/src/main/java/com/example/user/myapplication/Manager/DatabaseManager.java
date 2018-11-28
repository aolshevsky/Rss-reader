package com.example.user.myapplication.Manager;

import com.example.user.myapplication.View.IDatabaseView;
import com.example.user.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

public class DatabaseManager {

    private static DatabaseManager instance = new DatabaseManager();


    public static DatabaseManager getInstance(){
        return instance;
    }

    private DatabaseManager(){
        databaseRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private User currentUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;


    public DatabaseReference getDatabaseRef(){
        return databaseRef;
    }

    public FirebaseAuth getAuth(){
        return firebaseAuth;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void savaUser(User userInfo){
        FirebaseUser user = getAuthUser();
        databaseRef.child(user.getUid()).setValue(userInfo);
    }

    public FirebaseUser getAuthUser(){
        return firebaseAuth.getCurrentUser();
    }

    public String getAuthUserID(){
        return getAuthUser().getUid();
    }

    public String getAuthUserEmail(){
        return getAuthUser().getEmail();
    }

    public StorageReference getImageStorage() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        String new_file_path =  String.format("profile_images/users/%s/profile_icon.jpg", user.getUid());
        return storageRef.child(new_file_path);
    }

    public void loadUserInformation(final IDatabaseView view){
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.child(getAuthUserID()).getValue(User.class);
                currentUser = userInfo;
                if(userInfo != null) {
                    view.setUserInfo(userInfo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                view.onErrorMessage("Load user information failed");
            }
        });
    }
}
