package com.example.spacejuice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.LoginController;
import com.example.spacejuice.controller.TimeController;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.Calendar;


public class AdminMenuActivity extends AppCompatActivity {
    /*
This Activity is used to access admin commands in an admin account
 */
    private TextView adminText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);
        Button nextDayButton = findViewById(R.id.IncrementDayAM);
        Button confirmButton = findViewById(R.id.confirmButtonAM);
        Button resetButton = findViewById(R.id.resetAccountAM);
        adminText = findViewById(R.id.adminMenuDateText);
        refresh();

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeController.adminIncrementDay();
                refresh();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPhoenix.triggerRebirth(AdminMenuActivity.this);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginController.adminResetAccount(MainActivity.getUser());
                refresh();
            }
        });

    }

    public void refresh() {
        Calendar fakeDay = TimeController.getCurrentTime();
        String dayOfWeek = TimeController.getDayOfWeek(fakeDay);
        String month = TimeController.getMonth(fakeDay);
        int day = fakeDay.get(Calendar.DAY_OF_MONTH);
        adminText.setText(dayOfWeek + ", " + month + " " + day);
    }

}
