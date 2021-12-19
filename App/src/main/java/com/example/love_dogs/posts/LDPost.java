package com.example.love_dogs.posts;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LDPost {
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static HashMap<String, LDPost> all_posts = new HashMap<>();
    public static LDPost current = null;

    public String title;
    public String author;
    public String authorId;
    public String date;
    public String location;
    public String body;
    public String pid;
    public long timestamp;

    public LDPost(){

    }

    public LDPost(String title, String author, String authorID, String date, String location, String body){
        this.title = title;
        this.author = author;
        this.authorId = authorID;
        this.date = date;
        try {
             Date r_date = simpleDateFormat.parse(date);
             timestamp = r_date.getTime();
        }catch (Exception ex){
            Log.e("firebase", "LDEvent: Failed to parse date");
        }
        this.location = location;
        this.body = body;
    }

    public void push(){
        if(pid != null){
            return;
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
        this.pid = key;

        all_posts.put(pid, this);
        this.updatePost();
    }

    public void updatePost(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + this.pid, postValues);
        childUpdates.put("/user-posts/" + this.authorId + "/" + this.pid, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("authorId", authorId);
        result.put("author", author);
        result.put("title", title);
        result.put("location", location);
        result.put("body", body);
        result.put("date", date);
        result.put("pid", pid);
        result.put("timestamp", timestamp);

        return result;
    }
}
