package com.slt.data;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.AchievementCalculator;

import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * A Timeline contains all data of a
 */
public class Timeline {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "Timeline";

    /**
     * Linked list containing all days of the timeline
     */
    private LinkedList<TimelineDay> myHistory;

    /**
     * The week and month achievements associated with the timeline
     */
    private LinkedList<Achievement> myAchievements;

    /**
     * Constructor, initializes the list
     */
    public Timeline(){
        myHistory = new LinkedList<TimelineDay>();
        myAchievements = new LinkedList<>();
    }

    /**
     * Get active time of a certain activity and day
     * @param activity Activity we want to retrieve the active time for
     * @param checkDay Day we want to retrieve the active time for
     * @return
     */
    public long getActiveTime(DetectedActivity activity, Date checkDay) {
        long time = 0;

        //go through all days in our list
        for(TimelineDay day : this.myHistory){
            //check if is the same day
            if(day.isSameDay(checkDay)){
                time += day.getActiveTime(activity);
            }
        }

        return time;
    }

    /**
     * Get inactive time of a certain activity and day
     * @param activity Activity we want to retrieve the inactive time for
     * @param checkDay Day we want to retrieve the inactive time for
     */
    public long getInactiveTime(DetectedActivity activity, Date checkDay) {
        long time = 0;

        //go through all days in our list
        for(TimelineDay day : this.myHistory){
            //check if is the same day
            if(day.isSameDay(checkDay)){
                time += day.getInactiveTime(activity);
            }
        }

        return time;
    }

    /**
     * Get active distance of a certain activity and day
     * @param activity Activity we want to retrieve the active distance for
     * @param checkDay Day we want to retrieve the active distance for
     * @return
     */
    public double getActiveDistance(DetectedActivity activity, Date checkDay) {
        double distance = 0;

        //go through all days in our list
        for(TimelineDay day : this.myHistory){
            //check if is the same day
            if(day.isSameDay(checkDay)){
                distance += day.getActiveDistance(activity);
            }
        }

        return distance;
    }

    /**
     *
     * Get inactive distance of a certain activity and day
     * @param activity Activity we want to retrieve the inactive distance for
     * @param checkDay Day we want to retrieve the inactive distance for
     */
    public double getInactiveDistance(DetectedActivity activity, Date checkDay) {
        double distance = 0;

        //go through all days in our list
        for(TimelineDay day : this.myHistory){
            //check if is the same day
            if(day.isSameDay(checkDay)){
                distance += day.getInactiveDistance(activity);
            }
        }

        return distance;
    }

    /**
     * Get total distance of a certain activity and day
     * @param activity Activity we want to retrieve the total distance for
     * @param checkDay Day we want to retrieve the total distance for
     * @return
     */
    public double getTotalDistance(DetectedActivity activity, Date checkDay){
        return this.getActiveDistance(activity, checkDay)+ getInactiveDistance(activity, checkDay);
    }

    /**
     *
     * Get total time of a certain activity and day
     * @param activity Activity we want to retrieve the total time for
     * @param checkDay Day we want to retrieve the total time for
     */
    public long getTotalTime(DetectedActivity activity, Date checkDay){
        return this.getActiveTime(activity, checkDay) + this.getInactiveTime(activity, checkDay);
    }

    /**
     * Calculate the achievements to see if we have any new achievements
     */
    public void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator
                .calculateWeekAchievements(this.myHistory, this.myAchievements);

        this.myAchievements.addAll(achievements);

        achievements = AchievementCalculator.calculateMonthAchievements(this.myHistory,
                this.myAchievements);
        this.myAchievements.addAll(achievements);
    }

    /**
     * Used to add a new location/activity to our timeline
     * @param location The location we want to add
     * @param date The date the location was detected
     * @param activity The activity that was detected
     */
    public void addUserStatus(Location location, Date date, DetectedActivity activity) {
        //check if we have a day already
        if (this.myHistory.size() == 0){
            this.myHistory.add(new TimelineDay(date));
        }

        //check if we still have the same day
        if(!this.myHistory.getLast().isSameDay(date)) {
            this.myHistory.add(new TimelineDay(date));
        }

        this.myHistory.getLast().addUserStatus(location, date, activity);
        this.calculateAchievements();
    }

    /**
     * Get the month and week achievements for the timeline
     * @return A list containing all achievements
     */
    public LinkedList<Achievement> getAchievements(){
        return new LinkedList<>(this.myAchievements);
    }

    /**
     * Get the size of the history, so how many days are already in storage
     * @return The size of the history
     */
    public int getHistorySize(){
        return this.myHistory.size();
    }

    /**
     * Get a specific day of the history
     * @param index The index for which we want to retrieve the day for
     * @return The TimelineDay
     */
    public TimelineDay getTimelineDay(int index){
        //check if index is out of bounds
        if(index < 0 || index >= this.myHistory.size()){
            Log.i(TAG, "getTimelineDay: Index out of bounds.");
            return null;
        }

        return this.myHistory.get(index);
    }
}
