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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register & login Buttons
        Button reg = findViewById(R.id.Reg);
        Button log = findViewById(R.id.Login);

        EditText mail = findViewById(R.id.mail);
        EditText pass = findViewById(R.id.Pass);


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
                                Toast.makeText(LogInActivity.this,"Logged In",Toast.LENGTH_LONG);
                                Toast.makeText(LogInActivity.this,user.getUid()+" , "+user.getEmail() ,Toast.LENGTH_LONG);
                                Intent home_page = new Intent(getApplicationContext(), Main_logged_in.class);
                                startActivity(home_page);
                            }
                            else Toast.makeText(LogInActivity.this,task.getException().toString(),Toast.LENGTH_LONG);
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


