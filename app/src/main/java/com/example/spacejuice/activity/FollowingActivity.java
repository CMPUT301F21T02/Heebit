package com.example.spacejuice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.FollowingList;
import com.example.spacejuice.Member;
import com.example.spacejuice.R;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {
   /*
   This Activity is used to display who I am following
    */
    private ImageButton backButton;
    ListView followListView;
    ArrayAdapter<Member> followAdapter;
    ArrayList<Member> followList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_activity);

        backButton = findViewById(R.id.backButtonFA);
        followListView = findViewById(R.id.listOfFollowingFA);
        followList = new ArrayList<>();

        followList.add(new Member("Heeba"));
        followList.add(new Member("Xuanhao"));
        followList.add(new Member("Harish"));
        followList.add(new Member("LemonJuice"));
        followList.add(new Member("Yuchen"));

        followAdapter = new FollowingList(this, followList);
        followListView.setAdapter(followAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
