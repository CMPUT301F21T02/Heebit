package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;

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
      setContentView(R.layout.my_habit_list);
      habitList = findViewById(R.id.my_habit_list);
      this.member = member.getInstance(this, R.layout.habit_content);
      habitList.setAdapter(habitAdapter);
   }

}