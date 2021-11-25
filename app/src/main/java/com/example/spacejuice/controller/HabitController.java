package com.example.spacejuice.controller;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.Schedule;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

public class HabitController {

    public static void addLocalHabit(Habit habit) {
        MainActivity.getUser().addHabit(habit);
    }

    public static void addHabit(Habit habit) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Member user = MainActivity.getUser();
        user.addHabit(habit);
        String username = user.getMemberName();
        String habitName = habit.getTitle();
        boolean sun = habit.getSchedule().Sun();
        boolean mon = habit.getSchedule().Mon();
        boolean tue = habit.getSchedule().Tue();
        boolean wed = habit.getSchedule().Wed();
        boolean thu = habit.getSchedule().Thu();
        boolean fri = habit.getSchedule().Fri();
        boolean sat = habit.getSchedule().Sat();
        boolean privateHabit = habit.isPrivate();

        DocumentReference documentReference = db.collection("Members").document(username)
                .collection("Habits").document(habitName);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Reason", habit.getReason());
                    data.put("Date", habit.getStartDate());
                    data.put("ID", habit.getUid());
                    data.put("Title", habit.getTitle());
                    data.put("Sun", sun);
                    data.put("Mon", mon);
                    data.put("Tue", tue);
                    data.put("Wed", wed);
                    data.put("Thu", thu);
                    data.put("Fri", fri);
                    data.put("Sat", sat);
                    data.put("PrivateHabit", privateHabit);
                    documentReference.set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("debugInfo", "Habit has been added successfully");
                                    LoginController.updateMaxID();
                                }
                            });
                }
            }
        });

    }

    public static void loadSingleHabit(Member member) {


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void loadHabitsFromFirebase(Member member, final LoginController.OnCompleteCallback callback) {
        /* This method should load a member's habits from firebase */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String username = member.getMemberName();

        final CollectionReference[] collectionReference = {db.collection("Members").document(username)
                .collection("Habits")};

        Task<QuerySnapshot> habitRef = collectionReference[0].get();

        habitRef.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<DocumentSnapshot> docs = querySnapshot.getDocuments();
                final boolean[] lastDocument = {false};
                if (!docs.isEmpty()) {

                    for (DocumentSnapshot doc : docs) {
                        String title = doc.getString("Title");
                        String reason = doc.getString("Reason");
                        Habit habit = new Habit(title, reason, -1);
                        Boolean sun = doc.getBoolean("Sun");
                        Boolean mon = doc.getBoolean("Mon");
                        Boolean tue = doc.getBoolean("Tue");
                        Boolean wed = doc.getBoolean("Wed");
                        Boolean thu = doc.getBoolean("Thu");
                        Boolean fri = doc.getBoolean("Fri");
                        Boolean sat = doc.getBoolean("Sat");
                        Boolean privateHabit = doc.getBoolean("PrivateHabit");
                        Date habitStartDate = doc.getDate("Date");
                        int uid = Integer.valueOf(doc.get("ID").toString());
                        habit.getSchedule().changeTo(sun, mon, tue, wed, thu, fri, sat);
                        if (privateHabit == null) {
                            privateHabit = false;
                        }
                        habit.setStartDate(habitStartDate);
                        habit.setPrivacy(privateHabit);
                        habit.forceUid(uid);
                        if (doc == docs.get(docs.size() - 1)) {
                            lastDocument[0] = true;
                            Log.d("debugInfo", "last document is being retrieved in HabitController");
                        }
                        HabitEventController.loadHabitEventsFromFirebase(habit, success -> {
                            if (success) {
                                if (lastDocument[0]) {
                                    Log.d("debugInfo", "loadHabitEventsFromFirebase callback received in HabitController");
                                    callback.onComplete(true);
                                }
                            } else {
                                Log.d("debugInfo", "loadHabitEventsFromFirebase callback returned as FALSE");
                            }
                        });

                        if (MainActivity.getUser().getMaxUID() <= uid) {
                            MainActivity.getUser().setUniqueId(uid + 1);
                        }

                        HabitController.addLocalHabit(habit);

                        Log.d("debugInfo", "doc uid: " + uid);
                    }
                } else {
                    callback.onComplete(true);
                }

            }
        });

    }

    public interface OnHabitLoaded {
        void onLoaded(Boolean success);
    }

    public static ArrayList<Habit> getHabitListItems() {
        ArrayList<Habit> habitList = MainActivity.getUser().getHabitListItems();
        return habitList;
    }

    public static ArrayList<HabitEvent> getHabitEvents(Habit habit) {
        ArrayList<HabitEvent> events = habit.getEvents();
        return events;
    }

    public static Habit getHabitFromUid(int uid) {
        /*     retrieve a habit from a unique id     */
        Habit habit;
        habit = MainActivity.getUser().getHabitFromUid(uid);
        return habit;
    }

    public static void updateHabit(Habit habit) {
        Member user = MainActivity.getUser();
        long uid = habit.getUidLong();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String username = user.getMemberName();
        String habitName = habit.getTitle();
        String habitReason = habit.getReason();
        boolean privateHabit = habit.isPrivate();
        boolean sun = habit.getSchedule().Sun();
        boolean mon = habit.getSchedule().Mon();
        boolean tue = habit.getSchedule().Tue();
        boolean wed = habit.getSchedule().Wed();
        boolean thu = habit.getSchedule().Thu();
        boolean fri = habit.getSchedule().Fri();
        boolean sat = habit.getSchedule().Sat();

        Log.d("debugInfo", "Habit Schedule for Sunday: " + sun);

        Task<QuerySnapshot> querySnap = db.collection("Members").document(username)
                .collection("Habits").whereEqualTo("ID", uid).get();

        Log.d("debugInfo", "uid: " + ((int) uid));
        Log.d("debugInfo", "querySnap: " + querySnap.toString());

        querySnap.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                DocumentReference habitRef = docs.get(0).getReference();
                habitRef.update("Title", habitName);
                habitRef.update("Reason", habitReason);
                habitRef.update("Sun", sun);
                habitRef.update("Mon", mon);
                habitRef.update("Tue", tue);
                habitRef.update("Wed", wed);
                habitRef.update("Thu", thu);
                habitRef.update("Fri", fri);
                habitRef.update("Sat", sat);
                habitRef.update("PrivateHabit", privateHabit);

                Log.d("debugInfo", habitRef.toString());

            }
        });
    }

    public static void deleteHabit(Habit habit) {
        Member user = MainActivity.getUser();
        if (user.getHabitListItems().contains(habit)) {

            MainActivity.getUser().deleteHabit(habit);
            String username = user.getMemberName();
            long uid = habit.getUidLong();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Task<QuerySnapshot> querySnap = db.collection("Members").document(username)
                    .collection("Habits").whereEqualTo("ID", uid).get();

            querySnap.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    DocumentReference habitRef = docs.get(0).getReference();
                    habitRef.delete();
                }
            });
        }
    }
}
