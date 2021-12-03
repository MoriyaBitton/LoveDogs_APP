package com.example.love_dogs.posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.love_dogs.R;

public class ViewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        TextView text = findViewById(R.id.vtitle);
        text.setText(bundle.getString("title"));
        TextView location = findViewById(R.id.vlocation);
        location.setText(bundle.getString("location"));
        TextView date = findViewById(R.id.vdate);
        date.setText(bundle.getString("date"));
        TextView context = findViewById(R.id.vcontext);
        context.setText(bundle.getString("context"));
    }

    public void backButton(View view){
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }
}