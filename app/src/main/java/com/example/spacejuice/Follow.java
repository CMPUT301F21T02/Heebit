package com.example.spacejuice;

import java.util.ArrayList;

public class Follow {
    private ArrayList<Member> followers;
    private ArrayList<Member> followings;

    // Constructor for new member
    public Follow(){
        this.followers = new ArrayList<Member>();
        this.followings = new ArrayList<Member>();
    }
}
