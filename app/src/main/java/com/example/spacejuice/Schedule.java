package com.example.spacejuice;

import java.io.Serializable;
import java.util.Calendar;

public class Schedule implements Serializable {
   // Helper class to store Schedule booleans
   private boolean sun;
   private boolean mon;
   private boolean tue;
   private boolean wed;
   private boolean thu;
   private boolean fri;
   private boolean sat;

   public Schedule() {
      // Empty Constructor
      this.sun = false;
      this.mon = false;
      this.tue = false;
      this.wed = false;
      this.thu = false;
      this.fri = false;
      this.sat = false;
   }

   public Schedule(boolean sun, boolean mon, boolean tue, boolean wed,
                   boolean thu, boolean fri, boolean sat) {
      // Constructor with pre-set days
      this.sun = sun;
      this.mon = mon;
      this.tue = tue;
      this.wed = wed;
      this.thu = thu;
      this.fri = fri;
      this.sat = sat;
   }

   // Getters for individual days
   // Example Usage  <<habit>>.schedule.Mon();
   public Boolean Sun() { return sun; }
   public Boolean Mon() { return mon; }
   public Boolean Tue() { return tue; }
   public Boolean Wed() { return wed; }
   public Boolean Thu() { return thu; }
   public Boolean Fri() { return fri; }
   public Boolean Sat() { return sat; }

   // Setters for individual days
   // Example Usage  <<habit>>.schedule.setMon(false);
   public void setSun(Boolean bool) { sun = bool; }
   public void setMon(Boolean bool) { mon = bool; }
   public void setTue(Boolean bool) { tue = bool; }
   public void setWed(Boolean bool) { wed = bool; }
   public void setThu(Boolean bool) { thu = bool; }
   public void setFri(Boolean bool) { fri = bool; }
   public void setSat(Boolean bool) { sat = bool; }

   // Setters for multiple days
   public void changeTo(Boolean sun, Boolean mon, Boolean tue, Boolean wed, Boolean thu,
                        Boolean fri, Boolean sat) {
      this.sun = sun;
      this.mon = mon;
      this.tue = tue;
      this.wed = wed;
      this.thu = thu;
      this.fri = fri;
      this.sat = sat;
   }

   // checks if the schedule contains the day of week (1=SUN, 7=SAT)
   public Boolean checkScheduleDay(int dayOfWeek) {
      switch (dayOfWeek) {
         case Calendar.SUNDAY:
            return Sun();
         case Calendar.MONDAY:
            return Mon();
         case Calendar.TUESDAY:
            return Tue();
         case Calendar.WEDNESDAY:
            return Wed();
         case Calendar.THURSDAY:
            return Thu();
         case Calendar.FRIDAY:
            return Fri();
         case Calendar.SATURDAY:
            return Sat();
      }
      return false;
   }
}
