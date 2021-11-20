package com.example.spacejuice.controller;

import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// still can't return the boolean back
// need to fix this bug
public class LoginController {

    Member account = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Members");


    public LoginController() {

    }

    public static void updateMaxID() {
        Member user = MainActivity.getUser();
        String username = user.getMemberName();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Members").document(username);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                // if this name exist
                assert document != null;
                if (document.exists()) {
                    int maxUniqueId = user.getMaxUID();
                    documentReference.update("currentMaxID", maxUniqueId);
                }
            } else {
                // if other problems exist
                Log.d("debugInfo", "error updating maxID in LoginController.java");
            }
        });
    }


    public void login(String userName, String password, final OnCompleteCallback callback) {
        if(userName.length()>0 && password.length()>0) {
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name exist
                    assert document != null;
                    if (document.exists()) {

                        String p = document.getString("Password");
                        // then check pass word
                        assert p != null;
                        if (p.equals(password)) {
                            int maxUniqueId = 0;
                            if (document.getLong("currentMaxID") != null) {
                                maxUniqueId = Integer.parseInt(Objects.requireNonNull(document.getLong("currentMaxID")).toString());
                            }
                            account = new Member(userName, password);
                            MainActivity.setUser(account);
                            account.setUniqueId(maxUniqueId);
                            callback.onComplete(true);
                        } else {
                            // if the password is wrong
                            Log.d("login", "wrong password ");
                            callback.onComplete(false);
                        }
                    } else {
                        // if other problems exist
                        Log.d("login", "get failed with ", task.getException());
                        callback.onComplete(false);
                    }
                }
            });
        }

    }
    
    public void signUp(String userName, String password, final OnCompleteCallback callback) {
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> member = new HashMap<>();
        Map<String, Object> habit = new HashMap<>();
        if (userName.length() > 0 && password.length() > 0) {
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name is used
                    assert document != null;
                    if (document.exists()) {
                        callback.onComplete(false);
                    } else {
                        // if this name is not used
                        account = new Member(userName, password);
                        MainActivity.setUser(account);

                        // determine the user's next midnight so missed events can be calculated
                        Calendar nextMidnight = Calendar.getInstance();
                        nextMidnight.set(Calendar.HOUR_OF_DAY, 0);
                        nextMidnight.set(Calendar.MINUTE, 0);
                        nextMidnight.set(Calendar.SECOND, 0);
                        nextMidnight.set(Calendar.MILLISECOND, 0);
                        nextMidnight.add(Calendar.DATE, 1);

                        user.put("Password", password);
                        user.put("FollowerNumber", "0");
                        user.put("FollowingNumber", "0");
                        user.put("Score", "0");
                        user.put("currentMaxID", 0);
                        user.put("NextMidnight", nextMidnight);
                        collectionReference.document(userName)
                                .set(user)
                                .addOnSuccessListener(unused -> Log.d("message", "Data has been added successfully"));
                        callback.onComplete(true);
                    }
                } else {
                    callback.onComplete(false);
                }
            });


        }
    }


public interface OnCompleteCallback {
    void onComplete(boolean suc);
}
}
