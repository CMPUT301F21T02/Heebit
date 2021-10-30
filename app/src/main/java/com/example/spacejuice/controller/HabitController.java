package com.example.spacejuice.controller;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;

import java.util.ArrayList;

public class HabitController {

   public static void addHabit(Habit habit) {
      MainActivity.getUser().addHabit(habit);
   }

   public static ArrayList<Habit> getHabitListItems() {
      ArrayList<Habit> habitList = MainActivity.getUser().getHabitListItems();
      return habitList;
   }

   public static ArrayList<HabitEvent> getHabitEvents(Habit habit) {
      ArrayList<HabitEvent> events = habit.getEvents();
      return events;
   }

   public static Habit getHabitFromUid(int uid) {
      /*     retrieve a habit from a unique id     */
      Habit habit;
      habit = MainActivity.getUser().getHabitFromUid(uid);
      return habit;
   }
}
