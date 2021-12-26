package com.example.love_dogs.volunteers;

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

public class VolunteerPost {
    public final String volunteers_post_types_db = "/volunteers/posts/";
    public final String volunteers_users_types_db = "/volunteers/users/";

    public static class RoleFieldData{
        public String type;
        public int required;

        public RoleFieldData(){

        }
    }

    public class RoleField{
        public String type;
        public int required;

        public transient Set<String> subscribed = new HashSet<>();

        public RoleField(){
        }

        public RoleField(RoleFieldData data){
            this.type = data.type;
            this.required = data.required;
        }

        public int getNumSubscribed(){
            return subscribed.size();
        }

        public RoleField(View view, int type_id, int required_id){
            TextView typet = view.findViewById(type_id);
            TextView reuqiredt = view.findViewById(required_id);
            this.type = typet.getText().toString();
            this.required = Integer.parseInt(reuqiredt.getText().toString());
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("type", type);
            result.put("required", required);

            return result;
        }

        public void addVolunteer(String userID){
            if(subscribed.contains(userID)){
                return;
            }
            Log.d("firebase","added volunteer, " + type);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(volunteers_post_types_db + pid + "/" + type + "/subscribed").child(userID).setValue(true);
            mDatabase.child(volunteers_users_types_db + userID + "/" + pid + "/subscribed").child(type).setValue(true);
            subscribed.add(userID);
        }

        public void removeVolunteer(String userID){
            if(!subscribed.contains(userID)){
                return;
            }
            Log.d("firebase","removed volunteer, " + type);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(volunteers_post_types_db + pid + "/" + type + "/subscribed").child(userID).removeValue();
            mDatabase.child(volunteers_users_types_db + userID + "/" + pid + "/subscribed").child(type).removeValue();
            subscribed.remove(userID);
        }

        public void loadSubscribed(OnSuccessListener<Void> listener){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Query getRolesQuery = mDatabase.child(volunteers_post_types_db + pid + "/" + type + "/subscribed");
            FirebaseGetList.getKeySetOnce(getRolesQuery, new FirebaseGetList.SetCallback<String>() {
                @Override
                public void getSet(Set<String> items) {
                    subscribed = items;
                    listener.onSuccess(null);
                }
            });
        }
    }

    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static HashMap<String, VolunteerPost> all_posts = new HashMap<>();
    public static VolunteerPost current = null;

    public String title;
    public String author;
    public String authorId;
    public String date;
    public String location;
    public String body;
    public String pid;
    public String img_url;
    public int volunteers_needed;
    public long timestamp;
    public transient HashMap<String, RoleField> roles = new HashMap<>();

    public VolunteerPost(){

    }

    public void syncWithOld(VolunteerPost old){
        if(old == null || this.pid != null && !this.pid.equals(old.pid)){
            return;
        }
        this.pid = old.pid;
        this.roles = old.roles;
    }

    public void UpdatePost(String title, String author, String authorID, String date, String location, String body){
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

    public void loadRoles(OnSuccessListener<Void> listener){
        if(roles.size() > 0){
            // already loaded.
            listener.onSuccess(null);
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query getRolesQuery = mDatabase.child(volunteers_post_types_db).child(pid);
        FirebaseGetList.getListOnce(getRolesQuery, RoleFieldData.class, new FirebaseGetList.Callback<RoleFieldData>() {
            @Override
            public void getList(ArrayList<RoleFieldData> items) {
                for (RoleFieldData field: items) {
                    roles.put(field.type, new RoleField(field));
                }
                listener.onSuccess(null);
            }
        });
    }

    public void uploadRoles(ArrayList<RoleField> items){
        HashMap<String,RoleField> new_roles = new HashMap();
        for (RoleField field: items) {
            new_roles.put(field.type, field);
        }
        roles = new_roles;
    }

    public void push(){
        if(pid != null){
            updatePost();
            return;
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
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
        mDatabase.child(volunteers_post_types_db + this.pid).removeValue();
        mDatabase.child("posts").child(this.pid).removeValue();
        mDatabase.child("user-posts").child(this.authorId).child(this.pid).removeValue();
        return true;
    }

    private void updatePost(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        mDatabase.child(volunteers_post_types_db + this.pid).removeValue();
        childUpdates.put("/posts/" + this.pid, postValues);
        childUpdates.put("/user-posts/" + this.authorId + "/" + this.pid, postValues);

        for (RoleField field: roles.values()) {
            childUpdates.put(volunteers_post_types_db + this.pid + "/" + field.type, field.toMap());
        }

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
        result.put("img_url", img_url);
        result.put("timestamp", timestamp);

        return result;
    }

}
