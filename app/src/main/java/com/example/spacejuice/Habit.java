package com.example.spacejuice;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.spacejuice.controller.HabitController;
import com.example.spacejuice.controller.HabitEventController;
import com.example.spacejuice.controller.TimeController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a class to represent a habit.
 */
public class Habit implements Serializable {
    private final static int TITLE_LENGTH = 20;
    private final static int REASON_LENGTH = 30;
    private final Indicator indicator;
    private String title;
    private String reason;
    private Date startDate;
    private Schedule schedule;
    private Boolean privateHabit = false;
    private int uid; // unique identifier for habit
    private ArrayList<HabitEvent> events;

    /**
     * The constructor use for create a new habit
     *
     * @param title  The title for the habit
     * @param reason The reason for the habit
     * @param xp     The current xp of the habit
     */
    public Habit(String title, String reason, int xp) {
        setTitle(title);
        setReason(reason);
        setStartDate(TimeController.getCurrentTime().getTime());
        this.indicator = new Indicator();
        if (xp != -1) {
            indicator.setXp(xp);
        }
        this.schedule = new Schedule();
        this.events = new ArrayList<HabitEvent>();
        this.uid = -1;
    }

    /**
     * Set schedule for the habit
     *
     * @param s the schedule set to the habit
     */
    public void setSchedule(Schedule s) {
        this.schedule = s;
    }

    /**
     * Set title for the habit
     *
     * @param s the title set to the habit
     */
    public void setTitle(String s) {
        int l = s.length();
        if (l > TITLE_LENGTH) {
            l = TITLE_LENGTH;
        }
        this.title = s.substring(0, l);
        Log.d("debugInfo", "title set to " + this.title);
    }

    /**
     * Set reason for the habit
     *
     * @param s the reason set to the habit
     */

    public void setReason(String s) {
        int l = s.length();
        if (l > REASON_LENGTH) {
            l = REASON_LENGTH;
        }
        this.reason = s.substring(0, l);
        Log.d("debugInfo", "reason set to " + this.reason);
    }

    /**
     * Set indicator for the habit
     *
     * @param xp The experience(xp) of the habit use to generate the indicator
     */
    public void setIndicator(int xp) {

        this.indicator.setXp(xp);
    }

    /**
     * Set the start date for habit
     *
     * @param d The start date set to the habit
     */
    public void setStartDate(Date d) {
        this.startDate = d;
    }

    /**
     * This return the title of the habit
     *
     * @return Return the title of the habit
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * This return the reason/description of the habit
     *
     * @return Return the reason of the habit
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * This return the indicator of the habit
     *
     * @return this.indicator   The indicator of the habit
     */
    public Indicator getIndicator() {
        return this.indicator;
    }

    /**
     * This return the start date of the habit
     *
     * @return Return the start date of the habit
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * This transfer habit into a string
     *
     * @return Return the title of the habit
     */
    @NonNull
    @Override
    public String toString() {
        return this.title;
    }

    /**
     * This return the schedule of the habit
     *
     * @return Return the schedule of the habit
     */
    public Schedule getSchedule() {
        return this.schedule;
    }

    /**
     * Check if this habit needs to do today according to the schedule
     *
     * @return Return true if the habit is schedule for the current day of the week
     */

    public Boolean isToday() {
        // returns true if the Habit is scheduled for the current day of the week

        Calendar calendar = TimeController.getCurrentTime();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                return schedule.Sun();
            case Calendar.MONDAY:
                return schedule.Mon();
            case Calendar.TUESDAY:
                return schedule.Tue();
            case Calendar.WEDNESDAY:
                return schedule.Wed();
            case Calendar.THURSDAY:
                return schedule.Thu();
            case Calendar.FRIDAY:
                return schedule.Fri();
            case Calendar.SATURDAY:
                return schedule.Sat();
        }
        return false;
    }

    /**
     * This returns all events of the habit
     *
     * @return return a list of events of the habit
     */
    public ArrayList<HabitEvent> getEvents() {
        return this.events;
    }

    /**
     * This sets the habit's HabitEvents array
     *
     * @param hEvents an ArrayList of HabitEvents
     */
    public void setEvents(ArrayList<HabitEvent> hEvents) {
        this.events = hEvents;
    }

    /**
     * Set the unique id for the habit.
     */
    public void setUid() {
        this.uid = MainActivity.getUser().getUniqueID();
    }

    /**
     * Force to manually set the unique id for the habit
     *
     * @param uidVal unique id
     */
    public void forceUid(int uidVal) {
        /* assigns the uid of the habit to the provided value */
        /* only to be used when gathering data from FireStore */
        this.uid = uidVal;
    }

    /**
     * This returns the unique id of habit
     *
     * @return Returns the unique id of the habit
     */
    public int getUid() {
        return this.uid;
    }

    /**
     * This returns the unique id of the habit in long
     *
     * @return Returns the unique id in long
     */
    public long getUidLong() {
        return this.uid;
    }


    /**
     * get a bool to show this habit is private or not
     *
     * @return privateHabit
     */
    public Boolean isPrivate() {
        return this.privateHabit;
    }

    /**
     * set this habit is private or not
     *
     * @param bool
     */
    public void setPrivacy(Boolean bool) {
        this.privateHabit = bool;
    }
}