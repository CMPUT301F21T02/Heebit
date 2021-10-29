package com.example.spacejuice.controller;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.activity.LoginActivity;
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
    public ArrayList<Member> MemberDataList = new ArrayList<Member>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Members");
    boolean loginSuccess;
    boolean signUpSuccess;

    public LoginController(){

        //Member test_member = new Member("Bradley", "handsome001");  //testmember
        //MemberDataList.add(test_member);
        //test_member = new Member("1234", "5678");
        //MemberDataList.add(test_member);
    }
    public ArrayList<Member> login(String userName, String password){
        MemberDataList.clear();
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
                        if(p.equals(password)){
                            loginSuccess = true;
                            Member member = new Member(userName,password);
                            MemberDataList.add(member);
                        }else {
                            // if the password is wrong
                            Log.d("login", "wrong password ");
                            loginSuccess = false;
                            MemberDataList.clear();
                        }
                    } else {
                        // if other problems exist
                        Log.d("login", "get failed with ", task.getException());
                        loginSuccess = false;
                        MemberDataList.clear();
                    }
                }
            }
        });
        return MemberDataList;
    }

    public ArrayList<Member> signUp(String userName, String password){
        MemberDataList.clear();
        Map<String,Object> user = new HashMap<>();
        if(userName.length()>0 && password.length()>0){
            DocumentReference documentReference = db.collection("Members").document(userName);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        // if this name is used
                        if (document.exists()) {
                            signUpSuccess = false;
                            MemberDataList.clear();
                        } else {
                            // if it not used
                            signUpSuccess = true;
                            Member member = new Member(userName,password);
                            MemberDataList.add(member);

                            user.put("Password",password);
                            collectionReference.document(userName)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d( "message","Data has been added successfully");
                                        }
                                    });
                        }
                    }
                }
            });
        }
        return MemberDataList;
    }
}
