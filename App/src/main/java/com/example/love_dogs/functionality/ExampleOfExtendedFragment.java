package com.example.love_dogs.functionality;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.FragmentExtended;
import com.example.love_dogs.volunteers.VolunteerPostEditkFragment;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ExampleOfExtendedFragment extends FragmentExtended {

    public ExampleOfExtendedFragment(View parent_view){
        super(R.layout.fragment_volunteer_post, R.id.vpost_layout, parent_view);
    }

    public ExampleOfExtendedFragment(int layout_xml_id, int layout_root_id, FragmentExtended other) {
        super(layout_xml_id, layout_root_id, other);
    }

    @Override
    public void OnCreateView(View view) {
        TextView text = view.findViewById(R.id.vvp_title);
        text.setText("just a test title");
    }

    void CreateThisFragmet(View parentView, Fragment other, ViewGroup container){
        // example of how you would create this fragment from semewhere else
        // you need to pass what is the "view" root, or something, and pass the current fragment.
        // you also need to know the container! ( with you get OnCreateView bla bla bla ).
        FragmentExtended myFragment = new ExampleOfExtendedFragment(parentView);
        myFragment.ShowFragment(other);
    }

    void CreateOtherFragemt(){
        // this is how you would create fragment that is also Extended Fragment.

        FragmentExtended myFragment = new ExampleOfExtendedFragment(parent_view);

        // destroy this fragment and set myFragment as new
        this.SwapFragments(myFragment);
        // or
        // hide this fragment, and set myFragment on top hence if we press back you get the old one.
        this.PutFragmentOnTop(myFragment);
        // do nothing with this one, and puy myFragment on top, with means you would see text one on another.
        myFragment.ShowFragment(this);
    }
}