package com.example.love_dogs.volunteers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.functionality.IFragmentBackable;
import com.example.love_dogs.login.User;
import com.example.love_dogs.posts.LDPost;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VolunteerPostFragment extends Fragment implements IFragmentBackable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View parent;
    private View post_layout;
    private ViewGroup container;

    public VolunteerPostFragment() {
        // Required empty public constructor
    }

    public VolunteerPostFragment(View parent){
        this.parent = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volunteer_post, container, false);

        this.container = container;
        this.post_layout = view.findViewById(R.id.vpost_layout);

        LDPost post = LDPost.current;
        FragmentManager.latest = this;

        TextView title = view.findViewById(R.id.vvp_title);
        title.setText(post.title);
        TextView location = view.findViewById(R.id.vvp_location);
        location.setText(post.location);
        TextView date = view.findViewById(R.id.vvp_time);
        date.setText(post.date);
        TextView body = view.findViewById(R.id.vvp_body);
        body.setText(post.body);
        TextView author = view.findViewById(R.id.vvp_username);
        author.setText(post.author);

        Button back = view.findViewById(R.id.vvp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressed();
            }
        });

        User user = User.getCurrentRaw();
       Button edit = view.findViewById(R.id.vvp_edit);
        if(user.uid.equals(post.authorId)){
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(this::OnEdit);
        }else{
            edit.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void OnBackPressed() {
        container.removeView(post_layout);
        parent.setVisibility(View.VISIBLE);
        FragmentManager.latest = null;
    }

    public void OnEdit(View view){
        OnBackPressed();
    }
}