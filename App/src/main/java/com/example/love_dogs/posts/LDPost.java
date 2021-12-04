package com.example.love_dogs.posts;

import android.util.Log;

import com.example.love_dogs.login.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LDPost {
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static HashMap<String, LDPost> all_posts = new HashMap<>();
    public static LDPost current = null;

    public String title;
    public String author;
    public String authorID;
    public Date date;
    public String location;
    public String body;
    public String pID = null;

    public LDPost(){

    }

    public LDPost(String title, String author, String authorID, String date, String location, String body){
        this.title = title;
        this.author = author;
        this.authorID = authorID;
        try {
            this.date = simpleDateFormat.parse(date);
        }catch (Exception ex){
            Log.e("firebase", "LDEvent: Failed to parse date");
        }
        this.location = location;
        this.body = body;
    }

    public void push(){
        if(pID != null){
            return;
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
        this.pID = key;

        all_posts.put(pID, this);
        this.updatePost();
    }

    public void updatePost(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + this.pID, postValues);
        childUpdates.put("/user-posts/" + this.authorID + "/" + this.pID, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("authorId", authorID);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("date", simpleDateFormat.format(date));
        result.put("pid", pID);

        return result;
    }
}
