package com.basic.lvd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //R.id.testKek
    }

    public void OnClick(View button){
        if(!connected) {
            TextView text = findViewById(R.id.testKek);
            TextView name = findViewById(R.id.userName);
            text.setText("Hello, " + name.getText());
            //button.setEnabled(false);
            connected = true;
            Log.d("mylog", "hello," + name.getText());
        }else if(connected) {
            //Intent intent = new Intent(this, CreateEvent.class);
            TextView name = findViewById(R.id.userName);
            CharSequence username = name.getText();
            setContentView(R.layout.hello_user);
            TextView text = findViewById(R.id.welcomeText);
            text.setText("Hello, " + name.getText());
        }
    }
}