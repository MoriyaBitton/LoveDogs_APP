package com.example.love_dogs.functionality;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.NoSuchElementException;

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

    public static void loadImg(Context context, String img_url,ImageView img) {
        if(img_url == null){
            img.setVisibility(View.GONE);
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref_storage = storage.getReference("images");
        //String img_name_ref = img.getDrawable().toString();
        StorageReference ref_img = ref_storage.child(img_url);
        final long ONE_MEGABYTE = 1024 * 1024;
        ref_img.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                int currentBitmapWidth = bmp.getWidth();
                int currentBitmapHeight = bmp.getHeight();

                int ivWidth = img.getWidth();
                int ivHeight = img.getHeight();
                int newWidth = ivWidth;

                int newHeight = (int) Math.floor((double) currentBitmapHeight *( (double) newWidth / (double) currentBitmapWidth));

                Bitmap newbitMap = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);

                img.setImageBitmap(newbitMap);

                //img.setImageBitmap(Bitmap.createScaledBitmap(bmp, img.getWidth(), img.getHeight(), false));
                img.setVisibility(View.VISIBLE);
                //scaleImage(context, img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                img.setVisibility(View.GONE);
                // Handle any errors
            }
        });
    }

    private static void scaleImage(Context context, ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            return;
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(context,500);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private static int dpToPx(Context context,int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
