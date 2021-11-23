package com.example.spacejuice;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.telephony.CarrierConfigManager;
import android.util.Log;

import com.example.spacejuice.activity.gpsActivity;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class HabitEvent {
    private String description;
    private Date date;
    private String ImageUrl = null;
    private double longitude;
    private double latitude;
    private Boolean done;
    public int DESC_LENGTH = 80; //cutoff for a short description
    private long eventId;
    private int uid; // a unique ID allowing easier tracking between activities

    public HabitEvent() {
        this.setDate(null);
        this.uid = MainActivity.getUser().getUniqueID();
        this.done = false;
        this.longitude = 0.00;
        this.latitude = 0.00;
    }

    public int getEventIndicator() {
        if (isDone()) {
            return R.drawable.event_success;
        } else {
            return R.drawable.event_failure;
        }
    }
    public void setEventId(@Nullable Integer id){
        if(id == null) {
            this.eventId = System.currentTimeMillis();
        }else {
            this.eventId = id;
        }
    }

    public long getEventId(){
        return this.eventId;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setDate(@Nullable Date d) {
        if(d == null) {
            this.date = new Date();
        } else {
            this.date = d;
        }
    }

    public void setDone(Boolean bool) {
        this.done = bool;
    }

    public String getDescription() {
        return this.description;
    }

    public String getShortDescription() {
        String shortDescription = this.description;
        int l = shortDescription.length();
        if (l > DESC_LENGTH) {
            l = DESC_LENGTH;
            shortDescription = shortDescription.substring(0, l - 2) + "..";
        }
        return shortDescription;
    }

    public Date getDate() {
        return this.date;
    }

    public Boolean isDone() {
        return this.done;
    }

    public int getUid() { return this.uid; }

    public void setImage(String uri){
        this.ImageUrl = uri;
    }

    public String getImage(){
        return this.ImageUrl;
    }

    public void setLocation(double la, double lo){
        this.latitude = la;
        this.longitude = lo;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

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
