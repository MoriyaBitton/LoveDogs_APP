package com.example.love_dogs.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.love_dogs.R;
import com.example.love_dogs.map_api.Map_Api_Init;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;


public class LogInActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private static MapView map;
    private static Button log;
    private static Button reg;
    private static Button test;
    private static EditText mail;
    private static EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register & login Buttons
        reg = findViewById(R.id.Reg);
        log = findViewById(R.id.Login);
        test = findViewById(R.id.Testing);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.Pass);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testIntent = new Intent(getApplicationContext(), Map_Api_Init.class);
                startActivity(testIntent);
                finish();
            }
        });


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail_str = mail.getText().toString();
                String pass_str = pass.getText().toString();
                mAuth = FirebaseAuth.getInstance();

                if (mail_str != null && pass_str!=null && Log_Utils.valid_email(mail_str)) {

                    mAuth.signInWithEmailAndPassword(mail_str,pass_str).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LogInActivity.this,"Logged In",Toast.LENGTH_LONG).show();
//                                Toast.makeText(LogInActivity.this,user.getUid()+" , "+user.getEmail() ,Toast.LENGTH_LONG).show();
                                //Intent home_page = new Intent(getApplicationContext(), Main_logged_in.class);
                                //startActivity(home_page);
                                User.LoadCurrentUser(LogInActivity.this);
                            }
                            else{
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //Toast.makeText(LogInActivity.this,task.getException().toString(),Toast.LENGTH_LONG);
                            }
                        }
                    });
                }

                else {
                    Toast.makeText(LogInActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_act = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(reg_act);

            }
        });
    }
}


