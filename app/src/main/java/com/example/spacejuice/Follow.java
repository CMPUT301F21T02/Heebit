package com.example.spacejuice;

import java.util.ArrayList;

public class Follow {
    private ArrayList<Member> followers;
    private ArrayList<Member> followings;
    private ArrayList<String> Requests;     // the requests received
    // Constructor for new member
    public Follow(){
        this.followers = new ArrayList<Member>();
        this.followings = new ArrayList<Member>();
        this.Requests = new ArrayList<String>();
    }

}
