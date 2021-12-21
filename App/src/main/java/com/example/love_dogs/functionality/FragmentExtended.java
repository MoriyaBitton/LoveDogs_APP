package com.example.love_dogs.functionality;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.love_dogs.R;
import com.example.love_dogs.volunteers.VolunteerPostFragment;

public abstract class FragmentExtended extends Fragment implements IFragmentBackable {

    private int layout_xml_id;
    private int root_id;
    protected View parent_view;
    protected ViewGroup container;
    protected View root_view;
    private boolean inQueue = false;

    public FragmentExtended(int layout_xml_id, int layout_root_id, View parent_view) {
        this.layout_xml_id = layout_xml_id;
        this.parent_view = parent_view;
        this.root_id = layout_root_id;
        // Required empty public constructor
    }

    public FragmentExtended(int layout_xml_id, int layout_root_id, FragmentExtended other){
        this(layout_xml_id, layout_root_id, other.parent_view);
    }

    public boolean IsInQueue(){
        return inQueue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout_xml_id, container, false);
        this.container = container;
        this.root_view = view.findViewById(root_id);
        FragmentManager.AddToStack(this);
        this.inQueue = true;
        OnCreateView(view);
        return view;
    }

    public abstract void OnCreateView(View view);

    @Override
    public void OnBackPressed() {
        container.removeView(this.root_view);
        parent_view.setVisibility(View.VISIBLE);
        FragmentManager.latest = null;
    }

    public void ShowFragment(Fragment parent){
        AppCompatActivity activity = (AppCompatActivity) parent.getContext();
        int id = ((ViewGroup)parent.getView().getParent()).getId();
        activity.getSupportFragmentManager().beginTransaction().replace(id, this).commit();
    }

    public void PutFragmentOnTop(FragmentExtended next){
        this.root_view.setVisibility(View.GONE);

        AppCompatActivity activity = (AppCompatActivity) this.root_view.getContext();
        int id = ((ViewGroup)this.root_view.getParent()).getId();
        activity.getSupportFragmentManager().beginTransaction().replace(id, next).commit();
    }

    public void SwapFragments(FragmentExtended next){
        AppCompatActivity activity = (AppCompatActivity) this.root_view.getContext();
        int id = ((ViewGroup)this.root_view.getParent()).getId();
        activity.getSupportFragmentManager().beginTransaction().replace(id, next).commit();

        this.container.removeView(this.root_view);
        FragmentManager.RemoveSelf(this);
    }
}
