package com.example.spacejuice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.OverviewActivity;

public class MainActivity extends AppCompatActivity {
    public Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (user == null) { // user references the Member class for the user
            user = new Member(this, R.layout.activity_main);
        }*/

        setContentView(R.layout.activity_main);

        Habit testHabit_1 = new Habit("Brush Teeth", "Prevent cavities");
        Habit testHabit_2 = new Habit("Walk Dog", "Exercise");

        // Feel free to change to whatever but setting to MainActivity makes it super buggy lmfao - harish
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }


    public Member getUser() {
        // Getter to get the Member class for the User
        return user;
    }

}