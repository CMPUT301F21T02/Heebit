package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.R;

import java.util.ArrayList;

// Going to work on this!
public class AllHabitsActivity extends AppCompatActivity {
   /*
   This Activity is used to view all of my habits
    */

   public ListView habitList;
   public static ArrayAdapter<Habit> habitAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "All My Habits View Created from AllHabitsActivity.java");
      setContentView(R.layout.my_habit_list);
      habitList = findViewById(R.id.list_of_my_habits);

      ArrayList<Habit> items = new ArrayList<>();

      /* We must find a way to store these Habits in such a way that they can be accessed from any
      activity. We want an accessible ArrayList<Habit> of a Member's habits.
       */

      items.add(new Habit("test habit #1", "reason #1", Habit.Indicator.EMPTY));
      items.add(new Habit("test habit #2", "reason #2", Habit.Indicator.BRONZE));
      items.add(new Habit("test habit #3", "reason #3", Habit.Indicator.SILVER));
      items.add(new Habit("test habit #4", "reason #4", Habit.Indicator.GOLD));

      habitList.setAdapter(new HabitListAdapter(this, R.layout.habit_content, items));


   }

}