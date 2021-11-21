package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitController;
import com.example.spacejuice.controller.HabitEventController;
import com.example.spacejuice.controller.LoginController;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    /*
    This Activity is used to display the login screen
     */
    Button loginButton;
    Button cancelButton;
    EditText UserNameET;
    EditText PassWordET;
    LoginController loginController;
    boolean success;
    public ArrayList<Member> MemberDataList = new ArrayList<Member>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginButton = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancelButton);
        UserNameET = findViewById(R.id.userName);
        PassWordET = findViewById(R.id.editTextTextPassword);
        TextView textView = findViewById(R.id.textView6);
        loginController = new LoginController();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginController.login(UserNameET.getText().toString(), PassWordET.getText().toString(), new LoginController.OnCompleteCallback() {
                    @Override
                    public void onComplete(boolean suc) {
                        success = suc;
                        if (success) {
                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                            HabitController.loadHabitsFromFirebase(MainActivity.getUser(), new LoginController.OnCompleteCallback() {
                                @Override
                                public void onComplete(boolean suc) {
                                    HabitEventController.generateMissedEvents(LoginActivity.this);
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "login failure", Toast.LENGTH_SHORT).show();
                            PassWordET.setText(""); // clear the Text
                        }
                    }
                });
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void finishLogin() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("Members").document(MainActivity.getUser().getMemberName());
        MainActivity.getUser().setNextMidnight(LoginController.getNextMidnight());
        userDoc.update("nextMidnight", LoginController.getNextMidnight());
        LoginController.updateMaxID();
        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
        startActivity(intent);
    }
}