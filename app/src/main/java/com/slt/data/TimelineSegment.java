package com.slt.data;

/**
 * Created by Thorsten on 06.11.2017.
 */

import com.google.android.gms.location.DetectedActivity;

import java.util.LinkedList;

public class TimelineSegment {
    private LinkedList<LocationEntry> myLocationPoints;
    private DetectedActivity myActivity;
    private LinkedList<UserComment> userComments;
    private double totalDistance;
    private long duration;

    private LinkedList<Achievement> myAchievements;

    //stores time user has been standing during the activity
    private long inactiveTime;

    private String startPlace;
    private String startAddress;

    //TODO Numbering für Routenabfrage




    public TimelineSegment(LocationEntry locationEntry, DetectedActivity activity){
        myLocationPoints = new LinkedList<>();
        myAchievements = new LinkedList<>();
        myActivity = activity;
        userComments = new LinkedList<>();
        totalDistance = 0.0;
        duration = 0;
        inactiveTime = 0;
        startAddress = "";
        myLocationPoints.add(locationEntry);
    }

    public String getAddress() {
        return startAddress;
    }

    public void setAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getPlace() {
        return startPlace;
    }

    public void setPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public int mergeTimelineSegments(TimelineSegment segment){
        int result = 0;



        return result;
    }
}
