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
import com.example.spacejuice.controller.LoginController;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    /*
    This Activity is used to display the login screen
     */
    Button loginButton;
    Button signUpButton;
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
        signUpButton = findViewById(R.id.signupButton);
        UserNameET = findViewById(R.id.userName);
        PassWordET = findViewById(R.id.editTextTextPassword);
        TextView textView = findViewById(R.id.textView6);
        loginController = new LoginController();
        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loginController.login(UserNameET.getText().toString(),
                       PassWordET.getText().toString(), new LoginController.OnCompleteCallback() {
                           @Override
                           public void onComplete(boolean suc) {
                               success = suc;
                               //MainActivity.setUser(member);
                               if (success) {
                                   Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                   HabitController.loadHabitsFromFirebase(MainActivity.getUser(), LoginActivity.this);  // load all habits from firebase into Habit array
                               } else {
                                   Toast.makeText(LoginActivity.this, "login failure",
                                           Toast.LENGTH_SHORT).show();
                                   PassWordET.setText(""); // clear the Text
                               }
                           }
                       });
           }
       });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginController.signUp(UserNameET.getText().toString(),
                        PassWordET.getText().toString(), new LoginController.OnCompleteCallback() {
                            @Override
                            public void onComplete(boolean suc) {
                                if(suc) {
                                    Toast.makeText(LoginActivity.this, "Signed Up!",
                                            Toast.LENGTH_SHORT).show();
                                    UserNameET.setText("");
                                    PassWordET.setText("");
                                    Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Username is unavailable",
                                            Toast.LENGTH_SHORT).show();
                                    PassWordET.setText(""); // clear the Text
                                    textView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }

    public void finishLogin() {
        LoginController.updateMaxID();
        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
        startActivity(intent);
    }
}