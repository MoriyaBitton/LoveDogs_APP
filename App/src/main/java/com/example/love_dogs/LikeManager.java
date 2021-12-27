package com.example.love_dogs;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import com.example.love_dogs.login.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public  class LikeManager {
    List<String> user_liked_posts;
    HashMap<String,String> Posts_likes;
    FirebaseDatabase mDataBase;

    boolean liked = false;
    Activity activity;
    View view;
    
    public LikeManager(Activity activity ,View v){
        this.activity = activity;
        this.view = v;
        User user = User.getCurrentRaw();
        mDataBase = FirebaseDatabase.getInstance();

        DatabaseReference user_posts = mDataBase.getReference("users_likes/" + user.uid);

        Button button = v.findViewById(R.id.like_btn);
        button.setOnClickListener(this::onLikePressed);
    }

    public void onLikePressed(View view){
        Button likeBtn = (Button) view;
        int tag;
        if(likeBtn.getTag()!=null){
            tag = (int) likeBtn.getTag();
        }
        else{tag=0;}
        tag=(tag+1)%2;
        if(tag==1) {
            likeBtn.setTag(1);
            likeBtn.setBackgroundColor(activity.getResources().getColor(R.color.red));
        }

        else{
            likeBtn.setTag(0);
            likeBtn.setBackgroundColor(activity.getResources().getColor(R.color.light_grey));
        }
    }
}
