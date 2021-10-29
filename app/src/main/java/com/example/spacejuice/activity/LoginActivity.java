package com.example.spacejuice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.LoginController;

public class LoginActivity extends AppCompatActivity {
   /*
   This Activity is used to display the login screen
    */
    Button loginButton;
    Button signUpButton;
    EditText UserNameET;
    EditText PassWordET;
    LoginController loginController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signupButton);
        UserNameET = findViewById(R.id.userName);
        PassWordET = findViewById(R.id.editTextTextPassword);
        loginController = new LoginController();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success;
                success = loginController.login(UserNameET.getText().toString(),
                        PassWordET.getText().toString());
                if (success){
                    Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT);
                    // Feel free to change to whatever but setting to MainActivity makes it super buggy lmfao - harish
                    Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password!",
                            Toast.LENGTH_SHORT);
                    PassWordET.setText(""); // clear the Text
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginController.signUp(UserNameET.getText().toString() ,
                        PassWordET.getText().toString());
                Toast.makeText(LoginActivity.this, "Signed Up!",
                        Toast.LENGTH_SHORT);
                Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                startActivity(intent);
            }

        });
    }
}
