package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;

import java.util.ArrayList;

public class FollowerRequestsActivity extends AppCompatActivity {
   /*
   This Activity is used to display all my pending incoming follow requests
    */
   public ImageButton back_button;
   ListView followerList;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "Follower Requests Activity Created from FollowerRequestsActivity.java");
      setContentView(R.layout.follower_requests);
      back_button = findViewById(R.id.backButtonFollowerRequests);

      followerList = findViewById(R.id.followersList);

      ArrayList<Member> requestingMembers = new ArrayList<>();

      requestingMembers.add(new Member("Heeba", "12345678"));
      requestingMembers.add(new Member("Xuanhao", "12345678"));
      requestingMembers.add(new Member("Harish", "12345678"));
      requestingMembers.add(new Member("LemonJuice", "12345678"));
      requestingMembers.add(new Member("Yuchen", "12345678"));

      followerList.setAdapter(new FollowRequestAdapter(this, R.layout.follow_request_content, requestingMembers));

      back_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });
   }





}
