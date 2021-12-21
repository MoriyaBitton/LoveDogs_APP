package com.example.love_dogs.volunteers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.functionality.FragmentManager;
import com.example.love_dogs.functionality.IFragmentBackable;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VolunteerPostEditkFragment extends FragmentExtended {

    public VolunteerPostEditkFragment(View parent_view){
        super(R.layout.fragment_volunteer_post_editk, R.id.vv_edit_post_layout, parent_view);
    }

    public VolunteerPostEditkFragment(int layout_xml_id, int layout_root_id, FragmentExtended other) {
        super(layout_xml_id, layout_root_id, other);
    }

    @Override
    public void OnCreateView(View view) {
        TextView text = view.findViewById(R.id.vvep_text);
        text.setText("something");
    }
}