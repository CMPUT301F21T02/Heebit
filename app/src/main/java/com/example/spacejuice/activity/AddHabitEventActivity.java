package com.example.spacejuice.activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitController;
import com.example.spacejuice.controller.HabitEventController;

import java.util.Date;

public class AddHabitEventActivity extends AppCompatActivity {
    /*
 This Activity is used to add and enter the details of a new habit event
  */
    public ImageButton back_button;
    public Button create_button;
    public EditText edit_text_description;
    public TextView habit_title_text;
    public TextView habit_reason_text;
    public TextView habit_date_completed;
    Habit currentHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");
        setContentView(R.layout.event_add);

        back_button = findViewById(R.id.backButtonEA);
        create_button = findViewById(R.id.createEventEA);
        edit_text_description = findViewById(R.id.eventDescriptionEA);
        habit_title_text = findViewById(R.id.habit_title_value);
        habit_reason_text = findViewById(R.id.habit_reason_value);
        habit_date_completed = findViewById(R.id.dateofcompletion);

        /*
            Get the Unique Identifier of the Habit that we are creating an event for
         */
        int habitUid;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                habitUid = 0;
            } else {
                habitUid = extras.getInt("habitUid");
            }
        } else {
            habitUid = (int) savedInstanceState.getSerializable("habitUid");
        }
        currentHabit = HabitController.getHabitFromUid(habitUid);
        Date date = new Date();

        /* date formatting retrieved from
        https://stackoverflow.com/questions/17192776/get-value-of-day-month-from-date-object-in-android
         */

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMMM",  date); // September
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        habit_date_completed.setText("on " + dayOfTheWeek + ", " + monthString + " " + day + ", " +
            year + ".");
        habit_title_text.setText(currentHabit.getTitle());
        habit_reason_text.setText(currentHabit.getReason());
        Log.d("debugInfo", "Habit title: " + currentHabit.getTitle() + "Habit reason: " + currentHabit.getReason());

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HabitEvent event = new HabitEvent();
                event.setDone(true);
                event.setDescription(edit_text_description.getText().toString());
                HabitEventController.addHabitEvent(currentHabit, event);
                finish();
            }
        });
    }
}

