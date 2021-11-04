package com.example.spacejuice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class MemberProfileActivity extends AppCompatActivity {
   /*
   This Activity is used to view another member's profile (that I am following)
    */
   private ImageButton backButton;
   @Override
   protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
       setContentView(R.layout.member_profile_activity);

       backButton = findViewById(R.id.backButtonMPA);

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
   }
}
