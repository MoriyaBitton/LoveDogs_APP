package com.example.love_dogs.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import com.example.love_dogs.R;
import com.example.love_dogs.posts.ViewPostsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Main_logged_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged_in);
        if(User.IsLoggedIn(this) == null){
            return;
        }
        User user = User.current;

        TextView text = findViewById(R.id.uhello);
        text.setText("Hello, " + user.user_name + "!");

        //User.AddUser(user, "mika", "0543210123");
    }

    public void ViewPosts(View view){
        Intent intent = new Intent(Main_logged_in.this, ViewPostsActivity.class);
        startActivity(intent);
    }

    public void UpdateProfile(View view){
        Intent intent = new Intent(Main_logged_in.this, UpdateUserActivity.class);
        startActivity(intent);
    }
}