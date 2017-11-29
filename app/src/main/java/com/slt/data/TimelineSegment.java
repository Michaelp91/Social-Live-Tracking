package com.slt.data;

/**
 * Created by Thorsten on 06.11.2017.
 */

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.AchievementCalculator;

import java.util.Date;
import java.util.LinkedList;

public class TimelineSegment {

    /*
    * Tag for the Logger
    */
    private static final String TAG = "TimelineSegment";

    /*
     *
     */
    private LinkedList<LocationEntry> myLocationPoints;

    /*
     *
     */
    private DetectedActivity myActivity;

    /*
     *
     */
    private LinkedList<UserComment> userComments;

    /**
     *
     */
    private double activeDistance;

    /**
     *
     */
    private double inactiveDistance;

    /**
     *
     */
    private LinkedList<Achievement> myAchievements;

    /**
     *
     */
    private long activeTime;

    /*
     *stores time user has been standing during the activity
     */
    private long inactiveTime;

    /**
     *
     */
    private String startPlace;

    /**
     *
     */
    private String startAddress;


    public void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator.calculateSegmentAchievements(
                this.activeDistance,this.inactiveDistance,this.activeTime, this.inactiveTime, this.myAchievements);

        this.myAchievements.addAll(achievements);
    }

    public TimelineSegment(Location location, Date date, DetectedActivity activity){
        myLocationPoints = new LinkedList<>();
        myAchievements = new LinkedList<>();
        myActivity = activity;
        userComments = new LinkedList<>();
        activeDistance = 0.0;
        activeTime = 0;
        inactiveTime = 0;
        startAddress = "";
        this.addLocationPoint(location, date);
    }

    public void addLocationPoint(Location location, Date date) {
        Location lastLocation = null;
        Date lastDate = null;

        if(!this.myLocationPoints.isEmpty()) {
            lastLocation = this.myLocationPoints.getLast().getMyLocation();
            lastDate = this.myLocationPoints.getLast().getMyEntryDate();
        }
        LocationEntry newEntry = new LocationEntry(location, date, lastLocation, lastDate);

        this.activeDistance += newEntry.getMyTrackDistance();
        this.activeTime += newEntry.getMyDuration();

        this.myLocationPoints.add(newEntry);
        this.calculateAchievements();;
    }

    public boolean compareActivities(DetectedActivity activity){
        return this.myActivity != activity;
    }

    public void setUserComment(String userComment, String user) {
        UserComment newComment = new UserComment(user, userComment);
        this.userComments = userComments;
    }

    public void addAchievement(Achievement achievement) {
        this.myAchievements.add(achievement);
    }

    public int mergeTimelineSegments(TimelineSegment segment){
        int result = 0;
        this.myLocationPoints.addAll(segment.getLocationPoints());
        this.myAchievements.addAll(segment.getMyAchievements());
        this.userComments.addAll(segment.getUserComments());

        switch(segment.getMyActivity().getType())
        {
            case DetectedActivity.ON_BICYCLE:
            case DetectedActivity.ON_FOOT:
            case DetectedActivity.WALKING:
            case DetectedActivity.RUNNING:

                this.activeTime += segment.getActiveTime();
                this.inactiveTime += segment.getInactiveTime();
                this.activeDistance += segment.getActiveDistance();
                this.inactiveDistance += segment.getInactiveDistance();
                Log.i(TAG, "Adding all values.");

            default:
                this.inactiveDistance = segment.getActiveDistance() + segment.getInactiveDistance();
                this.inactiveTime = segment.getActiveTime() + segment.getInactiveTime();
                Log.i(TAG, "Adding inactive time: " +(  segment.getInactiveTime()));
                Log.i(TAG, "Adding inactive distance: " +(  segment.getInactiveDistance()));
        }

        this.calculateAchievements();
        return result;
    }

    public void setPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public void setAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LinkedList<LocationEntry> getMyLocationPoints() {
        return myLocationPoints;
    }

    public DetectedActivity getMyActivity() {
        return myActivity;
    }

    public double getInactiveDistance() {
        return inactiveDistance;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public LinkedList<UserComment> getUserComments() {
        return new LinkedList<>(userComments);
    }

    public double getActiveDistance() {
        return activeDistance;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public LinkedList<Achievement> getMyAchievements() {
        return new LinkedList<>(myAchievements);
    }

    public long getInactiveTime() {
        return inactiveTime;
    }

    public String getAddress() {
        return startAddress;
    }

    public String getPlace() {
        return startPlace;
    }

    public LinkedList<LocationEntry> getLocationPoints() {
        return new LinkedList<>(this.myLocationPoints);
    }
}

