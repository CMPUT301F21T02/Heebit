package com.example.spacejuice;

import android.util.Log;

import java.util.Date;

public class Habit {
    private static int TITLE_LENGTH = 20;
    private static int REASON_LENGTH = 30;
    private int indicator;
    private String title;
    private String reason;
    private Date startDate;
    //Schedule schedule;

    public Habit(String title, String reason) {
        setTitle(title);
        setReason(reason);
        setStartDate(new Date());
        setIndicator(Indicator.EMPTY.indicatorImage);
        newSchedule();
    }

    public Habit(String title, String reason, int indicator) {
        setTitle(title);
        setReason(reason);
        setStartDate(new Date());
        setIndicator(indicator);
    }

    public enum Indicator {
        // allows the use of EMPTY, BRONZE, SILVER, GOLD to reference the indicator image
        // usage Indicator.GOLD.getImage() will retrieve the image ID for the gold indicator
        EMPTY("empty", R.drawable.habit_empty),
        BRONZE("bronze", R.drawable.habit_bronze),
        SILVER("silver", R.drawable.habit_silver),
        GOLD("gold", R.drawable.habit_gold);

        public String indicatorString;
        public int indicatorImage;
        private Indicator(String string, int imageInt) {
            indicatorString = string;
            indicatorImage = imageInt;
        }

        @Override
        public String toString() {
            return indicatorString;
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

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }

    public void setStartDate(Date d) {
        this.startDate = d;
    }

    public void newSchedule() {
        //todo: create Schedule Type
        //this.schedule = new Schedule();
    }

    public String getTitle() {
        return this.title;
    }

    public String getReason() {
        return this.reason;
    }

    public int getIndicator() { return this.indicator; }

    public Date getStartDate() {
        return this.startDate;
    }

    @Override
    public String toString() {
        return this.title;
    }

    //public Schedule getSchedule() {
    //    return this.schedule;
    //}
}
