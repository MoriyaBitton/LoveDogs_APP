package com.example.love_dogs.posts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.love_dogs.R;
import com.example.love_dogs.login.User;
import com.example.love_dogs.volunteers.VolunteerPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewPostsActivity extends AppCompatActivity {

    boolean loading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_volunteer_board);

        if(!User.isLoggedIn(this)){
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        LinearLayout layout = findViewById(R.id.vscroll);
        LayoutInflater inflater =  getLayoutInflater();

        Query myTopPostsQuery = mDatabase.child("posts").orderByChild("timestamp");
        boolean loaded = false;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

//                    String post = dataSnapshot.getValue(String.class);
                    if(loading == false){
                        return;
                    }
                    loading = false;
                    Log.w("firebase", "number of posts : " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        // Extract a Message object from the DataSnapshot
                        VolunteerPost post = child.getValue(VolunteerPost.class);
                        Log.d("firebase", post.location);

                        VolunteerPost.all_posts.put(post.pid, post);

//                        View child_view = inflater.inflate(R.layout.vpost,null);
//                        child_view.setOnClickListener(ViewPostsActivity.this::OnClickPost);
//                        TextView title = (TextView) child_view.findViewById(R.id.view_title);
//                        title.setText(post.title);
//                        TextView location = child_view.findViewById(R.id.view_location);
//                        location.setText(post.location);
//                        TextView date = child_view.findViewById(R.id.view_date);
//                        date.setText(post.date);
//
//                        TextView pid = child_view.findViewById(R.id.view_postID);
//                        pid.setText(post.pid);
//                        layout.addView(child_view);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

//        for (int i = 0; i < 10; i++) {
//            View child = inflater.inflate(R.layout.vpost,null);
//            child.setOnClickListener(this::OnClickPost);
//            TextView tv = (TextView) child.findViewById(R.id.view_title);
//            tv.setText("Child No. " + i);
//            layout.addView(child);
//        }
    }

    public void OnClickPost(View view){
//        TextView pid = view.findViewById(R.id.view_postID);
//        //Log.d("firebase", "pid : " + pid.getText().toString());
//        Intent intent = new Intent(this, ViewPostActivity.class);
//        LDPost.current = LDPost.all_posts.get(pid.getText());
//        startActivity(intent);
    }

}