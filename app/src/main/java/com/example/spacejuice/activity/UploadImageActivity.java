package com.example.spacejuice.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.spacejuice.Habit;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UploadImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button chooseImage;
    private Button uploadImage;
    private Button backButton;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Uri imageUri;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        chooseImage = findViewById(R.id.button_choose_image);
        uploadImage = findViewById(R.id.upload_button);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress_bar);
        backButton = findViewById(R.id.back_button);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        DocumentReference documentReference = db.collection("Members")
                .document(MainActivity.getUser().getMemberName())
                .collection("Habits").document(getIntent().getExtras().getString("habit"));
        // use logincontroller

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    private void openFileChooser(){
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(imageView);
            // imageView.setImageURI(imageUri);
        }
    }
}