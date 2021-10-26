package com.example.spacejuice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class AddHabitActivity extends AppCompatActivity {
   /*
   This Activity is used to add and enter the details of a new habit
    */
    private TextView title;
    private Button deleteB;
    private Button confirmB;
    private ImageButton backB;
    private EditText NameEdit;
    private String name;
    private EditText DescriptionEdit;
    private String description;
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_add_edit);

        title = findViewById(R.id.textViewHAE);
        title.setText("Add a Habit"); //Set the title into Add a Habit
        deleteB = findViewById(R.id.DeleteButtonHAE);
        deleteB.setVisibility(View.INVISIBLE); //Hide the delete button
        backB = findViewById(R.id.backButtonHAE);

        confirmB = findViewById(R.id.confirmButtonHAE);
        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameEdit = findViewById(R.id.HabitNameHAE);
                name = NameEdit.getText().toString();  //get the name
                DescriptionEdit = findViewById(R.id.HabitDescriptionHAE);
                description = DescriptionEdit.getText().toString(); //Get the Description
                Monday = findViewById(R.id.Monday);
                Tuesday = findViewById(R.id.Tuesday);
                Wednesday = findViewById(R.id.Wednesday);
                Thursday = findViewById(R.id.Thursday);
                Friday = findViewById(R.id.Friday);
                Saturday = findViewById(R.id.Saturday);
                Sunday = findViewById(R.id.Sunday);;
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              finish();
           }
        });


    }
}
