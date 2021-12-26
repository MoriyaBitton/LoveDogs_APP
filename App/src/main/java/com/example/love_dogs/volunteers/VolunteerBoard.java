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
import com.example.love_dogs.functionality.AutoLogin;
import com.example.love_dogs.functionality.FirebaseGetList;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.login.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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

        FragmentManager.ResetAll();
        if (container != null) {
            container.removeAllViews();
        }

        Log.d("firebase", "onCreateView: refresh");
        View view = inflater.inflate(R.layout.fragment_volunteer_board, container, false);
        // Inflate the layout for this fragment

        Button add_post = view.findViewById(R.id.vb_add_post);
        User me = User.getCurrentRaw();
        if(me.type == User.USER) {
            add_post.setVisibility(View.GONE);
        }else{
            add_post.setOnClickListener(this::OnCreatePost);
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        LinearLayout layout = view.findViewById(R.id.vscroll);
        LayoutInflater linear_layour_inflater =  getLayoutInflater();

        Button logout = view.findViewById(R.id.vb_logout);
        logout.setVisibility(View.INVISIBLE);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AutoLogin.logout(getActivity());
//            }
//        });

        //Query myTopPostsQuery = mDatabase.child("posts").orderByChild("timestamp");
        Query myTopPostsQuery = mDatabase.child("posts");
        FirebaseGetList.getListOnce(myTopPostsQuery, VolunteerPost.class, new FirebaseGetList.Callback<VolunteerPost>() {
            @Override
            public void getList(ArrayList<VolunteerPost> items) {
                Collections.sort(items,
                        (o1, o2) -> o1.timestamp > o2.timestamp ? 1 : -1);
                Date current_date = new Date(System.currentTimeMillis());
                for (VolunteerPost post: items) {
                    if(post.timestamp < current_date.getTime()){
                        continue;
                    }

                    VolunteerPost.all_posts.put(post.pid, post);

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
        });

        return view;
    }

    public void OnCreatePost(View view){
        View parent = getView().findViewById(R.id.vview_posts);
        parent.setVisibility(View.GONE);
        FragmentExtended myFragment = new VolunteerPostEditkFragment(parent, null);
        myFragment.showFragment(VolunteerBoard.this);
    }


    public void OnClickPost(View view){
        TextView pid = view.findViewById(R.id.vp_id);
        VolunteerPost target = VolunteerPost.all_posts.get(pid.getText());
         VolunteerPost.current = target;

         View v = getView().findViewById(R.id.vview_posts);
         v.setVisibility(View.GONE);

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myFragment = new VolunteerPostFragment(v, target);
        int id = ((ViewGroup)getView().getParent()).getId();
        activity.getSupportFragmentManager().beginTransaction().replace(id, myFragment).commit();
    }
}