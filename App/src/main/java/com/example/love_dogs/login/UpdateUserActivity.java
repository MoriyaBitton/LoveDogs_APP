package com.example.love_dogs.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.love_dogs.functionality.MainActivity;
import com.example.love_dogs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

public class UpdateUserActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    User user;
    TextView name;
    TextView phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        firebaseUser = User.getFirebaseUser(this);
        if(firebaseUser== null){
            return;
        }
        name = findViewById(R.id.auu_sname);
        phone = findViewById(R.id.auu_sphone);

        user = User.getCurrentRaw();
        if(user == null){
            Button button = findViewById(R.id.auu_back);
            button.setEnabled(false);
            return;
        }

        name.setText(user.user_name);
        phone.setText(user.phone_number);
    }

    public void OnConfirm(View button){
        if(name.getText().length() <= 1){
            Toast.makeText(UpdateUserActivity.this, "name should be at least 2 characters!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(phone.getText().length() != 10){
            Toast.makeText(UpdateUserActivity.this, "not valid phone number!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("firebase", "adding user or updating!");

        if(user != null){
            user.user_name = name.getText().toString();
            user.phone_number = phone.getText().toString();
        }

//        user = User.AddUser(firebaseUser, name.getText().toString(), phone.getText().toString(), "null",, new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Intent intent = new Intent(UpdateUserActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
////        User.current = user;
    }

    public void OnBack(View button){
        Intent intent = new Intent(UpdateUserActivity.this, MainActivity.class);
        startActivity(intent);
    }
}