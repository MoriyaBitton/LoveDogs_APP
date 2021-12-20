package com.example.love_dogs.volunteers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.login.User;
import com.example.love_dogs.posts.LDPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VolunteerBoard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VolunteerBoard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VolunteerBoard() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VolunteerBoard newInstance(String param1, String param2) {
        VolunteerBoard fragment = new VolunteerBoard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    boolean loading = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_volunteer_board, container, false);
        // Inflate the layout for this fragment

        Button add_post = view.findViewById(R.id.vb_add_post);
        User me = User.getCurrentRaw();
        if(me.organizationID == null) {
            add_post.setVisibility(View.GONE);
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        LinearLayout layout = view.findViewById(R.id.vscroll);
        LayoutInflater linear_layour_inflater =  getLayoutInflater();

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
                        LDPost post = child.getValue(LDPost.class);
                        Log.d("firebase", post.location);

                        LDPost.all_posts.put(post.pid, post);

                        View child_view = linear_layour_inflater.inflate(R.layout.volunteer_post_snapshot,null);
                        View main_layout =  child_view.findViewById(R.id.vp_main);


                        main_layout.setOnClickListener(VolunteerBoard.this::OnClickPost);
                        TextView title = (TextView) child_view.findViewById(R.id.vp_title);
                        title.setText(post.title);
                        TextView location = child_view.findViewById(R.id.vp_location);
                        location.setText(post.location);
                        TextView date = child_view.findViewById(R.id.vp_time);
                        date.setText(post.date);

                        TextView author = child_view.findViewById(R.id.vp_username);
                        author.setText(post.author);

                        TextView body = child_view.findViewById(R.id.vp_body);
                        String upToNCharacters = post.body.substring(0, Math.min(post.body.length(), 20));
                        if(upToNCharacters.length() == 20){
                            upToNCharacters += "...";
                        }
                        body.setText(upToNCharacters);

                        TextView pid = child_view.findViewById(R.id.vp_id);
                        pid.setText(post.pid);
                        layout.addView(child_view);
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
        return view;
    }

    public void OnClickPost(View view){
        TextView pid = view.findViewById(R.id.vp_id);
         LDPost.current = LDPost.all_posts.get(pid.getText());

         View v = getView().findViewById(R.id.vview_posts);
         v.setVisibility(View.GONE);

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myFragment = new VolunteerPostFragment(v);
        int id = ((ViewGroup)getView().getParent()).getId();
        activity.getSupportFragmentManager().beginTransaction().replace(id, myFragment).commit();
    }
}