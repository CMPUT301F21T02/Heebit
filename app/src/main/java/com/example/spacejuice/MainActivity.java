package com.example.spacejuice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.OverviewActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static Member user; // this is the Member instance for the app user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Habit testHabit_1 = new Habit("Brush Teeth", "Prevent cavities", new Date(), new Schedule());
        Habit testHabit_2 = new Habit("Walk Dog", "Exercise", new Date(), new Schedule());

        // Feel free to change to whatever but setting to MainActivity makes it super buggy lmfao - harish
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
        getUser().setScore(5);
        Log.d("debugInfo", "user score set to 5");

    }

    /*
        Singleton Support for Member
    */
    public static Member getUser() {
        // Getter to get the Member class for the User
        if (user == null) {
            user = new Member();
            user.initTestData();
        }
        return user;
    }

    public void testFunction() {
        Log.d("debugInfo", "testFunction() was run.");
    }

}