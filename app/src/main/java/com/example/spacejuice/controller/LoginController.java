package com.example.spacejuice.controller;

import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacejuice.Habit;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// still can't return the boolean back
// need to fix this bug
public class LoginController {

    Member account = null;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Members");


    public LoginController() {

    }

    /**
     * gets the max number to calculate unique id
     */
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

    /**
     * gets the necessary information to login
     * authenticates using firestore
     * @param userName
     * @param password
     * @param callback
     */
    public void login(String userName, String password, final OnLoginCompleteCallback callback) {
        Log.d("debugInfoLogin", "LoginController.login() - initialized");
        if (userName.length() > 0 && password.length() > 0) {
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("debugInfoLogin", "LoginController.login() - DocumentReference Task Successful");
                    DocumentSnapshot document = task.getResult();
                    // if this name exist
                    assert document != null;
                    if (document.exists()) {
                        Log.d("debugInfoLogin", "LoginController.login() - Document Exists");

                        String p = document.getString("Password");
                        // then check password
                        assert p != null;
                        if (p.equals(password)) {
                            Log.d("debugInfoLogin", "LoginController.login() - Password Match");
                            int maxUniqueId = 0;
                            if (document.getLong("currentMaxID") != null) {
                                maxUniqueId = Integer.parseInt(Objects.requireNonNull(document.getLong("currentMaxID")).toString());
                            }
                            account = new Member(userName, password);
                            MainActivity.setUser(account);
                            Date prevMidnight = TimeController.getCurrentTime().getTime();
                            if (document.getDate("NextMidnight") != null) {
                                prevMidnight = document.getDate("NextMidnight");
                            }
                            if (document.getBoolean("admin") != null) {
                                //noinspection ConstantConditions
                                if (document.getBoolean("admin")) {
                                    account.setAdmin(true);
                                    if (document.getLong("adminTimeOffset") != null) {
                                        account.setAdminTimeOffset(document.getLong("adminTimeOffset"));
                                    } else {
                                        documentReference.update("adminTimeOffset", (long) 0);
                                    }
                                }
                            }

                            account.setPrevNextMidnight(prevMidnight);
                            account.setUniqueId(maxUniqueId);
                            callback.onLoginComplete(true);
                        } else {
                            // if the password is wrong
                            Log.d("debugInfoLogin", "LoginController.login() - Password Mismatch");
                            callback.onLoginComplete(false);
                        }
                    } else {
                        // if other problems exist
                        Log.d("login", "get failed with ", task.getException());
                        callback.onLoginComplete(false);
                    }
                }
            });
        }

    }

    /**
     * takes the user input to register a new user
     * @param userName
     * @param password
     * @param callback
     */
    public void signUp(String userName, String password, final OnSignUpCompleteCallback callback) {
        Map<String, Object> user = new HashMap<>();
        if (userName.length() > 0 && password.length() > 0) {
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name is used
                    assert document != null;
                    if (document.exists()) {
                        callback.onSignUpComplete(false);
                    } else {
                        // if this name is not used
                        account = new Member(userName, password);
                        MainActivity.setUser(account);

                        // determine the user's next midnight so missed events can be calculated
                        Date nextMidnight = getNextMidnight();
                        account.setNextMidnight(nextMidnight);

                        user.put("Password", password);
                        user.put("FollowerNumber", "0");
                        user.put("FollowingNumber", "0");
                        user.put("Score", "0");
                        user.put("currentMaxID", 0);
                        user.put("NextMidnight", nextMidnight);
                        collectionReference.document(userName)
                                .set(user)
                                .addOnSuccessListener(unused -> Log.d("message", "Data has been added successfully"));
                        callback.onSignUpComplete(true);
                    }
                } else {
                    callback.onSignUpComplete(false);
                }
            });


        }
    }

    /**
     * changes the time for the admin user
     * @param member
     */
    public static void adminResetAccount(Member member) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userName = member.getMemberName();
        member.setAdminTimeOffset((long)0);
        Date midn = getNextMidnight();
        member.setPrevNextMidnight(midn);
        member.setNextMidnight(midn);
        DocumentReference documentReference = db.collection("Members").document(userName);
        documentReference.update("adminTimeOffset", (long)0);
        documentReference.update("NextMidnight", midn);


        for (Habit habit : member.getHabitListItems()) {
            HabitController.adminDeleteHabitEvents(member, habit);
        }
    }

    /**
     * gets the next day
     * @return midn.getTime()
     */
    public static Date getNextMidnight() {
        Calendar midn = TimeController.getCurrentTime();
        midn.set(Calendar.HOUR_OF_DAY, 0);
        midn.set(Calendar.MINUTE, 0);
        midn.set(Calendar.SECOND, 0);
        midn.set(Calendar.MILLISECOND, 0);
        midn.add(Calendar.DATE, 1);
        return midn.getTime();
    }

    public interface OnRequestCompleteCallback {
        void onRequestComplete(boolean suc);
    }

    public interface OnFollowerCompleteCallback {
        void onFollowerComplete(boolean suc);
    }

    public interface OnFollowingCompleteCallback {
        void onFollowingComplete(boolean suc);
    }

    public interface OnResponseCallback {
        void onResponseComplete(boolean suc);
    }

    public interface OnFindMemberCompleteCallback {
        void onFindMemberComplete(boolean suc);
    }

    public interface OnSignUpCompleteCallback {
        void onSignUpComplete(boolean suc);
    }

    public interface OnGetPublicHabitsCallback {
        void onPublicHabitsComplete(boolean suc);
    }

    public interface OnLoadEventsCallback {
        void onLoadEventsComplete(boolean suc);
    }

    public interface OnCheckFollowingCallback {
        void onCheckFollowingComplete(boolean suc);
    }

    public interface OnLoginCompleteCallback {
        void onLoginComplete(boolean suc);
    }

    public interface OnHabitsLoadedCompleteCallback {
        void onHabitsComplete(boolean suc);
    }
}
