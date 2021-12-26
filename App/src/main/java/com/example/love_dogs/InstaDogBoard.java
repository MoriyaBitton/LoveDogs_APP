package com.example.love_dogs;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.love_dogs.functionality.FragmentManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InstaDogBoard#newInstance} factory method to
 * create an instance of this fragment.
 */


public class InstaDogBoard extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_GALLERY = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button photoBtn;
    private ImageView img;
    private Button postBtn;
    private EditText text;
    private FirebaseStorage storage;

    public InstaDogBoard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DogBook.
     */
    // TODO: Rename and change types and number of parameters
    public static InstaDogBoard newInstance(String param1, String param2) {
        InstaDogBoard fragment = new InstaDogBoard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentManager.ResetAll();
        if (container != null) {
            container.removeAllViews();
        }

        View inflator = inflater.inflate(R.layout.fragment_insta_dog, container, false);

        photoBtn = (Button) inflator.findViewById(R.id.dbpicture);
        postBtn = (Button) inflator.findViewById(R.id.dbpost);
        text = (EditText) inflator.findViewById(R.id.Post_text);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
                img = inflator.findViewById(R.id.photo_post);


            }
        });


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (img != null || !text.getText().toString().isEmpty()) {
                    if (img != null) {
                        uploadImg();
                        String img_name = img.getDrawable().toString();
                    }
                    if(!text.getText().toString().isEmpty());
                    uploadPost();
                }
            }
        });
        // Inflate the layout for this fragment

        return inflator;

    }



    private void uploadPost() {
        String img_name = img.getDrawable().toString();
        String data = text.getText().toString();


        return;
    }

    private void uploadImg() {
        storage = FirebaseStorage.getInstance();
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
                    Toast.makeText(getContext(),"Could not upload image.",Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("firebase","test");
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

    private void loadGallery() {
        Intent choose = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choose, PICK_IMAGE_GALLERY);
    }


    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.photo_src_menu, popup.getMenu());
        MenuItem cam = popup.getMenu().findItem(R.id.photo_take_button);
        MenuItem gall = popup.getMenu().findItem(R.id.photo_choose_button);


        cam.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dispatchTakePictureIntent();
                return true;
            }
        });


        gall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadGallery();
                return true;
            }
        });

        popup.show();

    }


}