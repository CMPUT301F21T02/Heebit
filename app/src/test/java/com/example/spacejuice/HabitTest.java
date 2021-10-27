package com.example.spacejuice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class HabitTest {
    private Habit mockHabit(){
        return new Habit("SpaceJuice", "SpaceJuice", new Date(), new Schedule());
    }

    @Test
    void testGetID(){
        //pass
    }

    @Test
    void testTitle(){
        Habit habit = mockHabit();
        assertEquals("SpaceJuice", habit.getTitle());
        habit.setTitle("Title");
        assertEquals("Title",habit.getTitle());
    }

    @Test
    void testReason(){
        Habit habit = mockHabit();
        assertEquals("SpaceJuice",habit.getReason());
        habit.setReason("Reason");
        assertEquals("Reason",habit.getReason());
    }

    @Test
    void testDate(){
        Date date = new Date();
        Habit habit = mockHabit();
        habit.setStartDate(date);
        assertEquals(date,habit.getStartDate());
    }
}
