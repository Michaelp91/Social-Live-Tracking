package com.slt.data;


import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.AchievementCalculator;
import com.slt.control.ApplicationController;
import com.slt.control.StepSensor;
import com.slt.definitions.Constants;
import com.slt.restapi.DataUpdater;
import com.slt.restapi.UpdateOperations;
import com.slt.restapi.UpdateOperations_Synchron;

import org.w3c.dom.Comment;

import java.util.Date;
import java.util.LinkedList;

/**
 * A TimelineSegment stores all location points and statistics for a single activity
 */
public class TimelineSegment {

    /*
    * Tag for the Logger
    */
    private static final String TAG = "TimelineSegment";

    /*
     * The location points detected by GPS for the activity
     */
    private LinkedList<LocationEntry> myLocationPoints;

    /*
     * The activity of the segment
     */
    private DetectedActivity myActivity;

    /*
     * The user comments in regard to the segment
     */
    private LinkedList<UserComment> userComments;

    /**
     * The distance a user spend with the activity
     */
    private double activeDistance;

    /**
     * The distance the user was inactive during f.e. driving
     */
    private double inactiveDistance;

    /**
     * The achievements the users has accomplished
     */
    private LinkedList<Achievement> myAchievements;

    /**
     * The time a user was active during
     */
    private long activeTime;

    /*
     * Stores time user has been inactive (f.e. standing) during the activity
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
     * Start time of the segment
     */
    private Date startTime;

    /**
     * Database ID
     */
    private String ID;


    /**
     * Constructor to initialize all data
     * @param location The location for a new Segment
     * @param date The date the location was detected
     * @param activity The activity the segment was created for
     */
    public TimelineSegment(Location location, Date date, DetectedActivity activity, Date startTime){
        myLocationPoints = new LinkedList<>();
        myAchievements = new LinkedList<>();
        myImages = new LinkedList<>();
        myActivity = activity;
        this.startTime = startTime;
        userComments = new LinkedList<>();
        userSteps = 0;
        activeDistance = 0.0;
        activeTime = 0;
        inactiveTime = 0;
        startAddress = "";
        this.ID = null;

        //start a step counter, might not be needed, but want to have the data in case the user
        // changes the type of activity later
        this.myStepSensor = new StepSensor();

        //add a new location point
        this.addLocationPoint(location, date);
    }

    /**
     * Simple Constructor for use in the REST API
     * @param activity The activity to set
     * @param startTime The start Time of the segment
     */
    public TimelineSegment(DetectedActivity activity, Date startTime){
        myLocationPoints = new LinkedList<>();
        myAchievements = new LinkedList<>();
        myImages = new LinkedList<>();
        myActivity = activity;
        this.startTime = startTime;
        userComments = new LinkedList<>();
        userSteps = 0;
        activeDistance = 0.0;
        activeTime = 0;
        inactiveTime = 0;
        startAddress = "";
        this.ID = null;
    }

    /**
     * Calculate if new achievements were finished
     */
    private void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator.calculateSegmentAchievements(
                this.activeDistance,this.inactiveDistance,this.activeTime, this.inactiveTime, this.userSteps, this.myAchievements);

        this.myAchievements.addAll(achievements);

        //if new achievements -> send intent
        if(!achievements.isEmpty()){
            //REST Call to update the changed achievements
            DataUpdater.getInstance().updateTimelineSegment(this);

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_ACHIEVEMENT_UPDATE);
            intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }
    }



    /**
     *  Method to add a new location point to the segment
     * @param location The new location that should be added to the segment
     * @param date The date the new location was detected
     */
    public void addLocationPoint(Location location, Date date) {
        Location lastLocation = null;
        Date lastDate = null;

        //check if we already have a location point added to the list
        if(!this.myLocationPoints.isEmpty()) {
            lastLocation = this.myLocationPoints.getLast().getMyLocation();
            lastDate = this.myLocationPoints.getLast().getMyEntryDate();
        }

        //create a new location Entry
        LocationEntry newEntry = new LocationEntry(location, date, lastLocation, lastDate);

        //calculate the statistics
        this.activeDistance += newEntry.getMyTrackDistance();
        this.activeTime += newEntry.getMyDuration();

        // update the statistics for the steps
        this.userSteps = myStepSensor.getSteps();

        this.myLocationPoints.add(newEntry);
        this.calculateAchievements();

        //REST Call to add the point to the server
        DataUpdater.getInstance().addLocationEntry(newEntry, this);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_LOCATIONPOINTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Used for add Location Entries based on DB updates
     * @param locationEntry The location entry to add
     * @param userID The DB ID of the user
     */
    public void addLocationEntry(LocationEntry locationEntry, String userID){
        this.myLocationPoints.add(locationEntry);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OTHER_LOCATIONPOINTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Used for add Location Entries based on DB updates
     * @param id The location entry to delete
     * @param userID The DB ID of the user
     */
    public void deleteLocationEntry(String id, String userID){
        LocationEntry entry = null;

        //find entry in history
        for(LocationEntry location : this.myLocationPoints){
            if(id == this.getID()){
                entry = location;
            }
        }

        //if not null delete
        if(null == entry)
            return;

        this.myLocationPoints.remove(entry);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_LOCATIONPOINTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Get the Duration of the segment
     * @return The duration of the segment
     */
    public long getDuration(){
        return activeTime + inactiveTime;
    }

    /**
     * Compare if our current activity is the same
     * @param activity The activity to compare to
     * @return True if the activites are the same, False if not
     */
    public boolean compareActivities(DetectedActivity activity){
        return this.myActivity.getType() == activity.getType();
    }

    /**
     * Add a new user and a comment
     * @param userComment The comment of the user
     * @param user The user that made the comment
     */
    public void addUserComment(String userComment, String user) {
        UserComment newComment = null;

        //check if we have a comment from the user
        for(UserComment com : this.userComments){
            if(com.getUserName().equals(user)){
                newComment = com;
            }
        }

        //check if we have to create a new comment
        if(newComment == null){
            newComment = new UserComment(user, userComment);
            this.userComments.add(newComment);
        } else {
            newComment.addUserComment(userComment);
        }
    }

    /**
     * Add a comment from the DB
     * @param comment The comment
     * @param id  The DB ID of the owning user
     */
    public void addUserComment(UserComment comment, String id) {
        this.userComments.add(comment);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OTHER_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, id);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Get the comments for a specific user
     * @param user The user we want to get the comments for
     * @return The comments for the user, null if none are found
     */
    public UserComment getUserComment(String user){
        UserComment comment = null;

        //check if we have a comment from the user
        for(UserComment com : this.userComments){
            if(com.getUserName().equals(user)){
                comment = com;
            }
        }

        return comment;
    }

    /**
     * Add a achievement to our list
     * @param achievement The achievement we want to add
     * @param userID The DB ID of the user
     */
    public void addAchievement(Achievement achievement, String userID) {

        this.myAchievements.add(achievement);

        //Send intent to inform about update, since this method should only be used for DB based updates
        //add the DB ID
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OTHER_ACHIEVEMENT_UPDATE);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Merge two segments
     * @param segment The segment we want to merge into our segment
     */
    public void mergeTimelineSegments(TimelineSegment segment){

        //REST Call to remove the last location point
        DataUpdater.getInstance().deleteLocationEntry(this.myLocationPoints.getLast());

        //Remove the last location point since it is double in both segment
        this.myLocationPoints.removeLast();

        //merge all the remaining data
        this.myLocationPoints.addAll(segment.getLocationPoints());
        this.myAchievements.addAll(segment.getMyAchievements());
        this.userComments.addAll(segment.getUserComments());

        //REST Call to add all new location points
        for(LocationEntry entry : segment.getLocationPoints()){
            DataUpdater.getInstance().addLocationEntry(entry, this);
        }


        //check which activity type our activity is
        switch(segment.getMyActivity().getType())
        {
            //if we they are active activities simply add statistics
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
                //if it is a inactive activity add to the inactive times
                this.inactiveDistance = segment.getActiveDistance() + segment.getInactiveDistance();
                this.inactiveTime = segment.getActiveTime() + segment.getInactiveTime();
                this.userSteps += segment.getUserSteps();
                Log.i(TAG, "Adding inactive time: " +(  segment.getInactiveTime()));
                Log.i(TAG, "Adding inactive distance: " +(  segment.getInactiveDistance()));
        }

        this.calculateAchievements();

        //REST Call to update our segment
        DataUpdater.getInstance().updateTimelineSegment(this);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Get the Steps of the user
     * @return The User Steps
     */
    public int getUserSteps() {
        return userSteps;
    }

    /**
     * Get the images that are associated with the segment
     * @return A list containing all the images
     */
    public LinkedList<Image> getMyImages() {
        return myImages;
    }

    /**
     * Delete an image from the list
     * @param index The index of the image that should be deleted
     */
    public void deleteImage(int index){
        //check if the index is valid
        if(index < 0 || index >= this.myImages.size()){
            return;
        }

        this.myImages.remove(index);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Add an image from the DB
     * @param image The image to add
     * @param id  The DB ID of the owning user
     */
    public void addImage(Image image, String id) {
        this.myImages.add(image);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OTHER_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, id);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }


    /**
     * Add am image to the segment
     * @param image The image to add to the list
     */
    public void addImages(Image image) {
        if(image != null) {
            this.myImages.add(image);

            //Send intent to inform about update
            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED);
            intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }
    }

    /**
     * Used in Case the user wants to change the activity, can only be called from the TimelineDay
     * since we might have to merge
     * @param myActivity The activity we want to set
     */
    void setMyActivity(DetectedActivity myActivity) {
        Log.i(TAG, "setMyActivity: Set activity to: " + myActivity.getType());
        //if new activity is not a sport set the active counters to 0
        if(myActivity.getType() == DetectedActivity.STILL ||
                myActivity.getType() == DetectedActivity.UNKNOWN ||
                myActivity.getType() == DetectedActivity.IN_VEHICLE){
            this.inactiveTime += this.activeTime;
            this.inactiveDistance += this.activeDistance;
            this.activeDistance = 0;
            this.activeTime = 0;
        }

        //if old activity is not a sport one correct the counters
        if(this.myActivity.getType() == DetectedActivity.STILL ||
                this.myActivity.getType() == DetectedActivity.UNKNOWN ||
                this.myActivity.getType() == DetectedActivity.IN_VEHICLE){
            this.activeTime += this.inactiveTime;
            this.activeDistance += this.inactiveDistance;
            this.inactiveDistance = 0;
            this.inactiveTime = 0;
        }

        this.myActivity = myActivity;

        //Recalculate Achievements
        this.myAchievements = new LinkedList<>();
        this.calculateAchievements();

        //REST Call to update the segment in the DB
        UpdateOperations_Synchron.updateTimelineSegmentManually(this);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }


    /**
     * Set the start place of the segment
     * @param startPlace The start place that should be set
     */
    public void setPlace(String startPlace) {

        this.startPlace = startPlace;

        //REST Call to update the Segment
        DataUpdater.getInstance().updateTimelineSegment(this);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }


    /**
     * Change the Place from the DB
     * @param place The place to set
     * @param id  The DB ID of the owning user
     */
    public void setPlace(String place, String id) {
        this.startPlace = place;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OTHER_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, id);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Set the start address of the segment
     * @param startAddress The start address that should be set
     */
    public void setAddress(String startAddress) {

        this.startAddress = startAddress;

        //REST Call to update the Segment
        DataUpdater.getInstance().updateTimelineSegment(this);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }


    /**
     * Set the address from the DB
     * @param address The address to set
     * @param id  The DB ID of the owning user
     */
    public void setAddress(String address, String id) {
        this.startAddress = address;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_SEGMENT_INTENT_OTHER_INFO_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_SEGMENT_DATE, this.startTime);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, id);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Get the locations of the segment
     * @return A list containing all segments of the segment
     */
    public LinkedList<LocationEntry> getMyLocationPoints() {
        return myLocationPoints;
    }

    /**
     * Get the activity of the segment
     * @return The activity
     */
    public DetectedActivity getMyActivity() {
        return myActivity;
    }

    /**
     * Get the inactive distance, so the distance the user was inactive of the segment
     * @return The inactive distance
     */
    public double getInactiveDistance() {
        return inactiveDistance;
    }

    /**
     * Get the start place for the segment
     * @return The start place
     */
    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
         this.startPlace = startPlace;
    }

    /**
     * Get the start address of the segment
     * @return The start address
     */
    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }
    /**
     *  Get all user comments associated with the segment
     * @return A list containing all user comments
     */
    public LinkedList<UserComment> getUserComments() {
        return new LinkedList<>(userComments);
    }

    /**
     * Get the active distance, so the distance the user was actively doing something
     * @return The active distance
     */
    public double getActiveDistance() {
        return activeDistance;
    }

    /**
     * Get the active time of the segment
     * @return The active time
     */
    public long getActiveTime() {
        return activeTime;
    }

    /**
     * Get all the achievements of the segment
     * @return A list containing all achievements
     */
    public LinkedList<Achievement> getMyAchievements() {
        return new LinkedList<>(myAchievements);
    }

    /**
     * Get the inactive time of the segment
     * @return The inactive time
     */
    public long getInactiveTime() {
        return inactiveTime;
    }

    /**
     * Get the Address that was detected for the segment
     * @return The address the segment started
     */
    public String getAddress() {
        return startAddress;
    }

    /**
     * Get the place that was detected for the segment
     * @return The place the segment started
     */
    public String getPlace() {
        return startPlace;
    }

    /**
     * Get the location points of the segment
     * @return A list containing all segments
     */
    public LinkedList<LocationEntry> getLocationPoints() {
        return new LinkedList<>(this.myLocationPoints);
    }

    /**
     * Get the start time of the first point
     * @return The start time of the first segment
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Retrieve the database ID
     * @return The database ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the Datatbase ID
     * @param ID The new Database ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}

