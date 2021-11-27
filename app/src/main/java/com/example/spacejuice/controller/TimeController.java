package com.example.spacejuice.controller;

import android.util.Log;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class TimeController {
    private final static Member user = MainActivity.getUser();

    /**
     *
     * gets the current time
     * @return currentTime
     */
    public static Calendar getCurrentTime() {
        Calendar currentTime = Calendar.getInstance();
        if (user.isAdmin()) {
            long timeInMilli = currentTime.getTimeInMillis();
            long timeOffset = user.getAdminTimeOffset();
            Log.d("debugInfo", "getCurrentTime() --> timeOffset = " + (int) timeOffset);
            currentTime.setTimeInMillis(timeInMilli + timeOffset);
        }
        return currentTime;
    }

    public static void adminIncrementDay() {
        if (user.isAdmin()) {
            Long offset = user.getAdminTimeOffset();
            offset = offset + 86400000;
            user.setAdminTimeOffset(offset);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userName = user.getMemberName();
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.update("adminTimeOffset", offset);
        }
    }

    public static String getDayOfWeek(Calendar cal) {
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
        }
        return "";
    }

    public static String getMonth(Calendar cal) {
        Log.d("debugInfo", "getMonth --> Current: " + Calendar.getInstance().get(Calendar.MONTH) + "   Adjusted: " + cal.get(Calendar.MONTH));
        switch (cal.get(Calendar.MONTH)) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return "";
    }

    public static int compareDates(Date date1, Date date2) {
        // returns 0 if two date objects are the same day
        // otherwise returns <0 if date1 is before date2
        // or returns >0 if date1 is after date2

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return compareCalendarDays(cal1, cal2);
    }

    public static int compareCalendarDays(Calendar cal1, Calendar cal2) {
        // returns 0 if two calendar objects are the same day
        // otherwise returns <0 if cal1 is before cal2
        // or returns >0 if cal1 is after cal2

        if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {
            return 0;
        }

        Date date1 = cal1.getTime();
        Date date2 = cal2.getTime();
        return date1.compareTo(date2);
    }
}
