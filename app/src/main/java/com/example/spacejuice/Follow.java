package com.example.spacejuice;

import java.util.ArrayList;

public class Follow {
    private ArrayList<String> followers;
    private ArrayList<String> followings;
    private ArrayList<String> Requests;     // the requests received
    // Constructor for new member
    public Follow(){
        this.followers = new ArrayList<String>();
        this.followings = new ArrayList<String>();
        this.Requests = new ArrayList<String>();
    }
    public void setRequests(ArrayList<String> r){
        this.Requests = r;
    }

    public ArrayList<String> getRequests() {
        return Requests;
    }
}
