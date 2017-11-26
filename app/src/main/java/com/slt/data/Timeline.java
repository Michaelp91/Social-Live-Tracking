package com.slt.data;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Thorsten on 06.11.2017.
 */

public class Timeline {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "Timeline";

    private LinkedList<TimelineDay> myHistory;
    private TimelineDay myCurrentDay;
    private LinkedList<Achievement> myAchievements;


    public Timeline(){
        myHistory = new LinkedList<TimelineDay>();
        myAchievements = new LinkedList<>();
        myCurrentDay = null;
    }


    public void addUserStatus(Location location, Date date, DetectedActivity activity) {
        if (myCurrentDay == null){
            myCurrentDay = new TimelineDay(date);
        }

        if(!this.myCurrentDay.isSameDay(date)) {
            myHistory.add(this.myCurrentDay);
            this.myCurrentDay  = new TimelineDay(date);
        }

        this.myCurrentDay.addUserStatus(location, date, activity);
    }
}
