package com.example.spacejuice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.controller.HabitController;

import java.util.ArrayList;


// Yuchen: Add tests to create a member and add score and add/subtract followers/followings


public class Member {
   // Might need to redo accesses for these cuz i dunno shit abt private vs public - Harish
   private String memberName;
   private final String memberPassword;
   private int uniqueId = 1; // this is the current state of the uniqueId value, not to be used as a unique Id for this.
   private int id; // Needs to be the primary id on Firestore
   ArrayList<Habit> habitListItems = new ArrayList<>();
   private int score;
   private int followers;
   private int followings;


   public Member() {
      this.memberName = "";
      this.memberPassword = "";

      //Set the score and social stats to 0
      this.score = 0;
      this.followers = 0;
      this.followings = 0;

   }

   public Member(String name) {

      // temporary constructor for setting up test members

      this.memberName = name;
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

   public void initTestData() {
            /*
      initialization of TEST DATA
       */

      /*
      Habit habit;
      habit = new Habit("MWF Habit", "Reason #1", 0);
      habit.getSchedule().changeTo(false, true, false, true, false, true, false);
      HabitController.addHabit(habit);
      habit = new Habit("TTh Habit", "Reason #2", 3);
      habit.getSchedule().changeTo(false, false, true, false, true, true, false);
      HabitController.addHabit(habit);
      habit = new Habit("everyday habit", "reason #3", 9);
      habit.getSchedule().changeTo(true, true, true, true, true, true, true);
      HabitController.addHabit(habit);
      habit = new Habit("weekend habit", "reason #4", 6);
      habit.getSchedule().changeTo(true, false, false, false, false, false, true);
      HabitController.addHabit(habit);
      habit = new Habit("Thurs Fri Habit", "reason #5", 5);
      habit.getSchedule().changeTo(false, false, false, false, true, true, false);
      HabitController.addHabit(habit);
      habit = new Habit("Friday Habit", "reason #6", 7);
      habit.getSchedule().changeTo(false, false, false, false, false, true, false);
      HabitController.addHabit(habit);
      habit = new Habit("Sat Tues Habit", "reason #7", 3);
      habit.getSchedule().changeTo(false, false, true, false, false, false, true);
      HabitController.addHabit(habit);
      habit = new Habit("Sun Mon Habit", "reason #8", 4);
      habit.getSchedule().changeTo(true, true , false, false, false, false, false);
      HabitController.addHabit(habit);
      habit = new Habit("ThFSS Habit", "reason #9", -1);
      habit.getSchedule().changeTo(true, false, false, false, true, true, true);
      HabitController.addHabit(habit);
      Log.d("debugInfo", "is this the user? " + isUser());


       */

   }

   public Boolean isUser() {
      // returns true if this current Member is the User;
      // check for this before allowing certain methods to be used

      return (this == MainActivity.getUser());
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

   public ArrayList<Habit> getHabitListItems() {
      return this.habitListItems;
   }


   // Setters

   public void addHabit(Habit habit) {
      if (isUser()) {
         habit.setUid();
         habitListItems.add(habit);
      }
   }

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

   public int getUniqueID() {
      this.uniqueId += 1;
      return this.uniqueId - 1;
   }

   public void setUniqueId(int val) {
      /* this should only be used to set the uniqueId value to that which is stored in Firebase */
      this.uniqueId = val;
   }

   public Habit getHabitFromUid(int getid) {
      for (Habit habit : habitListItems) {
         if (habit.getUid() == getid) {
            return habit;
         }
      }
      // if no habit with matching id is found, a new habit is returned
      Habit habit;
      habit = new Habit("ERROR NO HABIT FOUND", "This erroneous habit was created since the UID was not found", -1);
      return habit;
   }

   public void deleteHabit(Habit habitDelete){ habitListItems.remove(habitDelete);
   }

}

