package com.example.spacejuice;

import android.media.Image;
import android.telephony.CarrierConfigManager;

import java.util.Date;

public class HabitEvent {
    private String description;
    private Date date;
    private Image image;
    private CarrierConfigManager.Gps location;
    private Boolean done;
    private int uid; // a unique ID allowing easier tracking between activities

    public HabitEvent() {
        this.setDate();
        this.uid = MainActivity.getUser().getUniqueID();
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setDate() {
        this.date = new Date();
    }

    public void setDone(Boolean bool) {
        this.done = bool;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getDate() {
        return this.date;
    }

    public Boolean isDone() {
        return this.done;
    }

}
