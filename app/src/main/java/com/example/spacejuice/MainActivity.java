package com.example.spacejuice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.activity.WelcomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static Member user; // this is the Member instance for the app user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition( 0, 0);
        finish();
        overridePendingTransition( 0, 0);

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition( 0, 0);

        getUser().setScore(5);
        Log.d("debugInfo", "user score set to 5");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    public static void setUser(Member new_user){
        user = new_user;
        user.initTestData();
    }

    public void testFunction() {
        Log.d("debugInfo", "testFunction() was run.");
    }

}