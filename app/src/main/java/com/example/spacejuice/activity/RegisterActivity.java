package com.example.spacejuice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spacejuice.R;
import com.example.spacejuice.controller.LoginController;

public class RegisterActivity extends AppCompatActivity {
    Button cancelButton;
    Button signupButton;
    EditText username;
    EditText password;
    EditText repeat_password;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginController logincontroller = new LoginController();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cancelButton = findViewById(R.id.register_CancelButton);
        signupButton = findViewById(R.id.register_RegisterButton);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.editTextTextPassword);
        repeat_password = findViewById(R.id.repeatEditTextTextPassword);
        textView = findViewById(R.id.textView6);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = password.getText().toString();
                if(p.equals(repeat_password.getText().toString())) {
                    logincontroller.signUp(username.getText().toString(),
                            p, new LoginController.OnSignUpCompleteCallback() {
                                @Override
                                public void onSignUpComplete(boolean suc) {
                                    if (suc) {
                                        Toast.makeText(RegisterActivity.this, "Signed Up!",
                                                Toast.LENGTH_SHORT).show();
                                        username.setText("");
                                        password.setText("");
                                        Intent intent = new Intent(RegisterActivity.this, OverviewActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Username is unavailable",
                                                Toast.LENGTH_SHORT).show();
                                        password.setText(""); // clear the Text
                                        repeat_password.setText("");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                } else{
                    Toast.makeText(RegisterActivity.this, "Username is unavailable",
                            Toast.LENGTH_SHORT).show();
                    password.setText(""); // clear the Text
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });





    }
}