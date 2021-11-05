package com.example.spacejuice.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.FollowController;
import com.example.spacejuice.controller.HabitController;
import com.example.spacejuice.controller.LoginController;

import java.util.ArrayList;

public class FollowerRequestsActivity extends AppCompatActivity {
   /*
   This Activity is used to display all my pending incoming follow requests
    */
   private ImageButton back_button;
   private ListView followerList;
   private FollowRequestAdapter followRequestAdapter;
   private ArrayList<Member> requestingMembers;
   private EditText requestName;
   private Button send;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "Follower Requests Activity Created from FollowerRequestsActivity.java");
      setContentView(R.layout.follower_requests);
      back_button = findViewById(R.id.backButtonFollowerRequests);

      followerList = findViewById(R.id.followersList);

      requestingMembers = new ArrayList<>();

      requestingMembers.add(new Member("Heeba"));
      requestingMembers.add(new Member("Xuanhao"));
      requestingMembers.add(new Member("Harish"));
      requestingMembers.add(new Member("LemonJuice"));
      requestingMembers.add(new Member("Yuchen"));

      followRequestAdapter = new FollowRequestAdapter(this, R.layout.follow_request_content, requestingMembers);
      followerList.setAdapter(followRequestAdapter);

      back_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });
      requestName = findViewById(R.id.requestingName);
      send = findViewById(R.id.sendRequestButton);
      send.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            FollowController followController = new FollowController();
            followController.sendRequest(requestName.getText().toString(), new LoginController.OnCompleteCallback() {
               @Override
               public void onComplete(boolean suc) {
                  //MainActivity.setUser(member);
                  if (suc) {
                     Toast.makeText(FollowerRequestsActivity.this, "Sent request successfully!", Toast.LENGTH_SHORT).show();
                  } else {
                     Toast.makeText(FollowerRequestsActivity.this, "No such user exists!",
                             Toast.LENGTH_SHORT).show();
                  }
               }
            });

         }
      });
   }

   public void acceptFollowRequest(int position) {

      Context context = getApplicationContext();
      String memName = requestingMembers.get(position).getMemberName();
      CharSequence text = memName + " is now following you";
      int duration = Toast.LENGTH_SHORT;
      MediaPlayer song = MediaPlayer.create(context, R.raw.pop);
      song.start();

      Toast toast = Toast.makeText(context, text, duration);
      toast.show();
      requestingMembers.remove(position);
      Log.d("debugInfo", "acceptFollowRequest run at position " + position);
      followRequestAdapter.notifyDataSetChanged();

   }

   public void denyFollowRequest(int position) {
      requestingMembers.remove(position);
      Context context = getApplicationContext();

      MediaPlayer song = MediaPlayer.create(context, R.raw.pop2);
      song.start();

      Log.d("debugInfo", "denyFollowRequest run at position " + position);
      followRequestAdapter.notifyDataSetChanged();
   }





}
