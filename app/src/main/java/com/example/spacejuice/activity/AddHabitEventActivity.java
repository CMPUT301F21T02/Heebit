package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class AddHabitEventActivity extends AppCompatActivity {
      /*
   This Activity is used to add and enter the details of a new habit event
    */
      public ImageButton back_button;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");
         setContentView(R.layout.event_add);

         back_button = findViewById(R.id.backButtonEA);

         back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }

         });
      }
}
