package com.slt.data;

/**
 * Created by Thorsten on 06.11.2017.
 */

import android.location.Location;
import android.media.Image;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.AchievementCalculator;
import com.slt.control.StepSensor;

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
     * Stores time user has been standing during the activity
     */
    private long inactiveTime;

    /**
     * Step Sensor
     */
    private StepSensor myStepSensor;

    /**
     * The Steps of the user if the segment is a walking segment
     */
    private int userSteps;

    /**
     * Start location that was detected for the Segment
     */
    private String startPlace;

    /**
     * Start address that was detected for the segment
     */
    private String startAddress;

    /**
     * Linked list with images for the segment
     */
    private LinkedList<Image> myImages;


    /**
     *
     * @param location
     * @param date
     * @param activity
     */
    public TimelineSegment(Location location, Date date, DetectedActivity activity){
        myLocationPoints = new LinkedList<>();
        myAchievements = new LinkedList<>();
        myImages = new LinkedList<>();
        myActivity = activity;
        userComments = new LinkedList<>();
        userSteps = 0;
        activeDistance = 0.0;
        activeTime = 0;
        inactiveTime = 0;
        startAddress = "";
        this.addLocationPoint(location, date);


        if(DetectedActivity.WALKING == activity.getType() || DetectedActivity.ON_FOOT == activity.getType()){
            this.myStepSensor = new StepSensor();
        }
    }

    /**
     *
     */
    public void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator.calculateSegmentAchievements(
                this.activeDistance,this.inactiveDistance,this.activeTime, this.inactiveTime, this.myAchievements);

        this.myAchievements.addAll(achievements);
    }

    /**
     *
     * @param location
     * @param date
     */
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

        if(DetectedActivity.WALKING == this.myActivity.getType() || DetectedActivity.ON_FOOT == this.myActivity.getType()) {
            this.userSteps = myStepSensor.getSteps();
        }

        this.myLocationPoints.add(newEntry);
        this.calculateAchievements();;
    }

    public long getDuration(){
        return activeTime + inactiveTime;
    }

    /**
     *
     * @param activity
     * @return
     */
    public boolean compareActivities(DetectedActivity activity){
        return this.myActivity.getType() == activity.getType();
    }

    public void setUserComment(String userComment, String user) {
        UserComment newComment = new UserComment(user, userComment);
        this.userComments = userComments;
    }

    /**
     *
     * @param achievement
     */
    public void addAchievement(Achievement achievement) {
        this.myAchievements.add(achievement);
    }

    /**
     *
     * @param segment
     * @return
     */
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
                this.userSteps += segment.getUserSteps();
                Log.i(TAG, "Adding all values.");

            default:
                this.inactiveDistance = segment.getActiveDistance() + segment.getInactiveDistance();
                this.inactiveTime = segment.getActiveTime() + segment.getInactiveTime();
                this.userSteps += segment.getUserSteps();
                Log.i(TAG, "Adding inactive time: " +(  segment.getInactiveTime()));
                Log.i(TAG, "Adding inactive distance: " +(  segment.getInactiveDistance()));
        }

        this.calculateAchievements();
        return result;
    }


    public int getUserSteps() {
        return userSteps;
    }

    /**
     *
     * @return
     */
    public LinkedList<Image> getMyImages() {
        return myImages;
    }

    /**
     *
      * @param index
     */
    public void deleteImage(int index){
        if(index < 0 || index >= this.myImages.size()){
            return;
        }

        this.myImages.remove(index);
    }

    /**
     *
     * @param image
     */
    public void setMyImages(Image image) {
        if(image != null) {
            this.myImages.add(image);
        }
    }

    /**
     *
     * @param startPlace
     */
    public void setPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    /**
     *
     * @param startAddress
     */
    public void setAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     *
     * @return
     */
    public LinkedList<LocationEntry> getMyLocationPoints() {
        return myLocationPoints;
    }

    /**
     *
     * @return
     */
    public DetectedActivity getMyActivity() {
        return myActivity;
    }

    /**
     *
     * @return
     */
    public double getInactiveDistance() {
        return inactiveDistance;
    }

    /**
     *
     * @return
     */
    public String getStartPlace() {
        return startPlace;
    }

    /**
     *
     * @return
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     *
     * @return
     */
    public LinkedList<UserComment> getUserComments() {
        return new LinkedList<>(userComments);
    }

    /**
     *
     * @return
     */
    public double getActiveDistance() {
        return activeDistance;
    }

    /**
     *
     * @return
     */
    public long getActiveTime() {
        return activeTime;
    }

    /**
     *
     * @return
     */
    public LinkedList<Achievement> getMyAchievements() {
        return new LinkedList<>(myAchievements);
    }

    /**
     *
     * @return
     */
    public long getInactiveTime() {
        return inactiveTime;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return startAddress;
    }

    /**
     *
     * @return
     */
    public String getPlace() {
        return startPlace;
    }

    /**
     *
     * @return
     */
    public LinkedList<LocationEntry> getLocationPoints() {
        return new LinkedList<>(this.myLocationPoints);
    }
}

