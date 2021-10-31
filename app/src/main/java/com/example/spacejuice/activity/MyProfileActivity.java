package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;

public class MyProfileActivity extends AppCompatActivity {
   /*
   This Activity is used to display my profile
    */
//    public view my_profile_activity;
   public Button go_to_requests;
   public ImageButton back_button;

  
   private Button exploreButton;
  
   public TextView user_name;

   private TextView followingCount;
   private TextView followingText;

    private TextView followersCount;
    private TextView followersText;

  


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");
       setContentView(R.layout.my_profile_activity);

       user_name = findViewById(R.id.userName);
       back_button = findViewById(R.id.backButtonMyProfile);

       exploreButton = findViewById(R.id.discoverButton);
       go_to_requests = findViewById(R.id.requestsButton);
       followingCount = findViewById(R.id.followingCount);
       followingText = findViewById(R.id.following);
       followersCount = findViewById(R.id.followersCount);
       followersText = findViewById(R.id.followers);

       user_name.setText(MainActivity.getUser().getMemberName());
       go_to_requests.setOnClickListener(new View.OnClickListener() {


         @Override
         public void onClick(View v) {
            openFollowRequestActivity();
         }

      });

      // back button
      back_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }

      });

      // Makes it so that if the user clicks on the following or following count, it goes to the list
      followingText.setOnClickListener(new View.OnClickListener(){
         @Override
         public void onClick(View v) {
            openFollowingListView();
         }
      });
      followingCount.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v) {
              openFollowingListView();
          }
      });

      // same for followers
       followersText.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               openFollowersListView();
           }
       });
       followersCount.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               openFollowersListView();
           }
       });

   }


   public void openFollowRequestActivity() {
      Intent intent = new Intent(this, FollowerRequestsActivity.class);
      startActivity(intent);
   }
   public void openFollowingListView() {
       // goes to following list view
      Intent intent = new Intent(this, FollowingActivity.class);
      startActivity(intent);
   }
    public void openFollowersListView() {
        // goes to follower list view
        Intent intent = new Intent(this, FollowersActivity.class);
        startActivity(intent);
    }
}
