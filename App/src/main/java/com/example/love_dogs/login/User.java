package com.example.love_dogs.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.love_dogs.functionality.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class User {
    private static User current = null;
    public String uid = null;
    public String email;
    public String user_name;
    public String phone_number;
    public String organizationID;

    public User(){

    }

    public User(String uid, String email, String user_name, String phone_number)
    {
        this.uid = uid;
        this.email = email;
        this.user_name=user_name;
        this.phone_number=phone_number;
    }

    public void UpdateInDatabase(){
        if(uid == null){
            return;
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("/users/" + uid + "/user_name", user_name);
        result.put("/users/" + uid + "/phone_number", phone_number);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.updateChildren(result);
    }

    public static boolean LoadCurrentUser(AppCompatActivity context){
        FirebaseUser user = getFirebaseUser(context);
        if(user == null) {
            return false;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "User doesn't exist!", task.getException());
                    Intent intent = new Intent(context, UpdateUserActivity.class);
                    context.startActivity(intent);
                }
                else {
                    Map<String,String> td=(HashMap<String, String>)task.getResult().getValue();
                    if(td == null){
                        Log.e("firebase", "User doesn't exist!", task.getException());
                        Intent intent = new Intent(context, UpdateUserActivity.class);
                        context.startActivity(intent);
                        return;
                    }
                    User c_user = new User(td.get("uid"), td.get("email"), td.get("user_name"), td.get("phone_number"));
                    c_user.organizationID = td.get("organizationID");
                    current = c_user;
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    //listener.onComplete(null);

                }
            }
        });

        return true;
    }

    public static User AddUser(FirebaseUser firebaseUser, String user_name, String phone_number, OnSuccessListener<Void> listener){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), user_name, phone_number);
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user)
                .addOnSuccessListener(listener);

        FirebaseUser firebaseUserThis = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUserThis != null && firebaseUserThis.getUid().equals(firebaseUser.getUid())){
            current = user;
        }
        return user;
    }

    public static User getCurrentUser(AppCompatActivity context){
        FirebaseUser user = getFirebaseUser(context);
        if(user == null){
            return null;
        }

        if(current == null || !current.uid.equals(user.getUid())){
            LoadCurrentUser(context);
            return null;
        }
        return current;
    }

    public static User getCurrentRaw(){
        return current;
    }

    public static boolean isLoggedIn(AppCompatActivity context){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser != null;
    }

    public static FirebaseUser getFirebaseUser(AppCompatActivity context){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(context.getApplicationContext(),"Logged out...",Toast.LENGTH_LONG);
            Intent loginPage = new Intent(context.getApplicationContext(), LogInActivity.class);
            context.startActivity(loginPage);
            return null;
        }
        return firebaseUser;
    }


}
