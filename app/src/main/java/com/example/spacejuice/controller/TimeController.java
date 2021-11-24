package com.example.spacejuice.controller;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
            offset += 86000000;
            user.setAdminTimeOffset(offset);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userName = user.getMemberName();
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.update("adminTimeOffset", offset);
        }
    }
}
