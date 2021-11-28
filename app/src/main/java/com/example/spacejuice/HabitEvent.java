package com.example.spacejuice;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.spacejuice.controller.TimeController;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

/**
 * This is a class to represent habitEvent
 */

public class HabitEvent {
    private String description = "";
    private Date date;
    private String ImageUrl = null;
    private double longitude;
    private double latitude;
    private Boolean done;
    public final int DESC_LENGTH = 80; //cutoff for a short description
    private long eventId;
    private final int uid; // a unique ID allowing easier tracking between activities

    /**
     * This is to create a new HabitEvent
     *
     */
    public HabitEvent() {
        this.setDate(null);
        this.uid = MainActivity.getUser().getUniqueID();
        this.done = false;
        this.longitude = 0.00;
        this.latitude = 0.00;
    }

    /**
     * get a indicator of this event
     * @return a drawable resource to show this event is finished or not
     */
    public int getEventIndicator() {
        if (isDone()) {
            return R.drawable.event_success;
        } else {
            return R.drawable.event_failure;
        }
    }

    /**
     * set an id to this event
     * @param id
     *
     */
    public void setEventId(@Nullable Integer id){
        if(id == null) {
            this.eventId = System.currentTimeMillis();
        }else {
            this.eventId = id;
        }
    }

    /**
     * get the id of a event
     * @return eventId
     */
    public long getEventId(){
        return this.eventId;
    }

    /**
     * set a description to this event
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * set a date to this event
     * @param d
     */
    public void setDate(@Nullable Date d) {
        if(d == null) {
            this.date = TimeController.getCurrentTime().getTime();
        } else {
            this.date = d;
        }
    }

    /**
     * set this event is done or not
     * @param bool
     */
    public void setDone(Boolean bool) {
        this.done = bool;
    }

    /**
     * get the description of this event
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * get the description of this event
     * @return shortDescription
     */
    public String getShortDescription() {
        String shortDescription = this.description;
        int l = shortDescription.length();
        if (l > DESC_LENGTH) {
            l = DESC_LENGTH;
            shortDescription = shortDescription.substring(0, l - 2) + "..";
        }
        return shortDescription;
    }

    /**
     * get the date of this event
     * @return date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * get a bool to show this event is done or not
     * @return done
     */
    public Boolean isDone() {
        return this.done;
    }

    /**
     * get the uid of this event
     * @return uid
     */
    public int getUid() { return this.uid; }

    /**
     * set a image uri of this event
     * @param uri
     */
    public void setImage(String uri){
        this.ImageUrl = uri;
    }

    /**
     * get the image uri of this event
     * @return imageUrl
     */
    public String getImage(){
        return this.ImageUrl;
    }

    /**
     * set the location of this event
     * @param la
     * @param lo
     */
    public void setLocation(double la, double lo){
        this.latitude = la;
        this.longitude = lo;
    }

    /**
     * get the latitude of this event
     * @return latitude
     */
    public double getLatitude(){
        return this.latitude;
    }

    /**
     * get the longitude of this event
     *
     * @return longitude
     */
    public double getLongitude(){
        return this.longitude;
    }

    /**
     * get the location of this event
     * @param context
     * @return location
     */
    public String getLocation(Context context){
        if (this.latitude == 0.00 && this.longitude == 0.00){
            return "Null";
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(this.latitude, this.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea());
    }
}
