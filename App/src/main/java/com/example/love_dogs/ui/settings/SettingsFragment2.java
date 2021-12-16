package com.example.love_dogs.ui.settings;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.example.love_dogs.R;

public class SettingsFragment2 extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackground(getResources().getDrawable(R.drawable.big_round));
        return view;
    }

//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        TypedValue typedValue = new TypedValue();
//        Resources.Theme theme = requireContext().getTheme();
//
//        theme.resolveAttribute(R.color.teal_200,typedValue,true);
//        int color = typedValue.data;
//
//        view.setBackgroundColor(color);
//
//        super.onViewCreated(view, savedInstanceState);
//    }
}