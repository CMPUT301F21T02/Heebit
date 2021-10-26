package com.example.spacejuice;

public class Schedule {
   // Helper class to store Schedule booleans
   private boolean sun;
   private boolean mon;
   private boolean tue;
   private boolean wed;
   private boolean thu;
   private boolean fri;
   private boolean sat;

   public Schedule(boolean sun, boolean mon, boolean tue, boolean wed,
                   boolean thu, boolean fri, boolean sat) {
      this.sun = sun;
      this.mon = mon;
      this.tue = tue;
      this.wed = wed;
      this.thu = thu;
      this.fri = fri;
      this.sat = sat;
   }

   public Boolean Sun() { return sun; }
   public Boolean Mon() { return mon; }
   public Boolean Tue() { return tue; }
   public Boolean Wed() { return wed; }
   public Boolean Thu() { return thu; }
   public Boolean Fri() { return fri; }
   public Boolean Sat() { return sat; }

   public void setSun(Boolean bool) { sun = bool; }
   public void setMon(Boolean bool) { mon = bool; }
   public void setTue(Boolean bool) { tue = bool; }
   public void setWed(Boolean bool) { wed = bool; }
   public void setThu(Boolean bool) { thu = bool; }
   public void setFri(Boolean bool) { fri = bool; }
   public void setSat(Boolean bool) { sat = bool; }
}
