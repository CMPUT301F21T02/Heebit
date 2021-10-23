package com.example.spacejuice;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Member {
   private static Member instance;
   public static ArrayAdapter<Habit> habitAdapter;
   public static ArrayList<Habit> habitList;

   private Member(Context activity, int layout) {
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
