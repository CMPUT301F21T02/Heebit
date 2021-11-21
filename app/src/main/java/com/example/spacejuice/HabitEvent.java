package com.example.spacejuice;

import android.media.Image;
import android.telephony.CarrierConfigManager;
import android.util.Log;

import java.util.Date;

import javax.annotation.Nullable;

public class HabitEvent {
    private String description = "";
    private Date date;
    private String ImageUrl = null;
    private CarrierConfigManager.Gps location;
    private Boolean done;
    public int DESC_LENGTH = 80; //cutoff for a short description
    private long eventId;
    private int uid; // a unique ID allowing easier tracking between activities

    public HabitEvent() {
        this.setDate(null);
        this.uid = MainActivity.getUser().getUniqueID();
        this.done = false;
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

}
