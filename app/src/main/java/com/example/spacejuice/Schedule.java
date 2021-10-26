package com.example.spacejuice;

public class Schedule {
   // Helper class to store Schedule booleans
   public boolean sun;
   public boolean mon;
   public boolean tue;
   public boolean wed;
   public boolean thu;
   public boolean fri;
   public boolean sat;

   public Schedule(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat) {
      this.sun = sun;
      this.mon = mon;
      this.tue = tue;
      this.wed = wed;
      this.thu = thu;
      this.fri = fri;
      this.sat = sat;
   }
}
