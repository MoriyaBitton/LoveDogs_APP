package com.example.love_dogs.functionality;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.love_dogs.R;
import com.example.love_dogs.volunteers.VolunteerBoard;
import com.example.love_dogs.volunteers.VolunteerPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Function;

public class FirebaseGetList {
    public interface Callback <T>{
        void getList(ArrayList<T> items);
    }

    public static <T> void getAll(Query getListQuery, @NonNull Class<T> targetClass, Callback<T> onLoaded) {
        getListQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> target = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    T item = child.getValue(targetClass);
                    target.add(item);
                }
                onLoaded.getList(target);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
