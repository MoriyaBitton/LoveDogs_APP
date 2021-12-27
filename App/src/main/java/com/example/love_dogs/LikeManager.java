package com.example.love_dogs;

import android.view.View;

import com.example.love_dogs.login.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public  class LikeManager {
    List<String> user_liked_posts;
    HashMap<String,String> Posts_likes;
    FirebaseDatabase mDataBase;
    
    public void startSession(View v){
        User user = User.getCurrentRaw();
        mDataBase = FirebaseDatabase.getInstance();

        DatabaseReference user_posts = mDataBase.getReference("users_likes/" + user.uid);
        
    }
}
