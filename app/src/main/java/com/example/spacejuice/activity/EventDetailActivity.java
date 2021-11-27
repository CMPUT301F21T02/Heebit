package com.example.spacejuice.activity;

import static android.content.ContentValues.TAG;
import static com.example.spacejuice.controller.HabitEventController.getDocumentIdString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.spacejuice.Habit;
import com.example.spacejuice.HabitEvent;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.HabitController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

//todo add call back
public class EventDetailActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    Button back;
    Button confirm;
    Button editImage;
    Button takePhoto;
    EditText editText;
    ImageView imageView;
    ProgressBar progressBar;
    private Button uploadImage;
    private Bitmap photo;
    Uri imageUri;

    int habitId = getIntent().getExtras().getInt("habitId");
    int event = getIntent().getExtras().getInt("event");
    private StorageTask uploadTask;
    private StorageReference storageReference;
    Habit h = MainActivity.getUser().getHabitFromUid(habitId);
    HabitEvent e = h.getEventFromUid(event);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debugInfo", "onCrate");
        setContentView(R.layout.activity_event_detail);
        uploadImage = findViewById(R.id.edit_upload);
        back = findViewById(R.id.edit_back);
        confirm = findViewById(R.id.edit_confirm);
        editImage = findViewById(R.id.choose_image);
        takePhoto = findViewById(R.id.take_photo);
        editText = findViewById(R.id.edit_editText);
        imageView = findViewById(R.id.edit_imageView);
        progressBar = findViewById(R.id.edit_progress_bar);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        editText.setText(e.getShortDescription());
        Log.d("debugInfo", "url :" + e.getImage());
        if(e.getImage() != null ) {
            Uri uri = Uri.parse(e.getImage());
            Picasso.get().load(uri).into(imageView);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //e.setDescription(editText.getText().toString());
//                Log.d("debugInfo", "into upload");
//                //Habit h = MainActivity.getUser().getHabitFromUid(habitId);
//                Log.d("debugInfo", "into upload"+ habitId);
//                //HabitEvent e = h.getEventFromUid(event);
//                e.setDescription(editText.getText().toString());
//                Log.d("debugInfo", "into upload"+ event);
//                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Members")
//                        .document(MainActivity.getUser().getMemberName())
//                        .collection("Habits").document(h.getTitle());
//                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            // if this name exist
//                            if (document.exists()) {
//                                U = document.getString("url");
//                                Log.d("debugInfo", "new image uri: u " + U);
//                                // then check url
//                                if (U != null){
//                                    e.setImage(U);
//                                    documentReference.update("url",null);
//                                    Log.d("debugInfo", "new image uri: u " + U);
//                                }
//                            }
//                        }
//                    }
//                });
//                Log.d("debugInfo", "new image uri: e.getImage " + e.getImage());
//                String id = String.valueOf(e.getEventId());
//                DocumentReference eventDocumentReference =documentReference.collection("Events")
//                        .document(id);
//                eventDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("debugInfo", "Task successful");
//                            DocumentSnapshot document = task.getResult();
//                            // if this name exist
//                            if (document.exists()) {
//                                Log.d("debugInfo", "document exist");
//                                eventDocumentReference.update("Url",e.getImage());
//                                eventDocumentReference.update("Description", editText.getText().toString());
//                            }
//                        }
//                    }
//                });
                    uploadFile();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Camera
                Log.d(TAG, "The take photo start");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
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
        }

        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            Log.d(TAG, "The start photo event");
            photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(EventDetailActivity.this, photo);
            imageView.setImageBitmap(photo);
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
        String id = String.valueOf(e.getEventId());
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Members")
                .document(MainActivity.getUser().getMemberName())
                .collection("Habits").document(h.getTitle()).collection("Events").document(id);
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
                            Toast.makeText(EventDetailActivity.this, "Upload successful"
                                    , Toast.LENGTH_LONG).show();
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("debugInfo", "Task successful");
                                        DocumentSnapshot document = task.getResult();
                                        // if this name exist
                                        if (document.exists()) {
                                            Log.d("debugInfo", "document exist");
                                            documentReference.update("Url",imageUri.toString().trim());
                                            documentReference.update("Description", editText.getText().toString());
                                        }
                                    }
                                }
                            });



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    })
            ;
        }else{
            Toast.makeText(EventDetailActivity.this, "Upload successful"
                    , Toast.LENGTH_LONG).show();
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("debugInfo", "Task successful");
                        DocumentSnapshot document = task.getResult();
                        // if this name exist
                        if (document.exists()) {
                            Log.d("debugInfo", "document exist");
                            documentReference.update("Description", editText.getText().toString());
                        }
                    }
                }
            });
        }
    }




}