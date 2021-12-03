package com.example.love_dogs.posts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.love_dogs.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    EditText date_time_in;
    EditText location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        setTitle("Create Event");
        date_time_in = findViewById(R.id.pdate);
        date_time_in.setInputType(InputType.TYPE_NULL);
        date_time_in.setFocusable(false);

        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

//        location = findViewById(R.id.paddress);
//        location.setInputType(InputType.TYPE_NULL);
//        location.setFocusable(false);
//
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDateTimeDialog(location);
//            }
//        });
    }

    private void showSetLocationDialog(EditText location){
    }

    private void showDateTimeDialog(EditText date_time_in){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(CreateEventActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(CreateEventActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
        Log.d("mylog", "done");
    }


    public void onPostButton(View button){
        Intent intent = new Intent(this, ViewEventActivity.class);
        Bundle bundle = new Bundle();

        TextView title = findViewById(R.id.ptitle);
        TextView date = findViewById(R.id.pdate);
        TextView location = findViewById(R.id.paddress);
        TextView context = findViewById(R.id.vcontext);

        bundle.putString("title", title.getText().toString());
        bundle.putString("date", date.getText().toString());
        bundle.putString("location", location.getText().toString());
        bundle.putString("context", context.getText().toString());

        //intent.putExtra("name", name.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}