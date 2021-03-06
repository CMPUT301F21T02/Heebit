package com.example.spacejuice.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitController;
import com.example.spacejuice.controller.HabitEventController;
import com.example.spacejuice.controller.TimeController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddHabitEventActivity extends AppCompatActivity {
    /*
 This Activity is used to add and enter the details of a new habit event
  */
    public ImageButton back_button;
    public Button add_image_button;
    public Button create_button;
    public EditText edit_text_description;
    public TextView habit_title_text;
    public TextView habit_reason_text;
    public TextView habit_date_completed;
    public Button gpsButton;
    private double longitude;
    private double latitude;
    private boolean hasLocation =  false;
    private TextView habit_gps_title;
    private TextView habit_gps_content;
    private ImageView imageView;
    Habit currentHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debugInfo", "My Profile View Created from MyProfileActivity.java");
        setContentView(R.layout.event_add);

        back_button = findViewById(R.id.backButtonEA);
        create_button = findViewById(R.id.createEventEA);
        edit_text_description = findViewById(R.id.eventDescriptionEA);
        habit_title_text = findViewById(R.id.habit_title_value);
        habit_reason_text = findViewById(R.id.habit_reason_value);
        habit_date_completed = findViewById(R.id.dateofcompletion);
        add_image_button = findViewById(R.id.add_an_image_button);
        gpsButton = findViewById(R.id.GPS);
        habit_gps_title = findViewById(R.id.habit_gps_title);
        habit_gps_content = findViewById(R.id.habit_gps_value);
        habit_gps_content.setVisibility(View.INVISIBLE);
        habit_gps_title.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.chosen_image);
        /*
            Get the Unique Identifier of the Habit that we are creating an event for
         */
        int habitUid;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                habitUid = 0;
            } else {
                habitUid = extras.getInt("habitUid");
            }
        } else {
            habitUid = (int) savedInstanceState.getSerializable("habitUid");
        }
        currentHabit = HabitController.getHabitFromUid(habitUid);
        Date date = TimeController.getCurrentTime().getTime();

        /* date formatting retrieved from
        https://stackoverflow.com/questions/17192776/get-value-of-day-month-from-date-object-in-android
         */
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMMM",  date); // September
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        habit_date_completed.setText(getString(R.string.eventDateCompletedString, dayOfTheWeek, monthString, Integer.valueOf(day), Integer.valueOf(year)));
        habit_title_text.setText(currentHabit.getTitle());
        habit_reason_text.setText(currentHabit.getReason());
        Log.d("debugInfo", "Habit title: " + currentHabit.getTitle() + "Habit reason: " + currentHabit.getReason());

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HabitEvent event = new HabitEvent();
                event.setDone(true);
                event.setEventId(MainActivity.getUser().getUniqueID());
                event.setDescription(edit_text_description.getText().toString());
                if (hasLocation){
                    event.setLocation(latitude,longitude);
                }
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Members")
                        .document(MainActivity.getUser().getMemberName())
                        .collection("Habits").document(currentHabit.getTitle());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            // if this name exist
                            if (document.exists()) {
                                String U = document.getString("url");
                                // then check url
                                if (U != null){
                                    event.setImage(U);
                                    documentReference.update("url",null);
                                }
                            }
                        }
                    }
                });
                HabitEventController.addHabitEvent(currentHabit, event);
                currentHabit.getIndicator().increase();
                MediaPlayer song = MediaPlayer.create(AddHabitEventActivity.this, R.raw.stamp);
                song.start();
                finish();
            }
        });

        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddHabitEventActivity.this, UploadImageActivity.class);
                intent.putExtra("habit",currentHabit.getTitle());
                startActivityForResult(intent,10);
            }
        });

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddHabitEventActivity.this, MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                longitude = data.getDoubleExtra("longitude", 0.00);
                latitude = data.getDoubleExtra("latitude", 0.00);
                hasLocation = true;
                habit_gps_content.setVisibility(View.VISIBLE);
                habit_gps_title.setVisibility(View.VISIBLE);
                Geocoder geocoder = new Geocoder(AddHabitEventActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                habit_gps_content.setText(addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea());
            }
        }
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                String stringUri = data.getExtras().getString("Uri");
                if(stringUri.equals("0")){
                    Picasso.get().load(R.drawable.empty_image).into(imageView);
                }
                else {
                    Picasso.get().load(Uri.parse(stringUri)).into(imageView);
                }
            }
            }
        }


}

