package com.example.love_dogs.posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.love_dogs.R;
import com.example.love_dogs.login.User;

public class ViewPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        if(User.IsLoggedIn(this) == null){
            return;
        }
    }

    public void OnCreatePost(View view){
        Intent intent = new Intent(ViewPostsActivity.this, CreatePostActivity.class);
        startActivity(intent);
    }
}