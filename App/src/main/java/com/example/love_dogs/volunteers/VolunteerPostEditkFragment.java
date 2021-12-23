package com.example.love_dogs.volunteers;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FieldCheck;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.functionality.GetDateTime;
import com.example.love_dogs.login.User;
import com.example.love_dogs.posts.LDPost;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VolunteerPostEditkFragment extends FragmentExtended {
    ArrayList<View> roles = new ArrayList<>();
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
            Date initial_date = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(initial_date);
            cal.add(Calendar.DATE, 7); //minus number would decrement the days
            date.setText(LDPost.simpleDateFormat.format(cal.getTime()));
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

        Button add_role = view.findViewById(R.id.vvep_add_role);
        add_role.setOnClickListener(this::onAddRoleClicked);
        update_button.setOnClickListener(this::onUpdateClicked);

        roles.add(view.findViewById(R.id.vvep_role_1));


        for (View role_field_view: roles) {
            addRemoveButton(role_field_view);
        }
    }

    void onUpdateClicked(View view){
        if(!FieldCheck.checkNonIsEmpty(
                new TextView[]{title, date, location, body},
                new String[]{"title", "date", "location", "body" })){
            return;
        }
        Log.d("firebase", "all fields non empty generating new post...");

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

    void addRemoveButton(View field_view){
        Button removeView = field_view.findViewById(R.id.vvep_field_remove);
        removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                onDeleteField(field_view);
            }
        });
    }

    void onAddRoleClicked(View view){
        LinearLayout layout = this.root_view.findViewById(R.id.vvep_roles);
        LayoutInflater linear_layout_inflater =  getLayoutInflater();

        View child_view = linear_layout_inflater.inflate(R.layout.volunteer_field,null);
        layout.addView(child_view);

        roles.add(child_view);
        addRemoveButton(child_view);
    }

    void onDeleteField(View parent){
        if(roles.size() > 1){
            roles.remove(parent);
            ((ViewGroup) parent.getParent()).removeView(parent);
        }else{
            CharSequence text = "Post must have at least 1 role!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
        }
    }
}