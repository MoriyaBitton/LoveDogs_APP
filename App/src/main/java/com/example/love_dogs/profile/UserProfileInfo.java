package com.example.love_dogs.profile;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.love_dogs.login.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class UserProfileInfo {
    public static final String userinfo_node = "user-info";
    public interface OnLoadUserInfo{
        void callback(UserProfileInfo info);
    }
    public int insta_likes;
    public int insta_posts;
    public int volunteer_posts;
    public int volunteer_score;
    public String uid;

    public UserProfileInfo() {

    }
    public static void loadUserInfo(String userId, OnLoadUserInfo callback){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(userinfo_node).child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    UserProfileInfo info = task.getResult().getValue(UserProfileInfo.class);
                    callback.callback(info);
                }else{
                    // there is no user info yet
                    UpdateUserInfo(userId, callback);
                }
            }
        });
    }
    public static void UpdateUserInfo(String userId, OnLoadUserInfo callback){
        int[] done = {4};
        OnLoadUserInfo call = new OnLoadUserInfo() {
            @Override
            public void callback(UserProfileInfo info) {
                done[0]--;
                if(done[0] <= 0){
                    loadUserInfo(userId, callback);
                }
            }
        };
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(userinfo_node).child(userId).child("uid").setValue(userId);

        UpdateUserInfoLikes(userId, call);
        UpdateUserInfoInstaPosts(userId, call);
        UpdateUserInfoScore(userId, call);
        UpdateUserInfoVolunteerPosts(userId, call);
    }
    private static void UpdateUserInfoLikes(String userId, OnLoadUserInfo callback){
        // update likes
        callback.callback(null);
    }
    private static void UpdateUserInfoInstaPosts(String userId, OnLoadUserInfo callback){
        // update insta posts count
        callback.callback(null);
    }
    private static void UpdateUserInfoScore(String userId, OnLoadUserInfo callback){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("volunteers").child("users").child(userId);
        myTopPostsQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    int score = 0;
                    for (DataSnapshot snapshot: task.getResult().getChildren()) {
                        for (DataSnapshot snapshot_inner: snapshot.getChildren()) {
                            score += snapshot_inner.getChildrenCount();
                        }
                    }
                    mDatabase.child(userinfo_node).child(userId).child("volunteer_score")
                            .setValue(score);
                    callback.callback(null);
                }else{
                    mDatabase.child(userinfo_node).child(userId).child("volunteer_score").setValue(0);
                    callback.callback(null);
                }
            }
        });
    }
    private static void UpdateUserInfoVolunteerPosts(String userId, OnLoadUserInfo callback){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("user-posts").child(userId);
        myTopPostsQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    mDatabase.child(userinfo_node).child(userId).child("volunteer_posts")
                            .setValue(task.getResult().getChildrenCount());
                    callback.callback(null);
                }else{
                    mDatabase.child(userinfo_node).child(userId).child("volunteer_posts").setValue(0);
                    callback.callback(null);
                }
            }
        });
    }
    public void increase_likes(int value) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(userinfo_node + "/" + uid + "/likes", ServerValue.increment(value));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.updateChildren(updates);
    }
    public void increase_num_posts(int value) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(userinfo_node + "/" + uid + "/num_posts", ServerValue.increment(value));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.updateChildren(updates);
    }
    public void increase_volunteer_score(int value) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(userinfo_node + "/" + uid + "/volunteer_score", ServerValue.increment(value));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.updateChildren(updates);
    }
}
