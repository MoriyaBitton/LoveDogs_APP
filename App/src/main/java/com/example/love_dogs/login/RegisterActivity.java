package com.example.love_dogs.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        EditText pass , pass2 , phone , email;
        Button register;

        pass = findViewById(R.id.Password);
        pass2 = findViewById(R.id.Password2);
        phone = findViewById(R.id.Phone_Reg);
        email = findViewById(R.id.Email_Reg);
        register = findViewById(R.id.Reg_Register);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass_str = pass.getText().toString();
                String pass_str2 = pass2.getText().toString();
                String email_str = email.getText().toString();
                boolean checked = true;


                if(!(pass_str.equals(pass_str2)))
                {
                    pass2.setError("Passwords Must Match");
                    checked=false;
                }

                if(!Log_Utils.valid_pass(pass_str)){
                    pass.setError("Password must contain 1 lowercase letter 1 uppercase letter and 1 number");
                    checked=false;
                }


                if(!(Log_Utils.valid_email(email_str))){
                    email.setError("Invalid Email Address");
                    checked=false;
                }

                if(checked){
                    Toast.makeText(RegisterActivity.this,"Registered Successfully.", Toast.LENGTH_SHORT);

                    mAuth.createUserWithEmailAndPassword(email_str, pass_str)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("RegisterActivity", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent home_page = new Intent(getApplicationContext(), Main_logged_in.class);
                                        startActivity(home_page);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                    register.setText("Registered Successfully");
                }
            }
        });
    }
}