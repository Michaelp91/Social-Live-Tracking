package com.slt.data;

import android.location.Location;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.nearby.messages.Distance;
import com.slt.control.AchievementCalculator;
import com.slt.control.AddressResolver;
import com.slt.control.PlacesResolver;

import java.util.Date;
import java.util.Calendar;
import java.util.LinkedList;



/**
 * Created by Thorsten on 25.11.2017.
 */

public class TimelineDay {
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
     *  Constructor initializes the data
     * @param myDate The day of the TimelineDay
     */
    public TimelineDay(Date myDate) {
        this.mySegments = new LinkedList<>();

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

        this.myAchievements.addAll(achievements);
    }

    /**
     * Get the active time for a activity of the day
     * @param activity The activity we want to have the active time for
     * @return The active time
     */
    public long getActiveTime(DetectedActivity activity){
        long time = 0;

        //loop over all segments to find all data for the selected activity
        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                time += segment.getActiveTime();
            }
        }

        return time;
    }

    /**
     * Get the inactive time for a activity of the day
     * @param activity The activity we want to have the inactive time for
     * @return The inactive time
     */
    public long getInactiveTime(DetectedActivity activity){
        long time = 0;

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
     * Merge the last segments
     */
    public void mergeLastSegment(){
        //check if we have enough entries in the history
        if(this.mySegments.size() < 2){
            Log.i(TAG, "mergeLastSegment: Only one element, nothing to merge.");
            return;
        }
        int index = this.mySegments.size() -1;
        this.mergeSegments(index);
    }

    /**
     * Merge a segment that is defined by the index with the segment before that segment
     * @param index
     */
    public void mergeSegments(int index){
        //check if a valid index is given
        if(index >= this.mySegments.size() || index < 1){
            Log.i(TAG, "mergeSegments: Out of Bounds.");
            return;
        }

        //check if there are enough segments in the history
        if(this.mySegments.size() < 2 ){
            Log.i(TAG, "mergeSegments: Only one element, nothing to merge.");
            return;
        }

        //get and merge the defined segments
        TimelineSegment current = mySegments.get(index);
        mySegments.get(index-1).mergeTimelineSegments(current);

        //remove the segment and recalculate the achievements
        mySegments.remove(index);
        this.calculateAchievements();
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
     * Add a user status to the
     * @param location The location that should be added
     * @param date The date the location/activity was detected
     * @param activity The activity that was detected
     */
    public void addUserStatus(Location location, Date date, DetectedActivity activity){

        //check if we have segments in the history, if not add and start place and address resolution
        if(this.mySegments.isEmpty()){
            this.mySegments.add(new TimelineSegment(location, date, activity));
            Object[] ResolutionData = new Object[2];
            ResolutionData[0] = this.mySegments.getLast();
            ResolutionData[1] = location;

            AddressResolver addressResolver = new AddressResolver();
            addressResolver.execute(ResolutionData);

            PlacesResolver placesResolver = new PlacesResolver();
            placesResolver.execute(ResolutionData);
            return;
        }

        //add the location to the last segment in our history
        this.mySegments.getLast().addLocationPoint(location, date);

        //if it is a new activity
        if(!this.mySegments.getLast().compareActivities(activity)){

            //TODO CHECK IF IT WORKS

            //if we have at least 3 segments in history
            if(this.mySegments.size() >= 3) {
                int lastSegmentIndex = this.mySegments.size() -2;

                //TODO what to do with same activities - merge?
                //check if last segment has a min duration and is a specific activity
                if(this.mySegments.get(lastSegmentIndex).getDuration()
                        >= MIN_SEGMENT_DURATION_IN_SECONDS && (
                        this.mySegments.peekLast().getMyActivity().getType()
                                == DetectedActivity.STILL ||
                                this.mySegments.peekLast().getMyActivity().getType()
                                == DetectedActivity.TILTING ||
                                this.mySegments.peekLast().getMyActivity().getType()
                                == DetectedActivity.UNKNOWN) ){
                    TimelineSegment lastSegment = this.mySegments.pop();
                    this.mySegments.peekLast().mergeTimelineSegments(lastSegment);
                }

            }

            //check if new activity, if yes add and start place/address resolution
            if(this.mySegments.peekLast().getMyActivity().getType() != activity.getType()) {
                TimelineSegment nextSegment = new TimelineSegment(location, date, activity);
                this.mySegments.add(nextSegment);

                Object[] ResolutionData = new Object[2];
                ResolutionData[0] = this.mySegments.getLast();
                ResolutionData[1] = location;

                AddressResolver addressResolver = new AddressResolver();
                addressResolver.execute(ResolutionData);

                PlacesResolver placesResolver = new PlacesResolver();
                placesResolver.execute(ResolutionData);
            }
        }

        this.calculateAchievements();
    }

    /**
     * Get the size of the segment history
     * @return The size of the segment list
     */
    public int getSegmentSize(){
        return mySegments.size();
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
}
