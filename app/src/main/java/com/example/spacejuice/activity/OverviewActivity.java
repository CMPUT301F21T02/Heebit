package com.example.spacejuice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitListAdapter;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitController;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    Button go_to_all_habits_button;
    ImageButton profile_imagebutton;
    ImageButton add_habit_imagebutton;
    /*
    This Activity is used my main page which shows an overlay of today's habits
    and various menus
     */
    public ListView today_habit_list;
    public static ArrayAdapter<Habit> habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_habits);

        profile_imagebutton = findViewById(R.id.profile_imagebutton);
        add_habit_imagebutton = findViewById(R.id.add_habit_imagebutton);
        go_to_all_habits_button = (Button) findViewById(R.id.all_habits_button);

        today_habit_list = findViewById(R.id.overview_habit_listview);
        ArrayList<Habit> today_habit_items = new ArrayList<>();

        Habit each;

        ArrayList<Habit> habitListItems;
        habitListItems = HabitController.getHabitListItems();

        for (int i = 0; i < habitListItems.size(); i += 1) {
            if (habitListItems.get(i).isToday()) {
                today_habit_items.add(habitListItems.get(i));
            }
        }

        today_habit_list.setAdapter(new HabitListAdapter(this, R.layout.overview_habit_content,today_habit_items ));

        int score = MainActivity.getUser().getScore();
        Log.d("debugInfo", "From OverviewActivity - user score = " + score);

        go_to_all_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllHabitsActivity();
            }
        });

        add_habit_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OverviewActivity.this, AddHabitActivity.class);
                startActivity(intent);
            }
        });
        // We apply a clicklistener to the imageView
        // and then once it is clicked, we change it to another activity
        profile_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });


    }


    public void openAllHabitsActivity() {
        Intent intent = new Intent(this, AllHabitsActivity.class);
        startActivity(intent);
    }
}
