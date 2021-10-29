package com.example.spacejuice;

import java.io.Serializable;

public class Indicator implements Serializable {
   // A class to manage indicator level and image resources
   private int MAX_LEVEL = 9;
   private int MIN_LEVEL = 0;
   private int INCREMENT = 1;
   private int DECAY = -3;
   private String string;
   private int level;
   private int image;

   public Indicator() {
      string = "new";
      level = 0;
      image = R.drawable.indicatornew;
   }

   public int getLevel() {
      return level;
   }

   public int getImage() {
      return image;
   }

   public void setLevel(int iLevel) {
      if (iLevel < MIN_LEVEL) {
         iLevel = MIN_LEVEL;
      }
      if (iLevel > MAX_LEVEL) {
         iLevel = MAX_LEVEL;
      }
      level = iLevel;
      switch (iLevel) {
         case 0:
            string = "empty";
            image = R.drawable.indicator00;
            break;
         case 1:
            string = "bronze 1";
            image = R.drawable.indicator01;
            break;
         case 2:
            string = "bronze 2";
            image = R.drawable.indicator02;
            break;
         case 3:
            string = "bronze 3";
            image = R.drawable.indicator03;
            break;
         case 4:
            string = "silver 1";
            image = R.drawable.indicator04;
            break;
         case 5:
            string = "silver 2";
            image = R.drawable.indicator05;
            break;
         case 6:
            string = "silver 3";
            image = R.drawable.indicator06;
            break;
         case 7:
            string = "gold 1";
            image = R.drawable.indicator07;
            break;
         case 8:
            string = "gold 2";
            image = R.drawable.indicator08;
            break;
         case 9:
            string = "gold 3";
            image = R.drawable.indicator09;
            break;
      }
   }

   public void increase() {
      // increase the indicator level by the reward for completing the habit
      setLevel(getLevel() + INCREMENT);
   }

   public void decrease() {
      // decay the indicator level after missing a habit
      setLevel(getLevel() + DECAY);
   }

   @Override
   public String toString() {
      return string;
   }
}
