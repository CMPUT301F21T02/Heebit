package com.example.spacejuice.controller;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;

import java.util.Calendar;

public class TimeController {
    private final static Member user = MainActivity.getUser();

    public static Calendar getCurrentTime() {
        Calendar currentTime = Calendar.getInstance();
        if (user.isAdmin()) {
            long timeInMilli = currentTime.getTimeInMillis();
            long timeOffset = user.getAdminTimeOffset();
            currentTime.setTimeInMillis(timeInMilli + timeOffset);
        }
        return currentTime;
    }

    public static void adminIncrementDay() {
        if (user.isAdmin()) {
            Long offset = user.getAdminTimeOffset();
            user.setAdminTimeOffset(offset + 86400000);
        }
    }
}
