package com.example.love_dogs.volunteers;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.login.User;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VolunteerPostFragment extends FragmentExtended {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public VolunteerPostFragment(View parent_view){
        super(R.layout.fragment_volunteer_post, R.id.vpost_layout, parent_view);
    }


    @Override
    public void onCreateView(View view) {
        VolunteerPost post = VolunteerPost.current;

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
                onBackPressed();
            }
        });

        User user = User.getCurrentRaw();
       Button edit = view.findViewById(R.id.vvp_edit);
        if(user.uid.equals(post.authorId)){
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(this::onEdit);
        }else{
            edit.setVisibility(View.GONE);
        }
    }

    public void onEdit(View view){
        FragmentExtended myFragment = new VolunteerPostEditkFragment(parent_view,false);
        swapFragments(myFragment);
    }
}