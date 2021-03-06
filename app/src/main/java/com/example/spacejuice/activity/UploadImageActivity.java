package com.example.spacejuice.activity;


import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Bundle;

import android.os.Handler;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UploadImageActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private Button chooseImage;
    private Button uploadImage;
    private Button backButton;
    private Button takePhoto;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Uri imageUri;
    private Uri returnUri;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private StorageTask uploadTask;
    private Bitmap photo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        chooseImage = findViewById(R.id.button_choose_image);
        uploadImage = findViewById(R.id.upload_button);
        imageView = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);
        backButton = findViewById(R.id.back_button);
        takePhoto = findViewById(R.id.take_photo_button);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        documentReference = db.collection("Members")
                .document(MainActivity.getUser().getMemberName())
                .collection("Habits").document(getIntent().getExtras().getString("habit"));
        //request for Camera Permission
        if(ContextCompat.checkSelfPermission(UploadImageActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UploadImageActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Camera
                Log.d(TAG, "The take photo start");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(UploadImageActivity.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                else if(imageUri == null){
                    Toast.makeText(UploadImageActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Log.d(TAG, "The start event");
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            returnUri = imageUri;
        }

        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            Log.d(TAG, "The start photo event");
            photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(UploadImageActivity.this, photo);
            imageView.setImageBitmap(photo);
            returnUri = imageUri;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(imageUri != null){
            StorageReference fileReference = storageReference.child(
                    MainActivity.getUser().getMemberName()+ getIntent().getExtras().getString("habit") + String.valueOf(System.currentTimeMillis())
                            +"."+ getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(UploadImageActivity.this, "Upload successful"
                                    , Toast.LENGTH_LONG).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Map<String,Object> Duri = new HashMap<>();
                                    Duri.put("url", url);
                                    documentReference.update(Duri)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("message", "URL has been added successfully");
                                                    //Picasso.get().load(imageUri).into(imageView);
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
            finishA();
        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void finishA(){
        Intent returnIntent = new Intent();
        if(returnUri != null) {
            returnIntent.putExtra("Uri",returnUri.toString().trim() );
        }
        else{
            returnIntent.putExtra("Uri","0" );
        }
        setResult(RESULT_OK, returnIntent);
        finish();

    }





}