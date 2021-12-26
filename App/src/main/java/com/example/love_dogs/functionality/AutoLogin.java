package com.example.love_dogs.functionality;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.love_dogs.login.LogInActivity;
import com.example.love_dogs.login.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AutoLogin {
    public static final String login_db = "logged_in";
    public interface CallbackInfo{
        void callback(boolean success);
    }

    public static void tryAutoLogIn(AppCompatActivity context, CallbackInfo info){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            info.callback(false);
            return;
        }

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(login_db).child(user.getUid()).child(android_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    User.LoadCurrentUser(context);
                    info.callback(true);
                }else{
                    info.callback(false);
                }
            }
        });
    }

    public static void rememberMe(Context context){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(login_db).child(user.getUid()).child(android_id).setValue(true);
    }

    public static void logout(Context context){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(login_db).child(user.getUid()).child(android_id).removeValue();

        FirebaseAuth.getInstance().signOut();

        FragmentManager.ResetAll();

        Intent intent = new Intent(context, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //context.this.finish();
    }
}
