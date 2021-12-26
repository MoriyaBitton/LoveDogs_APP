package com.example.love_dogs.functionality;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.love_dogs.R;
import com.example.love_dogs.volunteers.VolunteerBoard;
import com.example.love_dogs.volunteers.VolunteerPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class FirebaseGetList {
    public interface Callback <T>{
        void getList(ArrayList<T> items);
    }

    public interface SetCallback<T>{
        void getSet(Set<T> items);
    }

    public static <T> void getListOnce(Query getListQuery, @NonNull Class<T> targetClass, Callback<T> onLoaded) {
        // get data snapshot once!
        getListQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<T> target = new ArrayList<>();

                //Log.d("firebase", task.getResult().toString());
                for (DataSnapshot child : task.getResult().getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    T item = child.getValue(targetClass);
                    target.add(item);
                }
                onLoaded.getList(target);
            }
        });
    }

    public static <T> void getDataListOnChange(Query getListQuery, @NonNull Class<T> targetClass, Callback<T> onLoaded){
        // get query data on changes i.e continuously!
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

    public static void getKeySetOnce(Query getListQuery, SetCallback<String> onLoaded) {
        // get query data once
        getListQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashSet<String> target = new HashSet<>();
                for (DataSnapshot child : task.getResult().getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    target.add(child.getKey());
                }
                onLoaded.getSet(target);
            }
        });
    }

    public static void getKeySetOnChange(Query getListQuery, SetCallback<String> onLoaded) {
        // this will read data everytime there is a change on server side
        getListQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashSet<String> target = new HashSet<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    target.add(child.getKey());
                }
                onLoaded.getSet(target);
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
