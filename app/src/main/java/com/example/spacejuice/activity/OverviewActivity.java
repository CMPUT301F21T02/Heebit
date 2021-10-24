package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class OverviewActivity extends AppCompatActivity {
   /*
   This Activity is used my main page which shows an overlay of today's habits
   and various menus
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.today_habits);

    }
}
