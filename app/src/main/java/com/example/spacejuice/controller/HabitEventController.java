package com.example.spacejuice.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.StorageTask;
//import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HabitEventController {
    public static void editHabitEvent(Habit habit, HabitEvent habitEvent){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int uid = habit.getUid();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Members")
                .document(MainActivity.getUser().getMemberName())
                .collection("Habits").document(habit.getTitle()).collection("Events")
                .document(String.valueOf(habitEvent.getEventId()));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name exist
                    if (document.exists()) {
                        documentReference.update("url",habitEvent.getImage());
                        documentReference.update("Description", habitEvent.getDescription());
                        }
                    }
                }
        });

    }

    /**
     * Add a habit event to the habit
     *
     * @param habit a habit
     * @param habitEvent a habit event
     */

    public static void addHabitEventLocal(Habit habit, HabitEvent habitEvent) {
        // inserts the habitEvent into the appropriate chronological position of the events array

        ArrayList<HabitEvent> events = habit.getEvents();

        int i = events.size() - 1;

        if (i == -1) {
            events.add(habitEvent);
        } else {

            Date eventDate = habitEvent.getDate();
            int dateComparison = TimeController.compareDates(eventDate, events.get(i).getDate());
            if (dateComparison > 0) {
                events.add(habitEvent);
            } else {
                while (i > 0 && dateComparison < 0) {
                    if (dateComparison == 0) {
                        break;
                    }
                    i--;
                    dateComparison = TimeController.compareDates(eventDate, events.get(i).getDate());
                }
                if (dateComparison != 0) {
                    events.add(i, habitEvent);
                } else {
                    if (!events.get(i).isDone()) {
                        events.remove(i);
                        events.add(i, habitEvent);
                        Log.d("debugInfo", "habit event replacing erroneous missed habit event on that day");
                    } else {
                        Log.d("debugInfo", "habit event not added since completed event already exists on that day");
                    }
                }
            }
        }
        habit.setEvents(events);
    }
    /**
     * able to add a habit event to the habit it belongs to
     * @param habit
     * @param habitEvent
     */

    public static void addHabitEvent(Habit habit, HabitEvent habitEvent) {
        // adds a HabitEvent to the array of events contained by a Habit
        addHabitEventLocal(habit, habitEvent);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int uid = habit.getUid();
        Query habitQuery =
                db.collection("Members").document(MainActivity.getUser()
                        .getMemberName()).collection("Habits")
                        .whereEqualTo("ID", uid);
        Task<QuerySnapshot> habitQueryTask = habitQuery.get();
        habitQueryTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {    //get the document with the proper habit uID
                DocumentReference habitRef = Objects.requireNonNull(habitQueryTask
                        .getResult()).getDocuments().get(0).getReference();
                DocumentReference eventDocRef;
                //eventDocRef = habitRef.collection("Events").document(String.valueOf(habitEvent.getEventId()));
                String id = getDocumentIdString(habitEvent);
                //String id = String.valueOf(habitEvent.getEventId());
                eventDocRef = habitRef.collection("Events").document(id);
                eventDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("Id", habitEvent.getEventId());
                            data.put("Description", habitEvent.getDescription());
                            data.put("Date", habitEvent.getDate());
                            data.put("Url", habitEvent.getImage());
                            data.put("Location", new GeoPoint(habitEvent.getLatitude(), habitEvent.getLongitude()));
                            data.put("Done", habitEvent.isDone());
                            eventDocRef.set(data)
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
        });
    }

    public static void editEventGps(Habit habit, HabitEvent habitEvent, double la, double lo) {
        // adds a HabitEvent to the array of events contained by a Habit
        habitEvent.setLocation(la, lo);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int uid = habit.getUid();
        Query habitQuery =
                db.collection("Members").document(MainActivity.getUser()
                        .getMemberName()).collection("Habits")
                        .whereEqualTo("ID", uid);
        Task<QuerySnapshot> habitQueryTask = habitQuery.get();
        habitQueryTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {    //get the document with the proper habit uID
                DocumentReference habitRef = Objects.requireNonNull(habitQueryTask
                        .getResult()).getDocuments().get(0).getReference();
                DocumentReference eventDocRef;
                //eventDocRef = habitRef.collection("Events").document(String.valueOf(habitEvent.getEventId()));
                String id = getDocumentIdString(habitEvent);
                eventDocRef = habitRef.collection("Events").document(id);
                eventDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("Location", new GeoPoint(la, lo));
                            eventDocRef.update(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("debugInfo", "Habit event has been edited successfully");
                                            LoginController.updateMaxID();
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }
    public static void DeleteHabitEvent(Habit habit, HabitEvent habitEvent, boolean isMissed) {
        // adds a HabitEvent to the array of events contained by a Habit

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int uid = habit.getUid();
        Query habitQuery =
                db.collection("Members").document(MainActivity.getUser()
                        .getMemberName()).collection("Habits")
                        .whereEqualTo("ID", uid);
        Task<QuerySnapshot> habitQueryTask = habitQuery.get();
        habitQueryTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {    //get the document with the proper habit uID
                DocumentReference habitRef = Objects.requireNonNull(habitQueryTask
                        .getResult()).getDocuments().get(0).getReference();
                DocumentReference eventDocRef;
                //eventDocRef = habitRef.collection("Events").document(String.valueOf(habitEvent.getEventId()));
                String id = getDocumentIdString(habitEvent);
                //String id = String.valueOf(habitEvent.getEventId());
                eventDocRef = habitRef.collection("Events").document(id);
                eventDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (isMissed){
                                Map<String, Object> data = new HashMap<>();
                                data.put("Description", "");
                                data.put("Url", null);
                                data.put("Location", new GeoPoint(0.00, 0.00));
                                data.put("Done", false);
                                eventDocRef.update(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("debugInfo", "Habit has been deleted successfully");
                                                LoginController.updateMaxID();
                                            }
                                        });
                            }
                            else{
                                eventDocRef.delete();
                            }

                        }
                    }
                });
            }
        });
    }
    /**
     * loads habit events from Firebase for each habit
     * @param habit
     * @param name
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void loadHabitEventsFromFirebase(Habit habit, String name, final HabitController.OnHabitEventsLoaded callback) {
        Log.d("debugInfoLogin", "HabitEventController.loadHabitEventsFromFirebase() - initialized for " + habit.getTitle());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int uid = habit.getUid();
        Query habitEventQuery =
                db.collection("Members").document(name).collection("Habits")
                        .whereEqualTo("ID", uid);
        Task<QuerySnapshot> habitEventQueryTask = habitEventQuery.get();
        habitEventQueryTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {    //get the document with the proper habit uID
                Log.d("debugInfoLogin", "HabitEventController.loadHabitEventsFromFirebase() - habitEventQueryTask onSuccess for " + habit.getTitle());
                DocumentReference habitRef = Objects.requireNonNull(habitEventQueryTask
                        .getResult()).getDocuments().get(0).getReference();
                CollectionReference collectionReference = habitRef.collection("Events");
                Task<QuerySnapshot> habitEventRef = collectionReference.get();
                habitEventRef.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Log.d("debugInfoLogin", "HabitEventController.loadHabitEventsFromFirebase() - HabitEventRef onSuccss for " + habit.getTitle());

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        int docs_loaded = 0;
                        int docs_size = docs.size();

                        if (docs_size == 0) {
                            Log.d("debugInfoLogin", "HabitEventController.loadHabitEventsFromFirebase() - docs size = 0 for " + habit.getTitle() + "... sending callback");
                            callback.onHabitEventsComplete(true);
                        } else {

                            for (DocumentSnapshot doc : docs) {
                                docs_loaded += 1;
                                int id = Objects.requireNonNull(doc.getLong("Id")).intValue();
                                Map<String, Object> data = doc.getData();
                                assert data != null;
                                String Url = (String) data.get("Url");
                                Date date = ((com.google.firebase.Timestamp) data.get("Date")).toDate();
                                String des = (String) data.get("Description");
                                GeoPoint loc = (GeoPoint) data.get("Location");
                                Boolean isDone = (Boolean) data.get("Done");
                                HabitEvent habitEvent = new HabitEvent();
                                habitEvent.setEventId(id);
                                habitEvent.setDescription(des);
                                habitEvent.setDate(date);
                                habitEvent.setImage(Url);

                                assert loc != null;
                                if (name == MainActivity.getUser().getMemberName()) {
                                    habitEvent.setLocation(loc.getLatitude(), loc.getLongitude());
                                }
                                habitEvent.setDone(isDone);

                                addHabitEventLocal(habit, habitEvent);

                                Log.d("debugInfoLogin", "HabitEventController.loadHabitEventsFromFirebase() - loading doc #" + docs_loaded + " for " + habit.getTitle());
                                if (docs_loaded == docs_size) {
                                    Log.d("debugInfoLogin", "HabitEventController.loadHabitEventsFromFirebase() - last doc for " + habit.getTitle() + "... sending callback");
                                    Log.d("iterChecker", "COMPLETE - LAST EVENT LOADEO FOR " + habit.getTitle());
                                    callback.onHabitEventsComplete(true);
                                }
                            }


                        }


                    }

                });
            }
        });
    }

    /**
     * generates missed events on login
     * @param habit
     * @param eventDay
     */
    public static void addMissedEvent(Habit habit, Calendar eventDay, Boolean updateFirebase) {
        HabitEvent missedEvent = new HabitEvent();
        missedEvent.setDate(eventDay.getTime());
        missedEvent.setDone(false);
        missedEvent.setEventId(MainActivity.getUser().getUniqueID());
        if (updateFirebase) {
            addHabitEvent(habit, missedEvent);
        } else {
            //addHabitEventLocal(habit, missedEvent);
        }
    }

    /**
     * makes missed events
     * @param context
     * @param updateFirebase true if missed events are generated online, false if locally only
     */
    public static void generateMissedEvents(Context context, boolean updateFirebase) {

        Member user = MainActivity.getUser();
        int size = user.getHabitListItems().size();
        Log.d("debugInfoLogin", "HabitEventController.generateMissedEvents - initialized..");
        for (Habit h : user.getHabitListItems()) {
            generateHabitMissedEvents(h, updateFirebase);
        }
        if (context.getClass() == LoginActivity.class) {
            ((LoginActivity) context).finishLogin();
        }
    }

    /**
     * calculates the missed habits according to midnight calculation
     * @param habit
     * @param updateFirebase true if missed events are generated online, false if locally only
     */
    public static void generateHabitMissedEvents(Habit habit, boolean updateFirebase) {
        Log.d("debugInfoLogin", "HabitEventController.generateHabitMissedEvents() - intialized for " + habit.getTitle());
        Member user = MainActivity.getUser();
        Calendar dateIterator = Calendar.getInstance();      //iterator used to generated missed events day by day
        Calendar prevMidnightCal = Calendar.getInstance();   //the calendar object for the user's "next midnight" after their last login
        Calendar habitCreationTime = Calendar.getInstance(); //the calendar object for the habit's creation time
        Date prevMidnightDate = user.getPrevNextMidnight();
        Date habitStartDate = habit.getStartDate();

        habitCreationTime.setTime(habitStartDate);
        prevMidnightCal.setTime(prevMidnightDate);

        //convert to millis to compare dates
        if (prevMidnightDate.compareTo(habitStartDate) < 0) {
            prevMidnightDate = habitStartDate;
        }

        dateIterator.setTime(prevMidnightDate);
        Calendar currentDate = TimeController.getCurrentTime();
        dateIterator.add(Calendar.MILLISECOND, -1); // set the date Iterator to 11:59:59.999 previous day

        // check first day separately
        // because it might still have been completed on that day
        if (TimeController.compareCalendarDays(dateIterator, currentDate) < 0) {
            int dayOfWeek = dateIterator.get(Calendar.DAY_OF_WEEK);
            if (habit.getSchedule().checkScheduleDay(dayOfWeek)) {
                if (!HabitController.completedOnDay(habit, dateIterator)) {
                    Log.d("debugInfoLogin", "HabitEventController.generateHabitMissedEvents() - ** MISSED EVENT GENERATED ** for " + habit.getTitle() + " on " + (dateIterator.getTime()).toString());
                    addMissedEvent(habit, dateIterator, updateFirebase);
                } else {
                    Log.d("debugInfoLogin", "HabitEventController.generateHabitMissedEvents() - " + habit.getTitle() + " was completed on day of week #" + (dateIterator.get(Calendar.DAY_OF_WEEK)));
                }


            }
        }

        dateIterator.add(Calendar.DATE, 1);
        // don't need to check if habit was done on rest of days,
        // because we know the user was never logged in for those days
        for (; dateIterator.getTimeInMillis() < currentDate.getTimeInMillis(); dateIterator.add(Calendar.DATE, 1)) {
            int dayOfWeek = dateIterator.get(Calendar.DAY_OF_WEEK);
            if (habit.getSchedule().checkScheduleDay(dayOfWeek)) {
                addMissedEvent(habit, dateIterator, updateFirebase);
            }

        }

    }

    /**
     * gets idString
     * @param event
     * @return idString
     */
    public static String getDocumentIdString(HabitEvent event) {
        @SuppressLint("DefaultLocale") String idString = String.format("%012d", event.getEventId());
        return idString;
    }
/*
    public interface OnCompleteCallback {
        void onComplete(boolean suc);
    }

 */
}
