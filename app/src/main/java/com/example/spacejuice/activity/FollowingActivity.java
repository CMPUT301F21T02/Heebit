package com.example.spacejuice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class FollowingActivity extends AppCompatActivity {
   /*
   This Activity is used to display who I am following
    */
    private ImageButton backButton;
    ListView followListView;
    ArrayAdapter<Member> followAdapter;
    ArrayList<Member> followList;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_activity);

        String username = MainActivity.getUser().getMemberName();
        backButton = findViewById(R.id.backButtonFA);
        followListView = findViewById(R.id.listOfFollowingFA);
        followList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Members")
                .document(username)
                .collection("Followings");

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

        followAdapter = new FollowingList(this, followList);
        followListView.setAdapter(followAdapter);

        listViewListener();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void listViewListener(){
        Context context = this;
        followListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, MemberProfileActivity.class);
                Member member = followList.get(i);
                String name = member.getMemberName();
                intent.putExtra("Member Name", name);
                startActivity(intent);
            }
        });
    }
}
