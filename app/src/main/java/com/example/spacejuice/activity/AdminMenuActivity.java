package com.example.spacejuice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.TimeController;
import com.jakewharton.processphoenix.ProcessPhoenix;


public class AdminMenuActivity extends AppCompatActivity {
    /*
This Activity is used to access admin commands in an admin account
 */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);
        Button nextDayButton = findViewById(R.id.IncrementDayAM);
        Button confirmButton = findViewById(R.id.confirmButtonAM);

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeController.adminIncrementDay();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPhoenix.triggerRebirth(AdminMenuActivity.this);
            }
        });

    }

}
