package com.example.spacejuice.controller;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;

public class HabitEventController {
    private String name;
    private String imageUri;

    public HabitEventController(){

    }

    public static void addHabitEvent(Habit habit, HabitEvent habitEvent) {
        // adds a HabitEvent to the array of events contained by a Habit
        habit.addEvent(habitEvent);
    }

    public void Upload(String ImageUri){
        Member member = MainActivity.getUser();
        name = member.getMemberName();
        imageUri = ImageUri;

    }

    public String getName(){
        return name;
    }

    public String getImageUri(){
        return imageUri;
    }

    public void setImageUri(String ImageUri){
        imageUri = ImageUri;
    }



}
