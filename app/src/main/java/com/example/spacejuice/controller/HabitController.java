package com.example.spacejuice.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HabitController {


   public static void addHabit(Habit habit) {
      FirebaseFirestore db = FirebaseFirestore.getInstance();
      Member user = MainActivity.getUser();
      MainActivity.getUser().addHabit(habit);
      String username = user.getMemberName();
      String habitName = habit.getTitle();
      boolean sun = habit.getSchedule().Sun();
      boolean mon = habit.getSchedule().Mon();
      boolean tue = habit.getSchedule().Tue();
      boolean wed = habit.getSchedule().Wed();
      boolean thu = habit.getSchedule().Thu();
      boolean fri = habit.getSchedule().Fri();
      boolean sat = habit.getSchedule().Sat();
      DocumentReference documentReference = db.collection("Members").document(username)
                                                .collection("Habits").document(habitName);
      documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
               Map<String,Object> data = new HashMap<>();
               data.put("Reason",habit.getReason());
               data.put("Date",habit.getStartDate());
               data.put("ID",habit.getUid());
               data.put("Title",habit.getTitle());
               data.put("Sun",sun);
               data.put("Mon",mon);
               data.put("Tue",tue);
               data.put("Wed",wed);
               data.put("Thu",thu);
               data.put("Fri",fri);
               data.put("Sat",sat);
               documentReference.set(data)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {
                             Log.d( "message","Habit has been added successfully");
                          }
                       });
            }
         }
      });

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
