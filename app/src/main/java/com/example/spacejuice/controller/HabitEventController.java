package com.example.spacejuice.controller;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;

public class HabitEventController {

    public static void addHabitEvent(Habit habit, HabitEvent habitEvent) {
        // adds a HabitEvent to the array of events contained by a Habit
        habit.addEvent(habitEvent);
    }


}
