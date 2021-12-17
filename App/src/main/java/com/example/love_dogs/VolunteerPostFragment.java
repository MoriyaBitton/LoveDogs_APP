package com.example.love_dogs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        TextView title = view.findViewById(R.id.ptitle);
        title.setText(post.title);
        TextView location = view.findViewById(R.id.plocation);
        location.setText(post.location);
        TextView date = view.findViewById(R.id.pdate);
        date.setText(post.date);
        TextView body = view.findViewById(R.id.pbody);
        body.setText(post.body);
        TextView author = view.findViewById(R.id.pauthor);
        author.setText(post.author);

        Button back = view.findViewById(R.id.vpost_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressed();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void OnBackPressed() {
        container.removeView(post_layout);
        parent.setVisibility(View.VISIBLE);
        FragmentManager.latest = null;
    }
}