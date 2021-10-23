package com.example.spacejuice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (user == null) { // user references the Member class for the user
            user = new Member(this, 0);
        }

        setContentView(R.layout.activity_main);

        Habit testHabit_1 = new Habit("Brush Teeth", "Prevent cavities");
        Habit testHabit_2 = new Habit("Walk Dog", "Exercise");

        user.ViewAllHabits(this);
    }

}