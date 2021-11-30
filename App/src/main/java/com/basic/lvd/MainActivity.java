package com.basic.lvd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //R.id.testKek
    }

    public void OnClick(View button){
        TextView text = findViewById(R.id.testKek);
        TextView name = findViewById(R.id.userName);
        text.setText("Hello, " + name.getText());
        button.setEnabled(false);
    }
}