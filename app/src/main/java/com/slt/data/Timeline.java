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
     * Get the Steps of the timeline
     * @return The steps of the user
     */
    public int getSteps() {
        int steps = 0;

        //loop over all segments to count all steps
        for(TimelineDay day : this.myHistory){
            steps += day.getSteps();
        }

        return steps;
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
     * Get all Timeline Days for the selected month or week
     * @param referenceDate The date for the week we want the data for
     * @param mode The mode of the method, 0 returns all days of the week, 1 for the month
     * @return A linked list containing all TimelineDays
     */
    private  LinkedList<TimelineDay> getDaysOfWeekOrMonth(Date referenceDate, int mode) {

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //choose if we need all days of the week or month
        if( 0 == mode) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }

        LinkedList<TimelineDay> daysOfWeek = new LinkedList<>();

        //get the TimelineDays
        for (int i = 0; i < calendar.getActualMaximum(Calendar.DATE); i++) {
            for(TimelineDay day: this.myHistory) {
                if(day.isSameDay(calendar.getTime())) {
                    daysOfWeek.add(day);
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    /**
     * Get the date of the current TimelineDay
     * @return The date
     */
    private Date getLastDate(){
        return this.myHistory.getLast().getMyDate();
    }

    /**
     * Get the Month or Week Achievements
     * @return The achivements
     */
    public LinkedList<Achievement> getMyAchievements() {
        return myAchievements;
    }

    /**
     * Calculate the achievements to see if we have any new achievements
     * @param day A day of the week we want to calculate achievements for
     */
    public void calculateAchievements(Date day){

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);

        int monthLength = calendar.getActualMaximum(Calendar.DATE);

        LinkedList<Achievement> achievements = AchievementCalculator
                .calculateWeekAchievements(this.getDaysOfWeekOrMonth(day, 0), this.myAchievements);

        this.myAchievements.addAll(achievements);

        achievements = AchievementCalculator.calculateMonthAchievements(
                this.getDaysOfWeekOrMonth(day, 1),
                this.myAchievements, monthLength);
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
        this.calculateAchievements(date);
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
