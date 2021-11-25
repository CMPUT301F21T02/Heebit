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
import com.example.spacejuice.controller.FollowController;
import com.example.spacejuice.controller.LoginController;
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
    private ListView followListView;
    private ArrayAdapter<Member> followAdapter;
    private ArrayList<Member> followList;
    private FollowController followController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_activity);

        backButton = findViewById(R.id.backButtonFA);
        followListView = findViewById(R.id.listOfFollowingFA);
        followList = new ArrayList<>();
        followController = new FollowController();
        followController.getFollowing(new LoginController.OnFollowingCompleteCallback() {
            @Override
            public void onFollowingComplete(boolean suc) {
                if (suc){
                    ArrayList<String> list = MainActivity.getUser().getFollow().getFollowings();
                    for (int i = 0; i < list.size(); i++){
                        followList.add(new Member(list.get(i)));
                    }
                }
                followAdapter = new FollowingList(FollowingActivity.this, followList);
                followListView.setAdapter(followAdapter);
            }
        });

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
