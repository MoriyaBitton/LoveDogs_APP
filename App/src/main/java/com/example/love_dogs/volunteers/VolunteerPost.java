package com.example.love_dogs.volunteers;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.love_dogs.functionality.FirebaseGetList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class VolunteerPost {
    public static class RoleField{
        public String type;
        public int required;
        public int filled;

        public RoleField(){
        }

        public RoleField(View view, int type_id, int required_id){
            TextView typet = view.findViewById(type_id);
            TextView reuqiredt = view.findViewById(required_id);
            this.filled = 0;
            this.type = typet.getText().toString();
            this.required = Integer.parseInt(reuqiredt.getText().toString());
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("type", type);
            result.put("required", required);
            result.put("filled", filled);

            return result;
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
    public String imgUrl;
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

    public VolunteerPost(String title, String author, String authorID, String date, String location, String body){
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
        Query getRolesQuery = mDatabase.child("post_volunteer_list").child(pid);
        FirebaseGetList.getAll(getRolesQuery, RoleField.class, new FirebaseGetList.Callback<RoleField>() {
            @Override
            public void getList(ArrayList<RoleField> items) {
                for (RoleField field: items) {
                    roles.put(field.type, field);
                }
                listener.onSuccess(null);
            }
        });
    }

    public void uploadRoles(ArrayList<RoleField> items){
        HashMap<String,RoleField> new_roles = new HashMap();
        for (RoleField field: items) {
            if(roles.containsKey(field.type)){
                field.filled = roles.get(field.type).filled;
            }else{
                field.filled = 0;
            }
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

    private void updatePost(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        mDatabase.child("/post_volunteer_list/" + this.pid).removeValue();
        childUpdates.put("/posts/" + this.pid, postValues);
        childUpdates.put("/user-posts/" + this.authorId + "/" + this.pid, postValues);

        for (RoleField field: roles.values()) {
            childUpdates.put("/post_volunteer_list/" + this.pid + "/" + field.type, field.toMap());
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
        result.put("timestamp", timestamp);

        return result;
    }

}
