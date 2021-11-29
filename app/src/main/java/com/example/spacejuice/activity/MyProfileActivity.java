package com.example.spacejuice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.FollowController;
import com.example.spacejuice.controller.LoginController;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity{
   /*
   This Activity is used to display my profile
    */
//    public view my_profile_activity;
   public Button go_to_requests;
   public ImageButton back_button;
  
   //private Button exploreButton;
  
   public TextView user_name;

   private TextView followingCount;
   private TextView followingText;

    public TextView followersCount;
    private TextView followersText;
    private CardView followerCard;
    private CardView followingCard;
    private CardView requestCard;
    private TextView requestCount;
    private CardView requestCountCard;
    private FollowController followController;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");

       setContentView(R.layout.my_profile_activity);

       user_name = findViewById(R.id.userName);
       back_button = findViewById(R.id.backButtonMyProfile);

       go_to_requests = findViewById(R.id.requestsButton);
       followingCount = findViewById(R.id.followingCount);
       followersCount = findViewById(R.id.followersCount);
       followerCard = findViewById(R.id.followerCard);
       followingCard = findViewById(R.id.followingCard);
       requestCard = findViewById(R.id.RequestCard);
       requestCount = findViewById(R.id.requestCount);
       requestCountCard = findViewById(R.id.RequestCountCard);

       followController = new FollowController();

        retrieveData();

       user_name.setText(MainActivity.getUser().getMemberName());
       requestCountCard.setOnClickListener(new View.OnClickListener() {
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
       followingCard.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               openFollowingListView();
           }
       });
       followerCard.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               openFollowersListView();
           }
       });
       go_to_requests.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               new RequestSendFragment().show(getSupportFragmentManager(), "RequestSendFragment ");
           }
       });


   }


   public void openFollowRequestActivity() {
      Intent intent = new Intent(this, FollowerRequestsActivity.class);
       launchSomeActivity.launch(intent);
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

    // Opens DiscoverActivity
    public void openDiscoverActivity() {
       Intent intent = new Intent(this, DiscoverActivity.class);
       startActivity(intent);
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        retrieveData();
                    }
                }
            });
   public void retrieveData(){
       final LoadingDialog loadingDialog = new LoadingDialog(MyProfileActivity.this);
       //loadingDialog.startLoadingAlertDialog();
       followController.getRequests(new LoginController.OnRequestCompleteCallback() {
           @Override
           public void onRequestComplete(boolean suc) {
               ArrayList<String> list = MainActivity.getUser().getFollow().getRequests();
               int count = list.size();
               String countString = "" + count;
               if (count > 0){
                   requestCard.setVisibility(View.VISIBLE);
                   requestCount.setText(countString);
               }
               else{
                   requestCard.setVisibility(View.INVISIBLE);
               }
           }
       });

       followController.getFollower(new LoginController.OnFollowerCompleteCallback() {
           @Override
           public void onFollowerComplete(boolean suc) {
               followersCount.setText(String.valueOf(MainActivity.getUser().getFollow().getFollowers().size()));

           }
       });

       followController.getFollowing(new LoginController.OnFollowingCompleteCallback() {
           @Override
           public void onFollowingComplete(boolean suc) {
               followingCount.setText(String.valueOf(MainActivity.getUser().getFollow().getFollowings().size()));
               //loadingDialog.dismissDialog();
           }
       });
   }
}
