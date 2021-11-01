package com.example.spacejuice.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.FollowersList;
import com.example.spacejuice.FollowingList;
import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {
    /*
    This Activity is used to display who I am following
     */
    private ImageButton backButton;
    ListView followListView;
    ArrayAdapter<Member> followAdapter;
    ArrayList<Member> followList;
    TextView text;  // To change "following" to "followers"
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_activity);

        String username = MainActivity.getUser().getMemberName();

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Members")
                .document(username)
                .collection("Followers");

        backButton = findViewById(R.id.backButtonFA);
        followListView = findViewById(R.id.listOfFollowingFA);
        followList = new ArrayList<>();
        text = findViewById(R.id.textViewFA);
        text.setText("Followers");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                followList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String followMember = doc.getId();
                    followList.add(new Member(followMember)); // Adding the cities and provinces from FireStore
                }
                followAdapter.notifyDataSetChanged();
            }
        });

        followAdapter = new FollowersList(this, followList);
        followListView.setAdapter(followAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
