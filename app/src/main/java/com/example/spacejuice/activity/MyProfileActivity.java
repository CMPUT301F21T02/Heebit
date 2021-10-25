package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class MyProfileActivity extends AppCompatActivity {
   /*
   This Activity is used to display my profile
    */
//    public view my_profile_activity;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");
       setContentView(R.layout.my_profile_activity);
//       profile_activity = findViewById(R.id.list_of_my_habits);
   }
   }
