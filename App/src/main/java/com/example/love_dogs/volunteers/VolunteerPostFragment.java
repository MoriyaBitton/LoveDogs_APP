package com.example.love_dogs.volunteers;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.login.User;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

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
            if(user.type == User.ORGANIZATION){
                volunteer.setVisibility(View.INVISIBLE);
                remove.setVisibility(View.INVISIBLE);
            }

            update();

            remove.setOnClickListener(this::onCancelVolunteer);
            volunteer.setOnClickListener(this::onVolunteer);

            fieldMap.add(this);
        }

        public void update(){
            Log.d("firebase", "updating... " + field.getNumSubscribed());
            type.setText(field.type);
            num.setText(field.getNumSubscribed() + "/" + field.required);

            if(user.type == User.ORGANIZATION){
                return;
            }

            if(field.subscribed.contains(user.uid)){
                volunteer.setVisibility(View.INVISIBLE);
                remove.setVisibility(View.VISIBLE);
            }else {
                remove.setVisibility(View.INVISIBLE);
                volunteer.setVisibility(View.VISIBLE);
            }

            if(field.required <= field.getNumSubscribed()){
                volunteer.setVisibility(View.INVISIBLE);
            }
        }

        void onCancelVolunteer(View view){
            field.removeVolunteer(user.uid);
            update();
        }

        void onVolunteer(View view){
            field.addVolunteer(user.uid);
            update();
        }
    }

    private ArrayList<FieldData> fieldMap = new ArrayList<>();
    private VolunteerPost post;
    private User user;
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
        user = User.getCurrentRaw();

        View change_view = view.findViewById(R.id.vvp_change);
        if(user.uid.equals(post.authorId) || user.type == User.ADMIN){
            Button edit = view.findViewById(R.id.vvp_edit);
            Button delete = view.findViewById(R.id.vvp_delete);
            change_view.setVisibility(View.VISIBLE);
            edit.setOnClickListener(this::onEdit);
            delete.setOnClickListener(this::onDelete);
        }else{
            change_view.setVisibility(View.GONE);
        }

        Button volunteer = view.findViewById(R.id.vvp_volunteer_button);
        volunteer.setOnClickListener(this::onVolunteerClicked);
    }

    public void onEdit(View view){
        FragmentExtended myFragment = new VolunteerPostEditkFragment(parent_view,post);
        swapFragments(myFragment);
    }

    public void onDelete(View view){
        if(post.delete(user)) {
            FragmentManager.GoToRoot(getActivity());
        }
    }

    public void onVolunteerClicked(View view){
        Log.d("firebase", "clicked volunteer button");
        //view.setVisibility(View.GONE);

        View pview = root_view.findViewById(R.id.vvp_volunteer_blayout);
        pview.setVisibility(View.GONE);

        post.loadRoles(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("firebase", "add roles...");
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

        FieldData fieldData = new FieldData(child_view, field);
        field.loadSubscribed(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                fieldData.update();
                layout.addView(child_view);
            }
        });
    }
}