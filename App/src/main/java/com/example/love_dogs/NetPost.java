package com.example.love_dogs;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.love_dogs.functionality.FirebaseGetList;
import com.example.love_dogs.login.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetPost {
    public final String volunteers_post_types_db = "/net-posts/posts/";
    public final String volunteers_users_types_db = "/net-posts/users/";


    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static HashMap<String, com.example.love_dogs.NetPost> all_posts = new HashMap<>();
    public static com.example.love_dogs.NetPost current = null;

    public String authorId;
    public String body;
    public String pid;
    public String imgUrl;
    public long timestamp;

    public NetPost(){
    }

    public NetPost(String body, String imgUrl) {
        this.body = body;
        this.imgUrl = imgUrl;
    }

    public void syncWithOld(com.example.love_dogs.NetPost old){
        if(old == null || this.pid != null && !this.pid.equals(old.pid)){
            return;
        }
        this.pid = old.pid;
    }

    public void UpdatePost(String img,String body){
        this.imgUrl = img;
        try {
            Date now = new Date(System.currentTimeMillis());
            timestamp = now.getTime();
        }catch (Exception ex){
            Log.e("firebase", "LDEvent: Failed to parse date");
        }
        this.body = body;
    }


    public void push(){
        if(pid != null){
            updatePost();
            return;
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("net-posts").push().getKey();
        this.pid = key;

        all_posts.put(pid, this);
        this.updatePost();
    }

    public boolean delete(User user){
        // check that user has the permission to delete the post.
        if(user == null || user.type != User.ADMIN && !user.uid.equals(authorId)){
            return false;
        }
        current = null;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("net-posts").child(this.pid).removeValue();
        mDatabase.child("user-net-posts").child(this.authorId).child(this.pid).removeValue();
        return true;
    }

    private void updatePost(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        mDatabase.child(volunteers_post_types_db + this.pid).removeValue();
        childUpdates.put("/net-posts/" + this.pid, postValues);
        childUpdates.put("/user-net-posts/" + this.authorId + "/" + this.pid, postValues);


        mDatabase.updateChildren(childUpdates);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("body", body);
        result.put("pid", pid);
        result.put("timestamp", timestamp);
        result.put("url", imgUrl);
        result.put("authorId",authorId);
        return result;
    }

}
