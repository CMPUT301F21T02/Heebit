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
import java.util.HashMap;
import java.util.Map;

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
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name exist
                    if (document.exists()) {
                        int maxUniqueId = user.getMaxUID();
                        documentReference.update("currentMaxID", maxUniqueId);
                    }
                } else {
                    // if other problems exist
                    Log.d("debugInfo", "error updating maxID in LoginController.java");
                }
            }
        });
    }


    public void login(String userName, String password, final OnCompleteCallback callback) {
        DocumentReference documentReference = db.collection("Members").document(userName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name exist
                    if (document.exists()) {

                        String p = document.getString("Password");
                        // then check pass word
                        if (p.equals(password)) {
                            int maxUniqueId = Integer.valueOf(document.getLong("currentMaxID").toString());
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
            }

        });

    }


    public void signUp(String userName, String password, final OnCompleteCallback callback) {
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> member = new HashMap<>();
        Map<String, Object> habit = new HashMap<>();
        if (userName.length() > 0 && password.length() > 0) {
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        // if this name is used
                        if (document.exists()) {
                            callback.onComplete(false);
                        } else {
                            // if this name is not used
                            account = new Member(userName, password);
                            MainActivity.setUser(account);
                            user.put("Password", password);
                            user.put("FollowerNumber", "0");
                            user.put("FollowingNumber", "0");
                            user.put("Score", "0");
                            member.put("userName", "NONE");
                            member.put("currentMaxID", account.getUniqueID());
                            habit.put("habitName", "NONE");
                            habit.put("ID", "NONE");
                            habit.put("Reason", "NONE");
                            habit.put("Date", "NONE");
                            DocumentReference habitReference = documentReference
                                    .collection("Habits").document("NONE");
                            DocumentReference followerReference = documentReference
                                    .collection("Follower").document("NONE");
                            DocumentReference followingReference = documentReference
                                    .collection("Following").document("NONE");
                            collectionReference.document(userName)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("message", "Data has been added successfully");
                                        }
                                    });
                            habitReference.set(habit)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("message", "Data has been added successfully");
                                        }
                                    });
                            followerReference.set(member)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("message", "Data has been added successfully");
                                        }
                                    });
                            followingReference.set(member)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("message", "Data has been added successfully");
                                        }
                                    });

                            callback.onComplete(true);
                        }
                    } else {
                        callback.onComplete(false);
                    }
                }
            });


        }
    }


public interface OnCompleteCallback {
    void onComplete(boolean suc);
}
}
