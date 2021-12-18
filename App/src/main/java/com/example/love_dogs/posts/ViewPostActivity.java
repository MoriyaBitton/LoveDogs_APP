package com.example.love_dogs.posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.love_dogs.R;
import com.example.love_dogs.login.User;

public class ViewPostActivity extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getCurrentUser(this);
        if(user == null){
            return;
        }
        setContentView(R.layout.fragment_volunteer_post);

        LDPost post = LDPost.current;

        TextView title = findViewById(R.id.ptitle);
        title.setText(post.title);
        TextView location = findViewById(R.id.plocation);
        location.setText(post.location);
        TextView date = findViewById(R.id.pdate);
        date.setText(post.date);
        TextView body = findViewById(R.id.dp_body);
        body.setText(post.body);
        TextView author = findViewById(R.id.pauthor);
        author.setText(post.author);
    }

    public void backButton(View view){
        Intent intent = new Intent(this, ViewPostsActivity.class);
        startActivity(intent);
    }
}