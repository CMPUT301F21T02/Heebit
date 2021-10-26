package com.example.spacejuice;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class Habit {
    private static int TITLE_LENGTH = 20;
    private static int REASON_LENGTH = 30;
    private Indicator indicator;
    private String title;
    private String reason;
    private Date startDate;
    public Schedule schedule = new Schedule(false, false, false, false,
          false, false, false);

    public Habit(String title, String reason) {
        setTitle(title);
        setReason(reason);
        setStartDate(new Date());
        setIndicator(Indicator.LEVELNEW);

    }

    public Habit(String title, String reason, Indicator indicator) {
        setTitle(title);
        setReason(reason);
        setStartDate(new Date());
        setIndicator(indicator);
    }

    public enum Indicator {
        // allows the use of EMPTY, BRONZE, SILVER, GOLD to reference the indicator image
        // usage Indicator.GOLD.image will retrieve the image ID for the gold indicator
        // Indicator.type will return an integer representing its indicator type

        LEVELNEW(-1,"new", R.drawable.indicator_new),
        LEVEL00(0, "empty", R.drawable.habit_level_00),
        LEVEL01(1, "bronze 1", R.drawable.habit_level_01),
        LEVEL02(2, "bronze 2", R.drawable.habit_level_02),
        LEVEL03(3, "bronze 3", R.drawable.habit_level_03),
        LEVEL04(4, "silver 1", R.drawable.habit_level_04),
        LEVEL05(5, "silver 2", R.drawable.habit_level_05),
        LEVEL06(6, "silver 3", R.drawable.habit_level_06),
        LEVEL07(7, "gold 1", R.drawable.habit_level_07),
        LEVEL08(8, "gold 2", R.drawable.habit_level_08),
        LEVEL09(9, "gold 3", R.drawable.habit_level_09);
        public String string;
        public int image;
        public int type;

        private Indicator(int type, String string, int imageInt) {
            this.string = string;
            this.image = imageInt;
            this.type = type;
        }

        @Override
        public String toString() {
            return string;
        }
    }


    private void generateHabitID() {
        //todo: generate an ID unique to this habit (unique relative to this user's habits)
        //this.id = id;
    }

    public void setTitle(String s) {
        int l = s.length();
        if (l > TITLE_LENGTH) { l = TITLE_LENGTH; }
        this.title = s.substring(0,l);
        Log.d("debugInfo", "title set to " + this.title);
    }

    public void setReason(String s) {
        int l = s.length();
        if (l > REASON_LENGTH) { l = REASON_LENGTH; }
        this.reason = s.substring(0,l);
        Log.d("debugInfo", "reason set to " + this.reason);
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public void setStartDate(Date d) {
        this.startDate = d;
    }

    public String getTitle() {
        return this.title;
    }

    public String getReason() {
        return this.reason;
    }

    public Indicator getIndicator() { return this.indicator; }

    public Date getStartDate() {
        return this.startDate;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }
}
