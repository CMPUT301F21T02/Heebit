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
import com.example.spacejuice.Indicator;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

public class HabitController {

    /**
     * @param habit
     */

    public static void addLocalHabit(Habit habit) {
        MainActivity.getUser().addHabit(habit);
    }

    /**
     * Can add a habit
     *
     * @param habit
     */

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
    public static void loadHabitsFromFirebase(Member member, final LoginController.OnHabitsLoadedCompleteCallback callback) {
        Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - initialized");
        /* This method should load a member's habits from firebase */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String username = member.getMemberName();

        final CollectionReference[] collectionReference = {db.collection("Members").document(username)
                .collection("Habits")};

        Task<QuerySnapshot> habitRef = collectionReference[0].get();

        habitRef.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - Task habitRef onSuccess() triggered");
                List<DocumentSnapshot> docs = querySnapshot.getDocuments();
                if (!docs.isEmpty()) {
                    Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - !docs.isEmpty().. iterating through docs");
                    int habits_loaded = 0;
                    int docs_size = docs.size();
                    for (DocumentSnapshot doc : docs) {
                        habits_loaded += 1;
                        Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - checking habit #" + habits_loaded + " of " + docs_size);
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

                        Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - Launching loadHabitEventsFromFirebase for " + habit.getTitle());

                        if (MainActivity.getUser().getMaxUID() <= uid) {
                            MainActivity.getUser().setUniqueId(uid + 1);
                        }

                        HabitController.addLocalHabit(habit);

                        int finalHabits_loaded = habits_loaded;
                        HabitEventController.loadHabitEventsFromFirebase(habit, MainActivity.getUser().getMemberName(), new OnHabitEventsLoaded() {

                            @Override
                            public void onHabitEventsComplete(Boolean success) {
                                if (success) {
                                    if (finalHabits_loaded == docs_size) {

                                        Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - onHabitEventsComplete triggered for last habit");
                                        callback.onHabitsComplete(true);
                                    } else {
                                        Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - onHabitEventsComplete triggered but not last habit");
                                    }
                                }
                            }
                        });

                    }
                } else {
                    Log.d("debugInfoLogin", "HabitController.loadHabitsFromFirebase() - onHabitEventsComplete triggered since docs is empty");
                    callback.onHabitsComplete(true);
                }

            }
        });

    }

    public interface OnHabitEventsLoaded {
        void onHabitEventsComplete(Boolean success);
    }

    /**
     * returns list
     *
     * @return habitList
     */
    public static ArrayList<Habit> getHabitListItems() {
        ArrayList<Habit> habitList = MainActivity.getUser().getHabitListItems();
        return habitList;
    }

    /**
     * gets habit events
     *
     * @param habit
     * @return events
     */

    public static ArrayList<HabitEvent> getHabitEvents(Habit habit) {
        ArrayList<HabitEvent> events = habit.getEvents();
        return events;
    }

    /**
     * get habit from UID
     *
     * @param uid
     * @return habit
     */

    public static Habit getHabitFromUid(int uid) {
        /*     retrieve a habit from a unique id     */
        Habit habit;
        habit = MainActivity.getUser().getHabitFromUid(uid);
        return habit;
    }

    /**
     * updates habit
     *
     * @param habit
     */
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

    /**
     * deletes habit
     *
     * @param habit
     */
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

    /**
     * check this day is completed or not
     *
     * @param habit    a habit
     * @param checkDay day to check
     * @return bool
     */
    public static Boolean completedOnDay(Habit habit, Calendar checkDay) {
        // checks if a habit was completed on day checkDay
        Date eventDate;
        ArrayList<HabitEvent> events = habit.getEvents();
        Calendar eventDay = Calendar.getInstance();

        for (HabitEvent e : events) {
            eventDate = e.getDate();
            eventDay.setTime(eventDate);
            if (TimeController.compareCalendarDays(eventDay, checkDay) == 0) {
                Log.d("debugInfo", habit.getTitle() + " was completed on that day");
                return true;
            }
        }
        Log.d("debugInfo", "Habit " + habit.getTitle() + " has " + events.size() +
                " events, none of them done on " + (checkDay.getTime()).toString());
        return false;

    }

    /**
     * set this day is complete
     *
     * @param habit a habit
     * @return complete
     */
    public static Boolean completedToday(Habit habit) {
        Calendar today = TimeController.getCurrentTime();
        Log.d("debugInfo", "checking if habit was completed on day of week #" + today.get(Calendar.DAY_OF_WEEK));
        return HabitController.completedOnDay(habit, today);

    }

    public static void adminDeleteHabitEvents(Member member, Habit habit) {
        if (member.getHabitListItems().contains(habit)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String username = member.getMemberName();
            long uid = habit.getUidLong();

            Task<QuerySnapshot> querySnap = db.collection("Members").document(username)
                    .collection("Habits").whereEqualTo("ID", uid).get();

            querySnap.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> habitTask) {
                    List<DocumentSnapshot> docs = habitTask.getResult().getDocuments();
                    DocumentReference habitRef = docs.get(0).getReference();
                    CollectionReference eventsRef = habitRef.collection("Events");
                    Task<QuerySnapshot> eventQuery = eventsRef.get();
                    eventQuery.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> eventTask) {
                            List<DocumentSnapshot> eventsCol = eventTask.getResult().getDocuments();
                            if (eventsCol.size() != 0) {
                                for (int i = eventsCol.size() - 1; i >= 0; i--) {
                                    eventsCol.get(i).getReference().delete();
                                }
                                Log.d("debugInfo", "deleting all events for habit " + habit.getTitle());
                            }

                        }
                    });
                }
            });
        }
    }

    /**
     * iterates through a habits events to calculate its score,
     * assigning it the appropriate indicator, and returning the
     * score as an int.
     *
     * @param habit the habit to evaluate
     */
    public static void calculateScore(Habit habit) {
        Indicator indicator = habit.getIndicator();
        ArrayList<HabitEvent> events = habit.getEvents();
        indicator.setXp(0);

        if (events.size() > 0) {
            for (HabitEvent eventItem : events) {
                if (eventItem.isDone()) {
                    indicator.increase();

                } else {
                    indicator.decrease();
                }
            }
        }
    }

    public static int getHabitStreak(Habit habit) {
        ArrayList<HabitEvent> events = habit.getEvents();
        int size = events.size();
        int streak = 0;
        boolean successStreak = false;
        if (size == 0) {
            return 0;
        }
        if (size == 1) {
            if (events.get(0).isDone()) {
                return 1;
            } else {
                return -1;
            }
        }

        // start the streak off with the last event
        if (events.get(size - 1).isDone()) {
            streak = 1;
            successStreak = true;
        } else {
            streak = -1;
        }

        // if streak is a success streak
        if (successStreak) {
            for (int i = size - 2; i >= 0; i--) {
                boolean currEventDone = events.get(i).isDone();
                if (currEventDone) {
                    streak++;
                } else {
                    return streak;
                }
            }
        } else {
            for (int i = size - 2; i >= 0; i--) {
                boolean currEventDone = events.get(i).isDone();
                if (!currEventDone) {
                    streak--;
                } else {
                    return streak;
                }
            }
        }
        return streak;
    }

    public static Habit copyHabit(Habit habit) {
        String title = habit.getTitle();
        int uid = habit.getUid();
        String reason = habit.getReason();
        Date date = habit.getStartDate();
        Schedule schedule = habit.getSchedule();
        Boolean privateHabit = habit.isPrivate();
        ArrayList<HabitEvent> events = habit.getEvents();
        Habit newHabit = new Habit(title, reason, 0);
        newHabit.forceUid(uid);
        newHabit.setPrivacy(privateHabit);
        newHabit.setStartDate(date);
        newHabit.setSchedule(schedule);
        newHabit.setEvents(events);
        return newHabit;
    }

    public static void swapFirebaseHabitUid(Habit habit1, Habit habit2, OnHabitSwapCallback callback) {
        Member user = MainActivity.getUser();
        String username = user.getMemberName();
        long uid1 = habit1.getUidLong();
        long uid2 = habit2.getUidLong();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Task<QuerySnapshot> querySnap1 = db.collection("Members").document(username)
                .collection("Habits").whereEqualTo("ID", uid1).get();

        querySnap1.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                DocumentReference habitRef1 = docs.get(0).getReference();

                Task<QuerySnapshot> querySnap2 = db.collection("Members").document(username)
                        .collection("Habits").whereEqualTo("ID", uid2).get();

                querySnap2.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> docs = task.getResult().getDocuments();
                        DocumentReference habitRef2 = docs.get(0).getReference();
                        habitRef1.update("ID", uid2);
                        habitRef2.update("ID", uid1);
                        callback.onHabitSwapComplete(true);
                    }
                });

            }
        });


    }

    public static void swapHabits(Habit habit1, Habit habit2, OverviewActivity inst) {
        swapFirebaseHabitUid(habit1, habit2, new OnHabitSwapCallback() {
            @Override
            public void onHabitSwapComplete(boolean suc) {
                Log.d("debugInfoSwap", "before swap, habit1 uid: " + habit1.getUid() + ", habit2 uid: " + habit2.getUid());
                int uid1 = habit1.getUid();
                int uid2 = habit2.getUid();
                habit1.forceUid(uid2);
                habit2.forceUid(uid1);
                Log.d("debugInfoSwap", "after swap, habit1 uid: " + habit1.getUid() + ", habit2 uid: " + habit2.getUid());
                inst.refreshData();
            }
        });
    }

    interface OnHabitSwapCallback {
        void onHabitSwapComplete(boolean suc);
    }
}
