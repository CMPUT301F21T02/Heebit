package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class MyProfileActivity extends AppCompatActivity {
   /*
   This Activity is used to display my profile
    */
//    public view my_profile_activity;
   public Button go_to_requests;
   public ImageButton back_button;
   private Button exploreButton;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");
       setContentView(R.layout.my_profile_activity);

       back_button = findViewById(R.id.backButtonMyProfile);
      go_to_requests = findViewById(R.id.requestsButton);
       exploreButton = findViewById(R.id.discoverButton);

      // requests button
      go_to_requests.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            openFollowersActivity();
         }

      });

      // back button
      back_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }

      });

      exploreButton.setOnClickListener(new View.OnClickListener(){
         @Override
         public void onClick(View v) {
            openFollowingListView();
         }
      });

   }


   public void openFollowersActivity() {
      Intent intent = new Intent(this, FollowerRequestsActivity.class);
      startActivity(intent);
   }
   public void openFollowingListView() {
      Intent intent = new Intent(this, FollowingActivity.class);
      startActivity(intent);
   }
}
