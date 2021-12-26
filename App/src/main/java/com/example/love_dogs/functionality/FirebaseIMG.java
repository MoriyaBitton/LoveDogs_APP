package com.example.love_dogs.functionality;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.love_dogs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class FirebaseIMG {
    public interface IMGTake{

    }
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE_GALLERY = 2;

    public static void showPopup(Fragment fragment, View v) {
        PopupMenu popup = new PopupMenu(fragment.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.photo_src_menu, popup.getMenu());
        MenuItem cam = popup.getMenu().findItem(R.id.photo_take_button);
        MenuItem gall = popup.getMenu().findItem(R.id.photo_choose_button);


        cam.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dispatchTakePictureIntent(fragment);
                Log.d("firebase", "taking image...");
                return true;
            }
        });


        gall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadGallery(fragment);
                return true;
            }
        });

        popup.show();

    }

    private static void dispatchTakePictureIntent(Fragment context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            context.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private static void loadGallery(Fragment activity) {
        Intent choose = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(choose, PICK_IMAGE_GALLERY);
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data, ImageView img) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == PICK_IMAGE_GALLERY) && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            Bitmap imageBitmap;
            if (PICK_IMAGE_GALLERY == requestCode) {
                //Get image
                Uri selectedImage = data.getData();
                img.setImageURI(selectedImage);
            } else {
                imageBitmap = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBitmap);
            }
            img.setVisibility(View.VISIBLE);
            img.setAdjustViewBounds(true);
        }
    }

    public static void uploadImg(Context context, ImageView img) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref_storage = storage.getReference("images");
        String img_name_ref = img.getDrawable().toString();
        StorageReference ref_img = ref_storage.child(img_name_ref);
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref_img.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context,"Could not upload image.",Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }
}
