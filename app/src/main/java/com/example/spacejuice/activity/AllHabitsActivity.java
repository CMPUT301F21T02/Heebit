package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitListItem;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;

import java.util.ArrayList;

// Going to work on this!
public class AllHabitsActivity extends AppCompatActivity {
   /*
   This Activity is used to view all of my habits
    */

   public ListView habitList;
   public static ArrayAdapter<Habit> habitAdapter;
   public Member member;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "All My Habits View Created from AllHabitsActivity.java");
      setContentView(R.layout.my_habit_list);
      habitList = findViewById(R.id.list_of_my_habits);

      ArrayList<HabitListItem> items = new ArrayList<>();
      items.add(new HabitListItem("Text Habit #1", R.drawable.habit_empty));
      items.add(new HabitListItem("Test Habit #2", R.drawable.habit_bronze));
      items.add(new HabitListItem("Test Habit #3", R.drawable.habit_silver));
      items.add(new HabitListItem("Test Habit #4", R.drawable.habit_gold));

      habitList.setAdapter(new HabitListAdapter(this, R.layout.habit_content, items));


   }

}