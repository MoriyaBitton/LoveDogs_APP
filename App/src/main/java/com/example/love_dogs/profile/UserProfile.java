package com.example.love_dogs.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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

    public UserProfile() {
        // Required empty public constructor
    }

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

        UserProfileInfo.OnLoadUserInfo update = new UserProfileInfo.OnLoadUserInfo() {
            @Override
            public void callback(UserProfileInfo info) {
                TextView volunteer_score = view.findViewById(R.id.ap_score);
                TextView posts = view.findViewById(R.id.ap_num_posts);
                volunteer_score.setText(String.valueOf(info.volunteer_posts * 50 + info.volunteer_score * 5));
                posts.setText(String.valueOf(info.volunteer_posts + info.insta_posts));
            }
        };

        UserProfileInfo.UpdateUserInfo(user.uid, update);
        //UserProfileInfo.loadUserInfo(user.uid, update);
        // Inflate the layout for this fragment
        return view;
    }
}