package com.example.love_dogs.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.love_dogs.R;
import com.example.love_dogs.functionality.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        EditText pass , pass2 , phone , email,username,address;
        Button register;
        LinearLayout regLayout = (LinearLayout) findViewById(R.id.user_type);
        RadioGroup userType;

        pass = findViewById(R.id.Password);
        pass2 = findViewById(R.id.Password2);
        email = findViewById(R.id.Email_Reg);
        register = findViewById(R.id.Reg_Register);
        phone = findViewById(R.id.phone_reg);
        username = findViewById(R.id.user_comp_name_reg);
        userType = findViewById(R.id.user_type_group);
        address = findViewById(R.id.address_reg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_type = User.USER;
                String pass_str = pass.getText().toString();
                String pass_str2 = pass2.getText().toString();
                String email_str = email.getText().toString();
                String user_str = username.getText().toString();

                if(userType.getCheckedRadioButtonId()==R.id.radio_comp){
                    user_type = User.ORGANIZATION;
                }
                boolean checked = true;


                if(!(pass_str.equals(pass_str2)))
                {
                    pass2.setError("Passwords Must Match");
                    checked=false;
                }

                if(!Log_Utils.valid_pass(pass_str)){
                    pass.setError("Password must contain at list 1 lowercase letter, 1 uppercase letter and 1 number, and 8 characters total");
                    checked=false;
                }

                if(!(Log_Utils.valid_email(email_str))){
                    email.setError("Invalid Email Address");
                    checked=false;
                }

                if(user_str.length() <= 1){
                    username.setError("Username must be at least 2 characters");
                }

                if(checked){
                    int finalUser_type = user_type;
                    mAuth.createUserWithEmailAndPassword(email_str, pass_str)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("RegisterActivity", "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this,"Registered Successfully.", Toast.LENGTH_LONG);
                                        FirebaseUser fire_user = mAuth.getCurrentUser();
                                        User user = User.AddUser(fire_user, user_str, phone.getText().toString(),
                                                address.getText().toString(), finalUser_type, new OnSuccessListener<Void>() {

                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterActivity.this, "Registered Successfully,Moved to login page",
                                                        Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}