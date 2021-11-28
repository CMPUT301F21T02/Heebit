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
   private FollowController followController;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "Follower Requests Activity Created from FollowerRequestsActivity.java");
      setContentView(R.layout.follower_requests);
      back_button = findViewById(R.id.backButtonFollowerRequests);

      followerList = findViewById(R.id.followersList);
      followController = new FollowController();
      requestingMembers = new ArrayList<>();
      followController.getRequests(new LoginController.OnRequestCompleteCallback() {
         @Override
         public void onRequestComplete(boolean suc) {
            if (suc){
               ArrayList<String> list = MainActivity.getUser().getFollow().getRequests();
               for (int i = 0; i < list.size(); i++){
                  requestingMembers.add(new Member(list.get(i)));
               }
               followRequestAdapter = new FollowRequestAdapter(FollowerRequestsActivity.this, R.layout.follow_request_content, requestingMembers);
               followerList.setAdapter(followRequestAdapter);
            }
            else{
               followRequestAdapter = new FollowRequestAdapter(FollowerRequestsActivity.this, R.layout.follow_request_content, requestingMembers);
               followerList.setAdapter(followRequestAdapter);
            }
         }
      });


      followRequestAdapter = new FollowRequestAdapter(this, R.layout.follow_request_content, requestingMembers);
      followerList.setAdapter(followRequestAdapter);

      back_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            setResult(RESULT_OK, null);
            finish();
         }
      });

   }

   public void acceptFollowRequest(int position) {

      Context context = getApplicationContext();
      String memName = requestingMembers.get(position).getMemberName();
      followController.responseRequest(memName, true, new LoginController.OnResponseCallback() {
         @Override
         public void onResponseComplete(boolean suc) {
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
      });
   }

   public void denyFollowRequest(int position) {
      String memName = requestingMembers.get(position).getMemberName();
      Context context = getApplicationContext();
      followController.responseRequest(memName, false, new LoginController.OnResponseCallback() {
         @Override
         public void onResponseComplete(boolean suc) {
            requestingMembers.remove(position);
            MediaPlayer song = MediaPlayer.create(context, R.raw.pop2);
            song.start();

            Log.d("debugInfo", "denyFollowRequest run at position " + position);
            followRequestAdapter.notifyDataSetChanged();
         }
      });

   }





}
