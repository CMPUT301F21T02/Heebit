package com.example.spacejuice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitListAdapter;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitController;

import java.util.ArrayList;

// Going to work on this!
public class AllHabitsActivity extends AppCompatActivity {
   /*
   This Activity is used to view all of my habits
    */

   public ListView habitList;
   public ArrayAdapter<Habit> habitAdapter;
   public Context context;
   Button today_habits_button;
   ImageButton profile_imagebutton;
   ImageButton add_habit_imagebutton;
   public ArrayList<Habit> habitListItems;
   ActivityResultLauncher<Intent> editLaunch;

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

      editLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
              new ActivityResultCallback<ActivityResult>() {
                 @Override
                 public void onActivityResult(ActivityResult result) {
                    Log.d("debugInfo", "result code: " + result.getResultCode());

                    habitAdapter.notifyDataSetChanged();
                 }
              });

      profile_imagebutton = findViewById(R.id.today_button_profile);
      add_habit_imagebutton = findViewById(R.id.today_button_add_habit);
      today_habits_button = (Button) findViewById(R.id.today_habits_button);

      refreshData();
      habitAdapter = new HabitListAdapter(this, R.layout.habit_content, habitListItems);
      habitList.setAdapter(habitAdapter);

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

   public void launchAddHabit() {
      Intent intent = new Intent(AllHabitsActivity.this, AddHabitActivity.class);
      editLaunch.launch(intent);
   }

   public void launchEditHabit(int clickedUid) {
      Intent intent = new Intent(AllHabitsActivity.this, EditHabitActivity.class);
      intent.putExtra("habitUid", clickedUid);
      editLaunch.launch(intent);
   }

   public void refreshData() {
      /* updates the list of Habits */
      habitListItems = HabitController.getHabitListItems();
      if(habitAdapter != null) {
         habitAdapter.notifyDataSetChanged();
      }
   }

}