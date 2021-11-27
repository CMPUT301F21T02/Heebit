package com.example.spacejuice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.FollowingList;
import com.example.spacejuice.Habit;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.PublicHabitsAdapter;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.FollowController;
import com.example.spacejuice.controller.LoginController;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MemberProfileActivity extends AppCompatActivity {
   /*
   This Activity is used to view another member's profile
    */
   private FollowController followController;

   ArrayList<Habit> publicHabits;
   private ArrayAdapter<Habit> ListAdapter;

   private ImageButton backButton;
   private TextView displayName;
   private ListView displayHabits;


   private FirebaseFirestore db;

   @Override
   protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
       setContentView(R.layout.member_profile_activity);
       this.db = FirebaseFirestore.getInstance();
       final LoadingDialog loadingDialog = new LoadingDialog(MemberProfileActivity.this);

       backButton = findViewById(R.id.backButtonMPA);
       displayName = findViewById(R.id.memberNameMPA);
       displayHabits = findViewById(R.id.followingHabitsMPA);

       loadingDialog.startLoadingAlertDialog();

       followController = new FollowController();

       String memberName = getIntent().getStringExtra("Member Name");
       displayName.setText(memberName);


       followController.checkFollowing(memberName, new LoginController.OnCheckFollowingCallback(){
           @Override
           public void onCheckFollowingComplete(boolean suc) {
               boolean isFollowing = followController.getIsFollowing();
               Log.d("message", "being read");
               if (isFollowing == true) {
                   Log.d("message", "is following");
                   followController.findMember(memberName, new LoginController.OnFindMemberCompleteCallback(){
                       @Override
                       public void onFindMemberComplete(boolean suc) {

                       }
                   });

                   followController.findPublicHabits(memberName, new LoginController.OnGetPublicHabitsCallback(){
                       @Override
                       public void onPublicHabitsComplete(boolean suc) {
                           publicHabits = followController.getPublicHabits();
                           if (publicHabits.isEmpty()){
                               TextView HabitText = findViewById(R.id.textViewMPA);
                               HabitText.setText(R.string.noHabits);
                           }
                           ListAdapter = new PublicHabitsAdapter(MemberProfileActivity.this, publicHabits);
                           displayHabits.setAdapter(ListAdapter);
                           loadingDialog.dismissDialog();

                       }

                   });
               } else {
                   Log.d("message", "is not following");
                   TextView HabitText = findViewById(R.id.textViewMPA);
                   HabitText.setText(R.string.mustFollowToViewHabits);
                   loadingDialog.dismissDialog();
                   AlertDialog.Builder builder = new AlertDialog.Builder(MemberProfileActivity.this);
                   builder.setMessage("Must Follow to View Their Public Habits, Do You Wish to Follow?")
                           .setNegativeButton("Cancel", null)
                           .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int x) {
                                   followController.sendRequest(memberName, new LoginController.OnRequestCompleteCallback() {

                                       @Override
                                       public void onRequestComplete(boolean suc) {
                                           if (suc) {
                                               Toast.makeText(MemberProfileActivity.this, "Sent request successfully!", Toast.LENGTH_SHORT).show();
                                           }
                                           else {
                                               if (memberName.equals(MainActivity.getUser().getMemberName())){
                                                   Toast.makeText(MemberProfileActivity.this, "Please don't send follow request to yourself!",
                                                           Toast.LENGTH_SHORT).show();
                                               }
                                               else {
                                                   Toast.makeText(MemberProfileActivity.this, "No such user exists!",
                                                           Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }
                                   });

                               }
                           });
                   AlertDialog alert = builder.create();
                   alert.show();
               }

           }
       });


       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
   }
}
