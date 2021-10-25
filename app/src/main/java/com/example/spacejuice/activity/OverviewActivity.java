package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class OverviewActivity extends AppCompatActivity {

    Button go_to_all_habits_button;
    Button go_to_add_habit_button;
    ImageView go_to_profile_imageView;
   /*
   This Activity is used my main page which shows an overlay of today's habits
   and various menus
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_habits);

        go_to_all_habits_button = (Button) findViewById(R.id.all_habits_button);



        go_to_all_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllHabitsActivity();
            }
        });


    }

    public void openAllHabitsActivity() {
        Intent intent = new Intent(this, AllHabitsActivity.class);
        startActivity(intent);
    }
}
