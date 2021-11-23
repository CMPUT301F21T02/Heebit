package com.example.spacejuice;

import java.io.Serializable;

public class Indicator implements Serializable {
   // A class to manage indicator level and image resources
   private final int LEVEL1_XP = 3;
   private final int XP_INCREASE_PER_LEVEL = 1;
   private final int MAX_LEVEL = 99;
   private final int INCREMENT = 1;
   private final int DECAY = -3;
   private final Boolean PERCENT_XP_DECAY = true;
   private final float XP_DECAY_PERCENTAGE = 0.35f;
   private String string;
   private int level;
   private int progress;
   private int progressDenominator;
   private int image;
   private int xp; // total experience of the habit

   public Indicator() {
      string = "new";
      level = 0;
      image = R.drawable.indicatornew;
   }

   public String getIndicatorText() {
      String levelString = String.valueOf(getLevel());
      return "Lv" + levelString;
   }

   public void updateLevelAndProgress() {
      int xp_remain = this.xp;
      int next_level = LEVEL1_XP;
      int current_level = 0;
      int current_progress = 0;
      while (xp_remain > 0) {
         for (current_progress = 0; current_progress < next_level && xp_remain > 0; current_progress++) {
            xp_remain -= 1;
         }
         if (current_progress == next_level) {
            if (current_level != MAX_LEVEL) {
               current_level++;
               current_progress = 0;
               next_level += XP_INCREASE_PER_LEVEL;
            }
            else {
               xp_remain = 0;
            }
         }
      }
      this.level = current_level;
      this.progress = current_progress;
      this.progressDenominator = next_level;
      this.string = "Level " + level;
   }

   public int getLevel() {
      return this.level;
   }

   public int getXp() {
      return this.xp;
   }

   public int getProgressIndicatorVal() {
      int indicatorVal;
      indicatorVal = (int) Math.floor(( (float) progress / (float) progressDenominator) * 40);
      if (indicatorVal > 40) {
         indicatorVal = 40;
      }
      return indicatorVal;
   }
   public int getImage() {
      int indicatorVal = getProgressIndicatorVal();
      image = R.drawable.progress_00;
      switch (indicatorVal) {
         case 0:
            image = R.drawable.progress_00;
            break;
         case 1:
            image = R.drawable.progress_01;
            break;
         case 2:
            image = R.drawable.progress_02;
            break;
         case 3:
            image = R.drawable.progress_03;
            break;
         case 4:
            image = R.drawable.progress_04;
            break;
         case 5:
            image = R.drawable.progress_05;
            break;
         case 6:
            image = R.drawable.progress_06;
            break;
         case 7:
            image = R.drawable.progress_07;
            break;
         case 8:
            image = R.drawable.progress_08;
            break;
         case 9:
            image = R.drawable.progress_09;
            break;
         case 10:
            image = R.drawable.progress_10;
            break;
         case 11:
            image = R.drawable.progress_11;
            break;
         case 12:
            image = R.drawable.progress_12;
            break;
         case 13:
            image = R.drawable.progress_13;
            break;
         case 14:
            image = R.drawable.progress_14;
            break;
         case 15:
            image = R.drawable.progress_15;
            break;
         case 16:
            image = R.drawable.progress_16;
            break;
         case 17:
            image = R.drawable.progress_17;
            break;
         case 18:
            image = R.drawable.progress_18;
            break;
         case 19:
            image = R.drawable.progress_19;
            break;
         case 20:
            image = R.drawable.progress_20;
            break;
         case 21:
            image = R.drawable.progress_21;
            break;
         case 22:
            image = R.drawable.progress_22;
            break;
         case 23:
            image = R.drawable.progress_23;
            break;
         case 24:
            image = R.drawable.progress_24;
            break;
         case 25:
            image = R.drawable.progress_25;
            break;
         case 26:
            image = R.drawable.progress_26;
            break;
         case 27:
            image = R.drawable.progress_27;
            break;
         case 28:
            image = R.drawable.progress_28;
            break;
         case 29:
            image = R.drawable.progress_29;
            break;
         case 30:
            image = R.drawable.progress_30;
            break;
         case 31:
            image = R.drawable.progress_31;
            break;
         case 32:
            image = R.drawable.progress_32;
            break;
         case 33:
            image = R.drawable.progress_33;
            break;
         case 34:
            image = R.drawable.progress_34;
            break;
         case 35:
            image = R.drawable.progress_35;
            break;
         case 36:
            image = R.drawable.progress_36;
            break;
         case 37:
            image = R.drawable.progress_37;
            break;
         case 38:
            image = R.drawable.progress_38;
            break;
         case 39:
            image = R.drawable.progress_39;
            break;
         case 40:
            image = R.drawable.progress_40;
            break;
      }
      return image;
   }


   public void setXp(int ixp) {
      if (ixp < 0) {
         ixp = 0;
      }
      xp = ixp;
   }

   public void increase() {
      // increase the indicator level by the reward for completing the habit
      setXp(getXp() + INCREMENT);
      updateLevelAndProgress();
   }

   public void decrease() {
      // decay the indicator level after missing a habit
      if (this.PERCENT_XP_DECAY) {
         int newXp = (int) (xp * (1 - XP_DECAY_PERCENTAGE));
         if (newXp < 0) {
            newXp = 0;
         }
         setXp(newXp);
      }
      else {
         int newXp = getXp() + DECAY;
         if (newXp < 0) {
            newXp = 0;
         }
         setXp(newXp);
      }
      updateLevelAndProgress();
   }

   @Override
   public String toString() {
      return string;
   }
}
