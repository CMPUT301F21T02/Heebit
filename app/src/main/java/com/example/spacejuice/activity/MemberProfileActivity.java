package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.FollowingList;
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
   private ImageButton backButton;
   private FollowController followController;
   ArrayList<String> publicHabits;
   private ArrayAdapter<String> ListAdapter;

   private TextView displayName;
   private TextView displayScore;
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
       displayScore = findViewById(R.id.scoreMPA);
       displayHabits = findViewById(R.id.followingHabitsMPA);

       loadingDialog.startLoadingAlertDialog();

       followController = new FollowController();

       String memberName = getIntent().getStringExtra("Member Name");
       displayName.setText(memberName);


       followController.checkFollowing(memberName, new LoginController.OnCompleteCallback(){
           @Override
           public void onComplete(boolean suc) {
               boolean isFollowing = followController.getIsFollowing();
               Log.d("message", "being read");
               if (isFollowing == true) {
                   Log.d("message", "is following");
                   followController.findMember(memberName, new LoginController.OnCompleteCallback(){
                       @Override
                       public void onComplete(boolean suc) {
                           String score = followController.getScore();
                           displayScore.setText(score);
                       }
                   });

                   followController.findPublicHabits(memberName, new LoginController.OnCompleteCallback(){
                       @Override
                       public void onComplete(boolean suc) {
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
                   displayScore.setText("?");
                   TextView HabitText = findViewById(R.id.textViewMPA);
                   HabitText.setText(R.string.mustFollowToViewHabits);
                   loadingDialog.dismissDialog();
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
