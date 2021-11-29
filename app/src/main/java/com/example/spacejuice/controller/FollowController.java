package com.example.spacejuice.controller;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacejuice.Habit;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.MemberProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowController {
    private final FirebaseFirestore db;
    private ArrayList<Habit> publicHabits;
    boolean isFollowing = false;
    HabitEventController habitEventController;

    //Empty constructor
    public FollowController() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Send a following request to another member according to
     * their user name
     *
     * @param userName The user name of the member you tried to send request on
     */
    public void sendRequest(String userName, final LoginController.OnRequestCompleteCallback callback) {
        DocumentReference documentReference = db.collection("Members").document(userName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if this name exist
                    if (document.exists() && !userName.equals(MainActivity.getUser().getMemberName())) {
                        HashMap<String, Object> requesting = new HashMap<>();
                        //add the user to the Requests list
                        requesting.put(MainActivity.getUser().getMemberName(), MainActivity.getUser().getMemberName());
                        DocumentReference FollowRequests = documentReference
                                .collection("Follow")
                                .document("Requests");
                        FollowRequests.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        FollowRequests
                                                .update(requesting)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("message", "Data has been added successfully");
                                                        callback.onRequestComplete(true);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("message", "Data already exist");
                                                        callback.onRequestComplete(false);
                                                    }
                                                });
                                    } else {
                                        FollowRequests
                                                .set(requesting)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("message", "Data has been CREATED successfully");
                                                        callback.onRequestComplete(true);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("message", "Data already exist");
                                                        callback.onRequestComplete(false);
                                                    }
                                                });
                                    }
                                }
                            }
                        });
                    } else {
                        // if other problems exist
                        callback.onRequestComplete(false);
                    }
                }
            }

        });

    }

    /**
     * Update the requests
     */
    public void getRequests(final LoginController.OnRequestCompleteCallback callback) {
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Requests = documentReference
                .collection("Follow")
                .document("Requests");
        Requests.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                list.add(entry.getValue().toString());
                                Log.d("request", "add" + entry.getValue().toString());
                            }
                        }
                        MainActivity.getUser().getFollow().setRequests(list);
                        callback.onRequestComplete(true);
                    } else {
                        callback.onRequestComplete(false);
                    }
                }
            }
        });
    }

    /**
     * Update follower to local
     *
     * @param callback
     */
    public void getFollower(final LoginController.OnFollowerCompleteCallback callback) {
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Follower = documentReference
                .collection("Follow")
                .document("Follower");
        Follower.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                list.add(entry.getValue().toString());
                                Log.d("follower", "add " + entry.getValue().toString());
                            }
                        }
                        MainActivity.getUser().getFollow().setFollowers(list);
                        callback.onFollowerComplete(true);
                    } else {
                        callback.onFollowerComplete(false);
                    }
                }
            }
        });
    }

    /**
     * Get
     */

    /**
     * Update following to local
     *
     * @param callback
     */
    public void getFollowing(final LoginController.OnFollowingCompleteCallback callback) {
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Following = documentReference
                .collection("Follow")
                .document("Following");
        Following.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                list.add(entry.getValue().toString());
                                Log.d("follower", "add " + entry.getValue().toString());
                            }
                        }
                        MainActivity.getUser().getFollow().setFollowings(list);
                        callback.onFollowingComplete(true);
                    } else {
                        callback.onFollowingComplete(false);
                    }
                }
            }
        });
    }

    /**
     * Accept or deny a follow request
     *
     * @param userName the user name you want to accept/delete
     * @param accept   accept or deny
     */
    public void responseRequest(String userName, boolean accept, final LoginController.OnResponseCallback callback) {
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Requests = documentReference
                .collection("Follow")
                .document("Requests");
        //Remove the user from the request
        Map<String, Object> delete = new HashMap<>();
        delete.put(userName, FieldValue.delete()); // make the deleting hashmap

        Requests.update(delete).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (accept) {
                    //after delete, add the user into the follower
                    DocumentReference follower = documentReference
                            .collection("Follow")
                            .document("Follower");
                    //create hash map for follower
                    follower.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map<String, Object> user = new HashMap<>();
                                user.put(userName, userName);
                                if (documentSnapshot.exists()) {
                                    follower
                                            .update(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("message", "User has been added to follower");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("message", "User has failed adding to follower");
                                                }
                                            });
                                } else {
                                    follower
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("message", "User has been added to follower");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("message", "User has failed adding to follower");
                                                }
                                            });
                                }
                            }
                        }
                    });

                    // add user into the user following
                    DocumentReference documentReference = db.collection("Members").document(userName);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                HashMap<String, Object> mainUser = new HashMap<>();
                                mainUser.put(MainActivity.getUser().getMemberName(), MainActivity.getUser().getMemberName());
                                DocumentReference Following = documentReference
                                        .collection("Follow")
                                        .document("Following");
                                Following.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()) {
                                                Following
                                                        .update(mainUser)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("message", "User has been added to Following successfully");
                                                                callback.onResponseComplete(true);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("message", "Failed to add User to Following");
                                                                callback.onResponseComplete(false);
                                                            }
                                                        });
                                            } else {
                                                Following
                                                        .set(mainUser)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("message", "User has been added to Following successfully");
                                                                callback.onResponseComplete(true);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("message", "Failed to add User to Following");
                                                                callback.onResponseComplete(false);
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                            } else {
                                callback.onResponseComplete(false);
                            }
                        }
                    });

                } else {
                    callback.onResponseComplete(false);
                }

            }
        });
    }

    /**
     * Finds list of  public habits
     *
     * @param memberName
     * @param callback
     */
    public void findPublicHabits(String memberName, Context context, final LoginController.OnGetPublicHabitsCallback callback) {
        publicHabits = new ArrayList<>();

        Query query = db.collection("Members").document(memberName).collection("Habits")
                .whereEqualTo("PrivateHabit", false);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                Log.d("message", "task is successful");
                if (querySnapshot.isEmpty()) {
                    Log.d("message", "is empty");
                    callback.onPublicHabitsComplete(true);
                } else {
                    Log.d("message", "is not empty");
                    loadEvents(querySnapshot, memberName, context, new LoginController.OnLoadEventsCallback() {
                        @Override
                        public void onLoadEventsComplete(boolean suc) {
                            callback.onPublicHabitsComplete(true);
                        }
                    });

                }
            }

        });

    }

    /**
     * Checks if user is following target member
     *
     * @param memberName
     * @param callback
     */
    public void checkFollowing(String memberName, final LoginController.OnCheckFollowingCallback callback) {
        DocumentReference documentReference = db.collection("Members")
                .document(MainActivity.getUser().getMemberName())
                .collection("Follow")
                .document("Following");
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String string = document.getString(memberName);
                if (string != null) {
                    isFollowing = true;
                }
                callback.onCheckFollowingComplete(true);
            }
        });
    }


    /**
     * Finds the target member
     *
     * @param memberName
     * @param callback
     */
    public void findMember(String memberName, final LoginController.OnFindMemberCompleteCallback callback) {
        DocumentReference documentReference = db.collection("Members").document(memberName);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DocumentSnapshot document = task.getResult();
                // if this name exist
                assert document != null;
                if (document.exists()) {
                    callback.onFindMemberComplete(true);
                }
                callback.onFindMemberComplete(true);
            }
        });
    }

    /**
     * Inserting the Habits onto list and Loading the events
     * @param querySnapshot
     * @param memberName
     * @param context
     * @param callback
     */

    public void loadEvents(QuerySnapshot querySnapshot, String memberName, Context context, final LoginController.OnLoadEventsCallback callback) {
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Habit habit = new Habit(document.getId(), document.getString("Reason"), 0);
            int uid = Integer.valueOf(document.get("ID").toString());
            habit.forceUid(uid);
            habitEventController.loadHabitEventsFromFirebase(habit, memberName, new HabitController.OnHabitEventsLoaded() {
                @Override
                public void onHabitEventsComplete(Boolean success) {
                    if (success) {
                        Log.d("message", "being read");
                        HabitEventController.generateMissedEvents(context, false);
                        addPublicHabit(habit);
                        //publicHabits.add(habit);
                        callback.onLoadEventsComplete(true);
                    }
                }
            });
        }
    }

    public void addPublicHabit(Habit habit) {

        ArrayList<Habit> newHabitList = publicHabits;

        int i = publicHabits.size() - 1;

        if (i == -1) {
            newHabitList.add(habit);
        } else {

            int habitUid = habit.getUid();
            if (habitUid > newHabitList.get(i).getUid()) {
                newHabitList.add(habit);
            } else {
                while (habitUid < (newHabitList.get(i)).getUid() && i > 0) {
                    i--;
                }
                newHabitList.add(i, habit);
            }
        }
        setPublicHabits(newHabitList);
    }

    public void setPublicHabits(ArrayList<Habit> habits) {
        this.publicHabits = habits;
    }


    /**
     * Getters
     */
    public ArrayList<Habit> getPublicHabits() {
        return publicHabits;
    }

    public boolean getIsFollowing() {
        return isFollowing;
    }

}
