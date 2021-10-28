package com.example.spacejuice.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
   FollowRequestAdapter followRequestAdapter;
   ArrayList<Member> requestingMembers;

   /*
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("debugInfo", "Follower Requests Activity Created from FollowerRequestsActivity.java");
      setContentView(R.layout.follower_requests);
      back_button = findViewById(R.id.backButtonFollowerRequests);

      followerList = findViewById(R.id.followersList);

      requestingMembers = new ArrayList<>();

      requestingMembers.add(new Member("Heeba", "12345678"));
      requestingMembers.add(new Member("Xuanhao", "12345678"));
      requestingMembers.add(new Member("Harish", "12345678"));
      requestingMembers.add(new Member("LemonJuice", "12345678"));
      requestingMembers.add(new Member("Yuchen", "12345678"));

      followRequestAdapter = new FollowRequestAdapter(this, R.layout.follow_request_content, requestingMembers);
      followerList.setAdapter(followRequestAdapter);

      back_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });
   }
   */

   public void acceptFollowRequest(int position) {

      Context context = getApplicationContext();
      String memName = requestingMembers.get(position).getMemberName();
      CharSequence text = memName + " is now following you";
      int duration = Toast.LENGTH_SHORT;

      Toast toast = Toast.makeText(context, text, duration);
      toast.show();
      requestingMembers.remove(position);
      Log.d("debugInfo", "acceptFollowRequest run at position " + position);
      followRequestAdapter.notifyDataSetChanged();

   }

   public void denyFollowRequest(int position) {
      requestingMembers.remove(position);
      Log.d("debugInfo", "denyFollowRequest run at position " + position);
      followRequestAdapter.notifyDataSetChanged();
   }





}
