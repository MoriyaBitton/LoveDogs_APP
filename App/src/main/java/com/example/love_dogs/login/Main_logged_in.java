package com.example.love_dogs.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import com.example.love_dogs.R;
import com.example.love_dogs.posts.CreatePostActivity;
import com.example.love_dogs.posts.LDPost;
import com.example.love_dogs.posts.ViewPostActivity;
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

        Log.w("firebase", "number of posts : ");

        TextView text = findViewById(R.id.uhello);
        //Log.d("firebase", "user missing ? " + (user == null));
//        text.setText("Hello, " + user.user_name + "!");

        //User.AddUser(user, "mika", "0543210123");
    }

    public void OnClickPost(View view){
        TextView pid = view.findViewById(R.id.view_postID);
        //Log.d("firebase", "pid : " + pid.getText().toString());
        Intent intent = new Intent(this, ViewPostActivity.class);
        LDPost.current = LDPost.all_posts.get(pid.getText());
        startActivity(intent);
    }


    public void OnCreatePost(View view){
        Intent intent = new Intent(Main_logged_in.this, CreatePostActivity.class);
        startActivity(intent);
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