package com.slt.data;

import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.nearby.messages.Distance;
import com.slt.control.AchievementCalculator;
import com.slt.control.AddressResolver;
import com.slt.control.ApplicationController;
import com.slt.control.PlacesResolver;
import com.slt.definitions.Constants;
import com.slt.restapi.DataUpdater;
import com.slt.restapi.UpdateOperations;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedList;



/**
 * Created by Thorsten on 25.11.2017.
 */

public class TimelineDay {

    /**
     * Implementation of a comparator used to sort the location points by time
     */
    public class SortTimelineSegment implements Comparator<TimelineSegment> {
        /**
         * Overwritten comparision method
         * @param seg1 The first segment to compare
         * @param seg2 The second segment to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(TimelineSegment seg1, TimelineSegment seg2) {
            //get the values
            Date d1 = seg1.getStartTime();
            Date d2 = seg2.getStartTime();

            //return the comparision results
            if(d1.equals(d2))
                return 0;
            if(d1.after(d2))
                return 1;
            else
                return -1;
        }
    }


    /*
    * Tag for the Logger
    */
    private static final String TAG = "TimelineDay";

    /**
     * List containing all segments of the day
     */
    private LinkedList<TimelineSegment> mySegments;

    /**
     * Definition how long a segment has to be minimal
     */
    private static final long MIN_SEGMENT_DURATION_IN_SECONDS  = 120;

    /**
     * The date of the Timeline Day
     */
    private Date myDate;

    /**
     * The day achievements
     */
    private LinkedList<Achievement> myAchievements;

    /**
     * Database ID
     */
    private String ID;

    /**
     *  Constructor initializes the data
     * @param myDate The day of the TimelineDay
     */
    public TimelineDay(Date myDate) {
        this.mySegments = new LinkedList<>();
        this.ID = null;

        //Truncate date to the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date truncatedDate = calendar.getTime();
        this.myDate = truncatedDate;

        this.myAchievements = new LinkedList<>();
    }

    /**
     * Calculate the achievements for the day
     */
    public void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator
                .calculateDayAchievements(this.mySegments, this.myAchievements);
        Log.i(TAG, "calculateAchievements: In method.");
        this.myAchievements.addAll(achievements);

        //if new achievements -> send intent
        if(!achievements.isEmpty()){
            //REST Call to update the DB
            DataUpdater.getInstance().updateTimelineDay(this);

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OWN_ACHIEVEMENT_UPDATE);
            intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }
    }

    /**
     * Method can be used to update the achievements in case there was a change from the Database
     * @param achievement The new achievement we want to add
     * @param userID The DB ID of the user the Day is from
     */
    public void addAchievement(Achievement achievement, String userID) {
        this.myAchievements.add(achievement);

        //Send intent to inform about update, since this method should only be used for DB based updates
        //add the DB ID
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OTHER_ACHIEVEMENT_UPDATE);
        intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }


    /**
     * Get the active time for a activity of the day
     * @param activity The activity we want to have the active time for
     * @return The active time
     */
    public long getActiveTime(DetectedActivity activity){
        long time = 0;
        Log.i(TAG, "getActiveTime: In method.");

        //loop over all segments to find all data for the selected activity
        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                time += segment.getActiveTime();
            }
        }

        return time;
    }

    /**
     * Insert Segement, used if Data is synchronized with the server
     * @param segment The segment we want to add
     * @param userID The DB ID of the user the Day is from
     */
    public void insertTimelineSegment(TimelineSegment segment, String userID){

        //remove sensor from last segment if there is one
        if (!this.mySegments.isEmpty()) {
            this.mySegments.getLast().deleteSensor();
        }

        this.mySegments.add(segment);

        //sort in case the order the segments were added is wrong
        Collections.sort(this.mySegments, new SortTimelineSegment());

        //if we are on the current day
        if(this.isSameDay(new Date())) {
            //add a sensor
            this.mySegments.getLast().addSensor();
        }

        //Send intent to inform about update, since this method should only be used for DB based updates
        //add the DB ID
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OTHER_SEGMENTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Delete a segment, used if the data is synchronized from the server
     * @param ID The DB ID of the segment we want to delete
     * @param userID The DB ID of the user the Day is from
     */
    public void deleteTimelineSegment(String ID, String userID){
        TimelineSegment segment = null;

        //find segment with ID
        for(TimelineSegment sgt: this.mySegments){
            if(ID == sgt.getID()){
                segment = sgt;
            }
        }

        if(segment == null)
            return;

        //remove segment
        this.mySegments.remove(segment);

        //Send intent to inform about update, since this method should only be used for DB based updates
        //add the DB ID
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OTHER_SEGMENTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Get the inactive time for a activity of the day
     * @param activity The activity we want to have the inactive time for
     * @return The inactive time
     */
    public long getInactiveTime(DetectedActivity activity){
        long time = 0;
        Log.i(TAG, "getInactiveTime: In method.");

        //loop over all segments to find all data for the selected activity
        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                time += segment.getInactiveTime();
            }
        }

        return time;
    }

    /**
     * Get the active distance for a activity of the day
     * @param activity The activity we want to have the active distance for
     * @return The active distance
     */
    public double getActiveDistance(DetectedActivity activity) {
        double distance = 0;
        Log.i(TAG, "getActiveDistance: In method.");

        //loop over all segments to find all data for the selected activity
        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                distance += segment.getActiveDistance();
            }
        }

        return distance;
    }

    /**
     * Get the inactive distance for a activity of the day
     * @param activity The activity we want to have the inactive distance for
     * @return The inactive distance
     */
    public double getInactiveDistance(DetectedActivity activity) {
        double distance = 0;
        Log.i(TAG, "getInactiveDistance: In method.");

        //loop over all segments to find all data for the selected activity
        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null){
                distance += segment.getInactiveDistance();
            }
        }

        return distance;
    }

    /**
     * Get the Steps of the days
     * @return The steps of the user
     */
    public int getSteps() {
        int steps = 0;
        Log.i(TAG, "getSteps: In method.");

        //loop over all segments to count all steps
        for(TimelineSegment segment : this.mySegments){
            steps += segment.getUserSteps();
        }

        return steps;
    }

    /**
     * Get the total distance for a activity of the day
     * @param activity The activity we want to have the total distance for
     * @return The total distance
     */
    public double getTotalDistance(DetectedActivity activity){
        return this.getActiveDistance(activity)+ getInactiveDistance(activity);
    }

    /**
     * Get the total time for a activity of the day
     * @param activity The activity we want to have the total time for
     * @return The total time
     */
    public long getTotalTime(DetectedActivity activity) {
        return this.getActiveTime(activity) + this.getInactiveTime(activity);
    }

    /**
     * Compare if the date give is on the same day as the TimelineDay
     * @param date The date we want to use to compare
     * @return True if it is the same day, false if not
     */
    public boolean isSameDay(Date date){
        //check if we have a day to compare
        if(date == null){
            Log.i(TAG, "isSameDay: date is null");
            return false;
        }

        //truncate the date to the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date truncatedDate = calendar.getTime();

        return myDate.compareTo(truncatedDate) == 0;
    }

    /**
     * Get all Timeline Segments
     * @return The Timeline Segments
     */
    public LinkedList<TimelineSegment> getMySegments() {
        return mySegments;
    }

    /**
     * Change the Segment with the defined index to the activity. Also checks if the activity before
     * or after is of the same activity type and merge if that is the case
     * @param activity The activity that should be set
     * @param index The index of the TimelineSegment we want to set
     */
    public void changeActivity(DetectedActivity activity, int index){
        //check if index is out of bounds
        if(index < 0 || index >= this.mySegments.size()){
            Log.i(TAG, "changeActivity: Out of bounds.");
            return;
        }

        //set the new activity
        Log.i(TAG, "changeActivity: Change Activity: " + activity);
        this.mySegments.get(index).setMyActivity(activity);

        //if we have only one segment there is nothing to do
        if(this.mySegments.size() == 1){
            Log.i(TAG, "changeActivity: Only one element, nothing to do.");
            return;
        }

        //check if elements before the index exist, then merge the segments
        if(index-1 >= 0){
            if(this.mySegments.get(index-1).getMyActivity().getType() == activity.getType()){
                Log.i(TAG, "changeActivity: Merge with previous element.");
                TimelineSegment mergeSegment = this.mySegments.get(index);
                this.mySegments.get(index-1).mergeTimelineSegments(mergeSegment);

                //REST Call to update the DB
                DataUpdater.getInstance().deleteTimelineSegment(this.mySegments.get(index));

                this.mySegments.remove(index);
                index--;
            }
        }

        //check if elements after the index exist, if yes then merge the segments
        if(index+1 < this.mySegments.size()){
            if(this.mySegments.get(index+1).getMyActivity().getType() == activity.getType()){
                Log.i(TAG, "changeActivity: Merge with next element.");
                TimelineSegment mergeSegment = this.mySegments.get(index+1);
                this.mySegments.get(index).mergeTimelineSegments(mergeSegment);

                //REST Call to update the DB
                DataUpdater.getInstance().deleteTimelineSegment(this.mySegments.get(index+1));

                this.mySegments.remove(index+1);
            }
        }

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OWN_SEGMENTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Manually starts a new segment if the user wants to
     * @param location The location that should be added
     * @param date The date the location/activity was detected
     * @param activity The activity that was detected
     */
    public void manualStartNewSegment(Location location, Date date, DetectedActivity activity){
        Log.i(TAG, "ManualAddUserStatus:  create new Segment, location resolution.");
        TimelineSegment next = new TimelineSegment(activity, date, true);
        this.mySegments.add(next);

        //REST Call to add new segment to DB
        DataUpdater.getInstance().addTimeLineSegment(this.mySegments.getLast(), this);

        next.addLocationPoint(location,date);

        Object[] ResolutionData = new Object[2];
        ResolutionData[0] = this.mySegments.getLast();
        ResolutionData[1] = location;

        AddressResolver addressResolver = new AddressResolver();
        addressResolver.execute(ResolutionData);

        PlacesResolver placesResolver = new PlacesResolver();
        placesResolver.execute(ResolutionData);
        this.calculateAchievements();


        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OWN_SEGMENTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Add a new location point to a manual segment
     * @param location The location that should be added
     * @param date The date the location/activity was detected
     */
    public void manualAddLocation(Date date, Location location){
        this.mySegments.getLast().addLocationPoint(location, date);
    }

    /**
     * End a manually created segment by user choice
     * @param location The location that should be added
     * @param date The date the location/activity was detected
     */
    public void manualEndSegment(Date date, Location location){
        DetectedActivity activity = new DetectedActivity(DetectedActivity.UNKNOWN, 100);
        Log.i(TAG, "ManualEndSegment:  create new Segment, end last one.");
        TimelineSegment next = new TimelineSegment(activity, date, true);
        this.mySegments.add(next);
        next.addLocationPoint(location, date);

        //REST Call to add new segment to DB
        DataUpdater.getInstance().addTimeLineSegment(this.mySegments.getLast(), this);
    }

    /**
     * Removes the step sensor from the last segment
     */
    void removeStepSensor() {
        if(!this.mySegments.isEmpty()) {
            this.mySegments.getLast().deleteSensor();
        }
    }

    /**
     * Add a user status to the segment or create a new segment if needed
     * @param location The location that should be added
     * @param date The date the location/activity was detected
     * @param activity The activity that was detected
     */
    public void addUserStatus(Location location, Date date, DetectedActivity activity){

        //check if we have segments in the history, if not add and start place and address resolution
        if(this.mySegments.isEmpty()){
            Log.i(TAG, "addUserStatus: Segements empty, create new Segment, location resolution.");
            TimelineSegment next = new TimelineSegment(activity, date, false);
            this.mySegments.add(next);

            //REST Call to add new segment to DB
            DataUpdater.getInstance().addTimeLineSegment(next, this);

            //adds the segment and starts the step detection
            next.addSensor();
            next.addLocationPoint(location, date);


            //search for location and address
            Object[] ResolutionData = new Object[2];
            ResolutionData[0] = this.mySegments.getLast();
            ResolutionData[1] = location;

            AddressResolver addressResolver = new AddressResolver();
            addressResolver.execute(ResolutionData);

            PlacesResolver placesResolver = new PlacesResolver();
            placesResolver.execute(ResolutionData);
            this.calculateAchievements();
            return;
        }

        //add the location to the last segment in our history
        this.mySegments.getLast().addLocationPoint(location, date);

        //remove the step sensor since it might not be the current segment afterwards, current steps
        //have been saved by the addLocationPoint Call
        this.mySegments.getLast().deleteSensor();

        //if it is a new activity
        if(!this.mySegments.getLast().compareActivities(activity)){
            Log.i(TAG, "addUserStatus: New activity detected.");

            if(this.mySegments.getLast().getDuration()
                    < MIN_SEGMENT_DURATION_IN_SECONDS ) {
                Log.i(TAG, "addUserStatus: Last segment too short.");

                if(this.mySegments.size() == 1) {
                    //if we only have a single activity the detection might have been wrong at the
                    // beginning, so simply reset it to the new one
                    Log.i(TAG, "addUserStatus: Only 1 element in history, change activity.");
                    this.mySegments.getLast().setMyActivity(activity);
                }

                if(this.mySegments.size() > 2){
                    int lastSegmentIndex = this.mySegments.size() -2;
                    if(this.mySegments.get(lastSegmentIndex).compareActivities(activity)){
                        //if the previous one is the same as the current one merge all thre
                        Log.i(TAG, "addUserStatus: Previous activity same as current merge segments.");
                        TimelineSegment segment = this.mySegments.getLast();
                        this.mySegments.removeLast();
                        this.mySegments.getLast().mergeTimelineSegments(segment);

                        //REST Call to delete the last segment
                        DataUpdater.getInstance().deleteTimelineSegment(segment);
                    } else {
                        //if they are not the same simply change activity since something has
                        //changed since the previous element
                        Log.i(TAG, "addUserStatus: New activity not same as previous one, change activity.");
                        this.mySegments.getLast().setMyActivity(activity);
                    }
                }
            }

            //check if new activity, if yes add and start place/address resolution
            if(this.mySegments.peekLast().getMyActivity().getType() != activity.getType()) {
                Log.i(TAG, "addUserStatus: new Activity, new Segment created, create new Segment.");
                TimelineSegment nextSegment = new TimelineSegment(activity, date, false);
                this.mySegments.add(nextSegment);

                //REST Call to add new segment to DB
                DataUpdater.getInstance().addTimeLineSegment(nextSegment, this);

                //Add a new location point
                nextSegment.addLocationPoint(location, date);

                Object[] ResolutionData = new Object[2];
                ResolutionData[0] = this.mySegments.getLast();
                ResolutionData[1] = location;

                AddressResolver addressResolver = new AddressResolver();
                addressResolver.execute(ResolutionData);

                PlacesResolver placesResolver = new PlacesResolver();
                placesResolver.execute(ResolutionData);
            }
        }

        //add a step sensor to the last segment
        this.mySegments.getLast().addSensor();

        this.calculateAchievements();

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_DAY_INTENT_OWN_SEGMENTS_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.TIMELINE_DAY_DATE, this.myDate);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     *  Get the segment defined by the index
     * @param index The index we want the segment of
     * @return The TimelineSegment
     */
    public TimelineSegment getSegment(int index) {
        //check if the index is out of bounds
        if(index < 0  || index >= this.mySegments.size()){
            Log.i(TAG, "getSegment: Index out of bounds.");
            return null;
        }
        return mySegments.get(index);
    }

    /**
     * Get the date of the TimelineDay
     * @return The date
     */
    public Date getMyDate() {
        return myDate;
    }

    /**
     * Get the achievements of the TimelineDay
     * @return A list with all achievements for the day
     */
    public LinkedList<Achievement> getMyAchievements() {
        return new LinkedList<>(myAchievements);
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
