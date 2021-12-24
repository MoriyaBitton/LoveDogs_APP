package com.example.love_dogs.volunteers;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.login.User;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VolunteerPostFragment extends FragmentExtended {
    private class FieldData{
        private VolunteerPost.RoleField field;
        private TextView type;
        private TextView num;
        private Button remove;
        private Button volunteer;

        public FieldData(View child_view, VolunteerPost.RoleField field){
            this.field  = field;
            type = child_view.findViewById(R.id.vvpf_type);
            num = child_view.findViewById(R.id.vvpf_num);

            remove = child_view.findViewById(R.id.vvpf_field_remove);
            volunteer = child_view.findViewById(R.id.vvpf_field_volunteer);
            update();

            remove.setOnClickListener(this::onCancelVolunteer);
            volunteer.setOnClickListener(this::onVolunteer);

            fieldMap.add(this);
        }

        public void update(){
            type.setText(field.type);
            num.setText(field.filled + "/" + field.required);

            if(field.filled == 0){
                remove.setVisibility(View.INVISIBLE);
            }else if(field.required <= field.filled){
                volunteer.setVisibility(View.INVISIBLE);
            }
        }

        void onCancelVolunteer(View view){
            field.filled--;
            volunteer.setVisibility(View.VISIBLE);
            update();
        }

        void onVolunteer(View view){
            field.filled++;
            remove.setVisibility(View.VISIBLE);
            update();
        }
    }

    private ArrayList<FieldData> fieldMap = new ArrayList<>();
    private VolunteerPost post;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public VolunteerPostFragment(View parent_view, VolunteerPost post){
        super(R.layout.fragment_volunteer_post, R.id.vpost_layout, parent_view);
        this.post = post;
    }


    @Override
    public void onCreateView(View view) {
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

        Button volunteer = view.findViewById(R.id.vvp_volunteer_button);
        volunteer.setOnClickListener(this::onVolunteerClicked);
    }

    public void onEdit(View view){
        FragmentExtended myFragment = new VolunteerPostEditkFragment(parent_view,post);
        swapFragments(myFragment);
    }

    public void onVolunteerClicked(View view){
        view.setVisibility(View.INVISIBLE);

        post.loadRoles(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                for (VolunteerPost.RoleField field: post.roles.values()) {
                    addRoleField(field);
                }
            }
        });
    }

    void addRoleField(VolunteerPost.RoleField field){
        LinearLayout layout = this.root_view.findViewById(R.id.vvp_volunteers_layout);
        LayoutInflater linear_layout_inflater =  getLayoutInflater();

        View child_view = linear_layout_inflater.inflate(R.layout.volunteer_field_view,null);
        layout.addView(child_view);

        FieldData fieldData = new FieldData(child_view, field);
    }
}