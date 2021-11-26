package com.example.spacejuice.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitEventController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {
    Button back;
    Button upload;
    Button editImage;
    EditText editText;
    ImageView imageView;
    int eventId;
    int habitId;
    String habit;
    String U;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        back = findViewById(R.id.back);
        upload = findViewById(R.id.upload);
        editImage = findViewById(R.id.edit_photo);
        editText = findViewById(R.id.edit_editText);
        imageView = findViewById(R.id.edit_imageView);
        habit = getIntent().getExtras().getString("habit");
        habitId = getIntent().getExtras().getInt("habitId");
        eventId = getIntent().getExtras().getInt("eventId");
        // todo bug here
        Habit h = MainActivity.getUser().getHabitFromUid(habitId);
        HabitEvent e = h.getEventFromUid(eventId);
        editText.setText(e.getDescription());
        Uri uri = Uri.parse(e.getImage());
        Picasso.get().load(uri).into(imageView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.setDescription(editText.getText().toString());
                Log.d("debugInfo", "into upload");
                Habit h = MainActivity.getUser().getHabitFromUid(habitId);
                Log.d("debugInfo", "into upload"+ habitId);
                HabitEvent e = h.getEventFromUid(eventId);
                Log.d("debugInfo", "into upload"+ eventId);
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Members")
                        .document(MainActivity.getUser().getMemberName())
                        .collection("Habits").document(h.getTitle());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            // if this name exist
                            if (document.exists()) {
                                U = document.getString("url");
                                // then check url
                                if (U != null){
                                    e.setImage(U);
                                    documentReference.update("url",null);
                                }
                            }
                        }
                    }
                });
                Log.d("debugInfo", U.toString());
                DocumentReference eventDocumentReference =documentReference.collection("Events")
                        .document(String.valueOf(e.getEventId()));
                eventDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("debugInfo", "Task successful");
                            DocumentSnapshot document = task.getResult();
                            // if this name exist
                            if (document.exists()) {
                                Log.d("debugInfo", "document exist");
                                eventDocumentReference.update("url",U);
                                eventDocumentReference.update("Description", e.getDescription());
                            }
                        }
                    }
                });

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent habitIntent = new Intent(EventDetailActivity.this, UploadImageActivity.class);
                habitIntent.putExtra("habit", habit);
                startActivity(habitIntent);
            }
        });
    }


}