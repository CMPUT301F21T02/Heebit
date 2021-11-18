package com.example.spacejuice.controller;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.activity.UploadImageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class HabitEventController {

    public static void addHabitEvent(Habit habit, HabitEvent habitEvent) {
        // adds a HabitEvent to the array of events contained by a Habit
        habit.addEvent(habitEvent);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference;
        documentReference = db.collection("Members").document(MainActivity.getUser().getMemberName())
                .collection("Habits").document(habit.getTitle()).collection("Events").document(String.valueOf(habitEvent.getEventId()));

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Id", habitEvent.getEventId());
                    data.put("Description",habitEvent.getDescription());
                    data.put("Date",habitEvent.getDate());
                    data.put("Url",habitEvent.getImage());
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


}
