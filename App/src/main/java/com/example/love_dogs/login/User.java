package com.example.love_dogs.login;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    public String uid;
    public String email;
    public String user_name;
    public String phone_number;
    public User(){

    }

    public User(String uid, String email, String user_name, String phone_number)
    {
        this.uid = uid;
        this.email = email;
        this.user_name=user_name;
        this.phone_number=phone_number;

    }

    public static User AddUser(FirebaseUser firebaseUser){

        return null;
    }

    public static FirebaseUser IsLoggedIn(AppCompatActivity context){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Toast.makeText(context.getApplicationContext(),"Logged out...",Toast.LENGTH_LONG);
            Intent loginPage = new Intent(context.getApplicationContext(), LogInActivity.class);
            context.startActivity(loginPage);
            return null;
        }
        return user;
    }


}
