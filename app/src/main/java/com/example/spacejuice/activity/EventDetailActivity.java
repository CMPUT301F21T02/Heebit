package com.example.spacejuice.activity;

import static com.example.spacejuice.controller.HabitEventController.getDocumentIdString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.spacejuice.controller.HabitController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
//todo add call back
public class EventDetailActivity extends AppCompatActivity {
    Button back;
    Button upload;
    Button editImage;
    EditText editText;
    ImageView imageView;
    int eventId;
    int habitId;
    int event;
    String habit;
    String U;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debugInfo", "onCrate");
        setContentView(R.layout.activity_event_detail);
        back = findViewById(R.id.edit_back);
        upload = findViewById(R.id.upload);
        editImage = findViewById(R.id.edit_photo);
        editText = findViewById(R.id.edit_editText);
        imageView = findViewById(R.id.edit_imageView);
        habit = getIntent().getExtras().getString("habit");
        habitId = getIntent().getExtras().getInt("habitId");
        event = getIntent().getExtras().getInt("event");
        eventId = getIntent().getExtras().getInt("eventId");
        String suri = getIntent().getExtras().getString("uri");
        HabitEvent e;
        Habit h = MainActivity.getUser().getHabitFromUid(habitId);
        Log.d("debugInfo","h is not null");
        Log.d("debugInfo","h is not null title:"+ h.getTitle());
        e = h.getEventFromUid(event);
        Log.d("debugInfo","e is not null");
        editText.setText(e.getShortDescription());
        Log.d("debugInfo", "url :" + e.getImage());
        Uri uri = Uri.parse(suri);
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
                //e.setDescription(editText.getText().toString());
                Log.d("debugInfo", "into upload");
                Habit h = MainActivity.getUser().getHabitFromUid(habitId);
                Log.d("debugInfo", "into upload"+ habitId);
                HabitEvent e = h.getEventFromUid(event);
                e.setDescription(editText.getText().toString());
                Log.d("debugInfo", "into upload"+ event);
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
                                Log.d("debugInfo", "new image uri: u " + U);
                                // then check url
                                if (U != null){
                                    e.setImage(U);
                                    documentReference.update("url",null);
                                    Log.d("debugInfo", "new image uri: u " + U);
                                }
                            }
                        }
                    }
                });
                Log.d("debugInfo", "new image uri: e.getImage " + e.getImage());
                String id = String.valueOf(e.getEventId());
                DocumentReference eventDocumentReference =documentReference.collection("Events")
                        .document(id);
                eventDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("debugInfo", "Task successful");
                            DocumentSnapshot document = task.getResult();
                            // if this name exist
                            if (document.exists()) {
                                Log.d("debugInfo", "document exist");
                                eventDocumentReference.update("Url",e.getImage());
                                eventDocumentReference.update("Description", editText.getText().toString());
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