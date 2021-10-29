package com.example.spacejuice.controller;

import com.example.spacejuice.Habit;
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
}
