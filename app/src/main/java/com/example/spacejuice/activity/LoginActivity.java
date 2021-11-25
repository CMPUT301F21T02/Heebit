package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
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
        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        loginButton = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancelButton);
        UserNameET = findViewById(R.id.userName);
        PassWordET = findViewById(R.id.editTextTextPassword);
        TextView textView = findViewById(R.id.textView6);
        loginController = new LoginController();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debugInfoLogin", "LoginActivity login.onClick() - Login Button Clicked");
                loadingDialog.startLoadingAlertDialog();
                loginController.login(UserNameET.getText().toString(), PassWordET.getText().toString(), new LoginController.OnLoginCompleteCallback() {
                    @Override
                    public void onLoginComplete(boolean suc) {
                        Log.d("debugInfoLogin", "LoginActivity login - onLoginComplete(true) received");
                        success = suc;
                        if (success) {
                            HabitController.loadHabitsFromFirebase(MainActivity.getUser(), new LoginController.OnHabitsLoadedCompleteCallback() {
                                @Override
                                public void onHabitsComplete(boolean suc) {
                                    Log.d("debugInfoLogin", "LoginActivity login - onHabitsComplete(true) received");
                                    HabitEventController.generateMissedEvents(LoginActivity.this);
                                    loadingDialog.dismissDialog();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "login failure", Toast.LENGTH_SHORT).show();
                            PassWordET.setText(""); // clear the Text
                            loadingDialog.dismissDialog();
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
        Log.d("debugInfoLogin", "LoginActivity.finishLogin() - Finish Login was initialized");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("Members").document(MainActivity.getUser().getMemberName());
        MainActivity.getUser().setNextMidnight(LoginController.getNextMidnight());
        userDoc.update("NextMidnight", LoginController.getNextMidnight());
        LoginController.updateMaxID();
        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
        startActivity(intent);
    }
}