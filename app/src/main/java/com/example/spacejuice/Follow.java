package com.example.spacejuice;

import java.util.ArrayList;

public class Follow {
    private ArrayList<Member> followers;
    private ArrayList<Member> followings;
    private ArrayList<String> Requests;     // the requests received
    private ArrayList<String> Requesting;   // the requests sent out
    // Constructor for new member
    public Follow(){
        this.followers = new ArrayList<Member>();
        this.followings = new ArrayList<Member>();
        this.Requesting = new ArrayList<String>();
        this.Requests = new ArrayList<String>();
    }

    public void newRequest(String user){
        this.Requesting.add(user);
    }
}
