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
import com.example.spacejuice.R;

import java.util.ArrayList;

// Going to work on this!
public class AllHabitsActivity extends AppCompatActivity {
   /*
   This Activity is used to view all of my habits
    */

   public ListView habitList;
   public static ArrayAdapter<Habit> habitAdapter;
   public Context context;
   Button today_habits_button;
   ImageButton profile_imagebutton;
   ImageButton add_habit_imagebutton;

   public AllHabitsActivity() {

   }

   public AllHabitsActivity(Context mContext) {
      Log.d("debugInfo", "context constructor was run");
      context = mContext;
   }


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "All My Habits View Created from AllHabitsActivity.java");
      Log.d("debugInfo", "Context: " + context);
      setContentView(R.layout.my_habit_list);
      habitList = findViewById(R.id.list_of_my_habits);

      profile_imagebutton = findViewById(R.id.today_button_profile);
      add_habit_imagebutton = findViewById(R.id.today_button_add_habit);
      today_habits_button = (Button) findViewById(R.id.today_habits_button);

      ArrayList<Habit> items = new ArrayList<>();

      /* We must find a way to store these Habits in such a way that they can be accessed from any
      activity. We want an accessible ArrayList<Habit> of a Member's habits.
       */

      items.add(new Habit("test habit #01", "reason #01", 0));
      items.add(new Habit("test habit #02", "reason #02", 1));
      items.add(new Habit("test habit #03", "reason #03", 2));
      items.add(new Habit("test habit #04", "reason #04", 3));
      items.add(new Habit("test habit #05", "reason #05", 4));
      items.add(new Habit("test habit #06", "reason #06", 5));
      items.add(new Habit("test habit #07", "reason #07", 6));
      items.add(new Habit("test habit #08", "reason #08", 7));
      items.add(new Habit("test habit #09", "reason #09", 8));
      items.add(new Habit("test habit #10", "reason #10", 9));
      habitList.setAdapter(new HabitListAdapter(this, R.layout.habit_content, items));

      today_habits_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            openOverviewActivity();
         }
      });

      add_habit_imagebutton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(AllHabitsActivity.this, AddHabitActivity.class);
            startActivity(intent);
         }
      });

      profile_imagebutton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(AllHabitsActivity.this, MyProfileActivity.class);
            startActivity(intent);
         }
      });

   }

   public void openOverviewActivity() {
      Intent intent = new Intent(this, OverviewActivity.class);
      startActivity(intent);
   }

}