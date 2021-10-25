package com.example.spacejuice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.activity.AllHabitsActivity;

import java.util.ArrayList;


// Yuchen: Add tests to create a member and add score and add/subtract followers/followings


public class Member {
   // Might need to redo accesses for these cuz i dunno shit abt private vs public - Harish
   private String memberName;
   private final String memberPassword;
   private int id; // Needs to be the primary id on Firestore
   private int score;
   private int followers;
   private int followings;
   public Context context;

   // need habit list and set adapter
   public Member(Context context) {
      this.memberName = "";
      this.memberPassword = "";
      this.context = context;

      //Set the score and social stats to 0
      this.score = 0;
      this.followers = 0;
      this.followings = 0;
   }


   public Member(Context context, String name, String password) {
      // Set login name and password to the arguments
      this.memberName = name;
      this.memberPassword = password;
      this.context = context;

      //Set the score and social stats to 0
      this.score = 0;
      this.followers = 0;
      this.followings = 0;
   }

   public Member(String name, String password) {
      // Set login name and password to the arguments
      this.memberName = name;
      this.memberPassword = password;
      this.context = null;

      //Set the score and social stats to 0
      this.score = 0;
      this.followers = 0;
      this.followings = 0;
   }

   // Getters -- Need to figure out how to compare password in login so we can access the private password
   //            and return true or false whether it matches or not during login.

   // Public function here that verifies password. so
   // member.verifypassword(passwordAttempt)

   public String getMemberName() {
      return memberName;
   }

   public int getScore() {
      return score;
   }

   public int getFollowers() {
      return followers;
   }

   public int getFollowings() {
      return followings;
   }


   // Setters

   public void setScore(int score) {
      this.score = score;
   }

   public void setFollowers(int followers) {
      this.followers = followers;
   }

   public void setFollowings(int followings) {
      this.followings = followings;
   }

   public void addCustomHabit(String name, String reason, int imageIndicator) {
      // for debug purposes, allows setting a habit with pre-set values

   }
}


        /*extends AppCompatActivity {
   private static Member instance;
   public static ArrayAdapter<Habit> habitAdapter;
   public static ArrayList<Habit> habitList;

   Member(Context activity, int layout) {
      habitList = new ArrayList<>();
      if (habitAdapter == null) {
         habitAdapter = new ArrayAdapter<>(activity, layout, habitList);
      }
   }

   public static Member getInstance(Context activity, int layout) {
      if (instance == null) {
         instance = new Member(activity, layout);
      }
      return instance;
   }

}
*/