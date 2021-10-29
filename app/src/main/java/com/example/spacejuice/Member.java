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

   // need habit list and set adapter
   public Member() {
      this.memberName = "";
      this.memberPassword = "";

      //Set the score and social stats to 0
      this.score = 0;
      this.followers = 0;
      this.followings = 0;
   }

   public Member(String memberName, String memberPassword){
      this.memberName = memberName;
      this.memberPassword = memberPassword;

      //Set the score and social stats to 0
      this.score = 0;
      this.followers = 0;
      this.followings = 0;
   }


   // Getters -- Need to figure out how to compare password in login so we can access the private password
   //            and return true or false whether it matches or not during login.

   // Public function here that verifies password. so
   // member.verifyPassword(passwordAttempt)
   public boolean verifyPassword(String password){
      return (password.equals(memberPassword));
   }
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