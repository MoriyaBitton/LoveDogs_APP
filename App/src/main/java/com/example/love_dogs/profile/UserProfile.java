package com.example.love_dogs.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.login.User;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UserProfile extends Fragment {

<<<<<<< Updated upstream
    public UserProfile() {
        // Required empty public constructor
    }

=======
<<<<<<< HEAD
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public UserProfile() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
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
=======
    public UserProfile() {
        // Required empty public constructor
    }

>>>>>>> ec5e26bc0b2b9bf8d48ccc23b094eb357bc59671
>>>>>>> Stashed changes
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentManager.ResetAll();
        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        TextView username = view.findViewById(R.id.ap_username);
        TextView email = view.findViewById(R.id.ap_email);
        TextView phone = view.findViewById(R.id.ap_phone);
        TextView user_hint = view.findViewById(R.id.ap_username_filedname);

        User user = User.getCurrentRaw();
        if(user.type != User.USER){
            TextView title = view.findViewById(R.id.ap_profile_title);
            if(user.type == User.ORGANIZATION){
                user_hint.setText("name :");
                title.setText("Organization Profile");
            }else{
                title.setText("Admin Profile");
            }
        }

        username.setText(user.user_name);
        phone.setText(user.phone_number);
        email.setText(user.email);

        UserProfileInfo.loadUserInfo(user.uid, new UserProfileInfo.OnLoadUserInfo() {
            @Override
            public void callback(UserProfileInfo info) {
                TextView volunteer_score = view.findViewById(R.id.ap_score);
                TextView posts = view.findViewById(R.id.ap_num_posts);
                volunteer_score.setText(String.valueOf(info.volunteer_posts * 50 + info.volunteer_score * 5));
                posts.setText(String.valueOf(info.volunteer_posts + info.insta_posts));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}