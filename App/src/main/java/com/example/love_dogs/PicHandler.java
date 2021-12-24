package com.example.love_dogs;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PicHandler extends AppCompatActivity {
    private ImageView img;
    private StorageReference storage;
    private FirebaseAuth fAuth;
    private String url;
    private Context context;

    public PicHandler(ImageView img,Context context) {
        this.img = img;
        this.context = context;
        storage = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public StorageReference getStorage() {
        return storage;
    }

    public void setStorage(StorageReference storage) {
        this.storage = storage;
    }

    public FirebaseAuth getfAuth() {
        return fAuth;
    }

    public void setfAuth(FirebaseAuth fAuth) {
        this.fAuth = fAuth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    private void checkFilePermissions(){

        int requestCode = 0;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

                int permissionCheck = PicHandler.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
                permissionCheck += PicHandler.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
                if(permissionCheck != 0){
                    this.requestPermissions(new String[]{Manifest.permission.
                            READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode);
                }
        }
        }

    /* Function to upload images to Firebase storage */
    public void uploadToStorage(){

    }
}
