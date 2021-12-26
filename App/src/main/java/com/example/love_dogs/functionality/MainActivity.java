package com.example.love_dogs.functionality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.love_dogs.R;
import com.example.love_dogs.login.UpdateUserActivity;
import com.example.love_dogs.login.User;
import com.example.love_dogs.posts.CreatePostActivity;
import com.example.love_dogs.posts.ViewPostsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_logged_in);
        setContentView(R.layout.appnav_start);
        User user = User.getCurrentUser(this);
        if(user == null){
            return;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_app_bottom_nav);
        NavController navController = Navigation.findNavController(this,  R.id.fragment_main_app);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        View logout_item = findViewById(R.id.menu_logout);
        logout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoLogin.logout(MainActivity.this);
            }
        });
        //Log.w("firebase", "number of posts : ");

        //TextView text = findViewById(R.id.uhello);
        //Log.d("firebase", "user missing ? " + (user == null));
//        text.setText("Hello, " + user.user_name + "!");

        //User.AddUser(user, "mika", "0543210123");
    }

    @Override
    public void onBackPressed() {
        // new Way
        if(FragmentManager.BackOnce()){
            return;
        }

        // Old way
        IFragmentBackable latest = FragmentManager.latest;
        if (latest == null) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            super.onBackPressed();
            return;
        }
        latest.onBackPressed();
    }

    public void OnCreatePost(View view){
        Intent intent = new Intent(MainActivity.this, CreatePostActivity.class);
        startActivity(intent);
    }

    public void ViewPosts(View view){
        Intent intent = new Intent(MainActivity.this, ViewPostsActivity.class);
        startActivity(intent);
    }

    public void UpdateProfile(View view){
        Intent intent = new Intent(MainActivity.this, UpdateUserActivity.class);
        startActivity(intent);
    }
}