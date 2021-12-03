package com.example.love_dogs.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.google.firebase.auth.FirebaseUser;

public class Main_logged_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged_in);
        FirebaseUser user = User.IsLoggedIn(this);
        TextView text = findViewById(R.id.uhello);
        text.setText("hello - " + user.getEmail());
    }
}