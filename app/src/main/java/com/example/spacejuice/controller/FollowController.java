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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
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
                    if (document.exists()) {
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
}
