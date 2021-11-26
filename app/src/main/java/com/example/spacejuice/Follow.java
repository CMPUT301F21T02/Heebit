package com.example.spacejuice;

import java.util.ArrayList;

/**
 * this is a class to represent follow relationship
 */
public class Follow {
    private ArrayList<String> followers;
    private ArrayList<String> followings;
    private ArrayList<String> Requests;     // the requests received
    // Constructor for new member

    /**
     * this is to create a new follow relationship
     */
    public Follow(){
        this.followers = new ArrayList<String>();
        this.followings = new ArrayList<String>();
        this.Requests = new ArrayList<String>();
    }

    /**
     * set the list of requests
     * @param r
     */
    public void setRequests(ArrayList<String> r){
        this.Requests = r;
    }

    /**
     * set a list of followers
     * @param r
     */
    public void setFollowers(ArrayList<String> r){
        this.followers = r;
    }

    /**
     * set a list of followings
     * @param r
     */
    public void setFollowings(ArrayList<String> r){
        this.followings = r;
    }

    /**
     * get a list of requests
     * @return request
     */
    public ArrayList<String> getRequests() {
        return Requests;
    }

    /**
     * get a list of followers
     * @return followers
     */
    public ArrayList<String> getFollowers(){
        return followers;
    }

    /**
     * get a list of followings
     * @return followings
     */
    public ArrayList<String> getFollowings(){
        return followings;
    }
}
