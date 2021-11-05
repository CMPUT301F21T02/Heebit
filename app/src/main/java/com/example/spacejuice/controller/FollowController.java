package com.example.spacejuice.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowController {
    private FirebaseFirestore db;
    //Empty constructor
    public FollowController(){
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Send a following request to another member according to
     * their user name
     * @param userName The user name of the member you tried to send request on
     */
    public void sendRequest(String userName, final LoginController.OnCompleteCallback callback){
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

                        FollowRequests
                                .set(requesting)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("message", "Data has been added successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("message", "Data already exist");
                                    }
                                });

                        callback.onComplete(true);
                    }
                    else {
                        // if other problems exist
                        callback.onComplete(false);
                    }
                }
            }

        });

    }

    /**
     * Update the requests
     */
    public void getRequests(final LoginController.OnCompleteCallback callback){
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Requests = documentReference
                .collection("Follow")
                .document("Requests");
        Requests.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        ArrayList<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null){
                            for (Map.Entry<String, Object> entry : map.entrySet()){
                                list.add(entry.getValue().toString());
                                Log.d("request", "add"+entry.getValue().toString());
                            }
                        }
                        MainActivity.getUser().getFollow().setRequests(list);
                        callback.onComplete(true);
                    }
                    else{
                        callback.onComplete(false);
                    }
                }
            }
        });
    }

    /**
     * Update follower to local
     * @param callback
     */
    public void getFollower(final LoginController.OnCompleteCallback callback){
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Follower = documentReference
                .collection("Follow")
                .document("Follower");
        Follower.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        ArrayList<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null){
                            for (Map.Entry<String, Object> entry : map.entrySet()){
                                list.add(entry.getValue().toString());
                                Log.d("follower", "add "+entry.getValue().toString());
                            }
                        }
                        MainActivity.getUser().getFollow().setFollowers(list);
                        callback.onComplete(true);
                    }
                    else{
                        callback.onComplete(false);
                    }
                }
            }
        });
    }

    /**
     * Update following to local
     * @param callback
     */
    public void getFollowing(final LoginController.OnCompleteCallback callback){
        DocumentReference documentReference = db
                .collection("Members")
                .document(MainActivity.getUser().getMemberName());
        DocumentReference Following = documentReference
                .collection("Follow")
                .document("Following");
        Following.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        ArrayList<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();
                        if (map != null){
                            for (Map.Entry<String, Object> entry : map.entrySet()){
                                list.add(entry.getValue().toString());
                                Log.d("follower", "add "+entry.getValue().toString());
                            }
                        }
                        MainActivity.getUser().getFollow().setFollowings(list);
                        callback.onComplete(true);
                    }
                    else{
                        callback.onComplete(false);
                    }
                }
            }
        });
    }

    /**
     * Accept or deny a follow request
     * @param userName  the user name you want to accept/delete
     * @param accept    accept or deny
     */
    public void responseRequest(String userName, boolean accept, final LoginController.OnCompleteCallback callback){
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
                if(accept){
                    //after delete, add the user into the follower
                    DocumentReference follower = documentReference
                            .collection("Follow")
                            .document("Follower");
                    //create hash map for follower
                    Map<String, Object> user = new HashMap<>();
                    user.put(userName, userName);

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
                                Following
                                        .set(mainUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("message", "User has been added to Following successfully");
                                                callback.onComplete(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("message", "Failed to add User to Following");
                                                callback.onComplete(false);
                                            }
                                        });
                            }
                            else {
                                callback.onComplete(false);
                            }
                        }
                    });

                }
                else{
                    callback.onComplete(false);
                }

            }
        });
    }
}
