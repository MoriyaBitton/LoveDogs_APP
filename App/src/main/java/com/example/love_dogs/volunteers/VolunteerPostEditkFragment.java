package com.example.love_dogs.volunteers;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.functionality.GetDateTime;
import com.example.love_dogs.login.User;
import com.example.love_dogs.posts.LDPost;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VolunteerPostEditkFragment extends FragmentExtended {

    boolean new_post = true;

    TextView title;
    TextView location;
    EditText date;
    TextView body;

    public VolunteerPostEditkFragment(View parent_view, boolean new_post){
        super(R.layout.fragment_volunteer_post_editk, R.id.vve_post_layout, parent_view);
        this.new_post = new_post;
    }

    public VolunteerPostEditkFragment(int layout_xml_id, int layout_root_id, FragmentExtended other) {
        super(layout_xml_id, layout_root_id, other);
    }

    @Override
    public void onCreateView(View view) {
        title = view.findViewById(R.id.vvep_title);
        location = view.findViewById(R.id.vvep_location);
        date = view.findViewById(R.id.vvep_time);
        body = view.findViewById(R.id.vvep_body);
        //TextView author = view.findViewById(R.id.vvep_username);

        Button update_button = view.findViewById(R.id.vvep_update);
        if(!new_post){
            LDPost post = LDPost.current;
            //author.setText(post.author);
            body.setText(post.body);
            date.setText(post.date);
            location.setText(post.location);
            title.setText(post.title);
        }else{
            update_button.setText("Post");
        }

        date.setInputType(InputType.TYPE_NULL);
        date.setFocusable(false);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDateTime.showDateTimeDialog(date, getActivity());
            }
        });

        Button back = view.findViewById(R.id.vvep_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        update_button.setOnClickListener(this::onUpdateClicked);
    }

    void onUpdateClicked(View view){
        User user = User.getCurrentRaw();
        LDPost newPost = new LDPost(title.getText().toString(), user.user_name, user.uid, date.getText().toString(),
                location.getText().toString(), body.getText().toString());
        if(new_post){
            newPost.push();
        }else{
            LDPost post = LDPost.current;
            newPost.pid = post.pid;
            newPost.updatePost();
        }

        FragmentManager.GoToRoot(getActivity());
    }
}