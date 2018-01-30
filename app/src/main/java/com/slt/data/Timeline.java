package com.slt.data;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.AchievementCalculator;
import com.slt.control.ApplicationController;
import com.slt.definitions.Constants;
import com.slt.restapi.DataUpdater;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * A Timeline contains all data of a
 */
public class Timeline {


    /**
     * Implementation of a comparator used to sort the location points by time
     */
    public class SortTimelineDay implements Comparator<TimelineDay> {
        /**
         * Overwritten comparision method
         *
         * @param day1 The first day to compare
         * @param day2 The second day to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(TimelineDay day1, TimelineDay day2) {
            //get the values
            Date d1 = day1.getMyDate();
            Date d2 = day2.getMyDate();

            //return the comparision results
            if (d1.equals(d2))
                return 0;
            if (d1.after(d2))
                return 1;
            else
                return -1;
        }
    }

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
     * Database ID
     */
    private String ID;

    /**
     * Constructor, initializes the list
     */
    public Timeline() {
        myHistory = new LinkedList<TimelineDay>();
        myAchievements = new LinkedList<>();
        this.ID = null;
    }

    /**
     * Get active time of a certain activity and day
     *
     * @param activity Activity we want to retrieve the active time for
     * @param checkDay Day we want to retrieve the active time for
     * @return
     */
    public long getActiveTime(DetectedActivity activity, Date checkDay) {
        long time = 0;

        //go through all days in our list
        for (TimelineDay day : this.myHistory) {
            //check if is the same day
            if (day.isSameDay(checkDay)) {
                time += day.getActiveTime(activity);
            }
        }

        return time;
    }

    /**
     * Get the active time for the current month
     *
     * @return The active distance
     */
    public long getActiveTimeForMonth() {
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 1);

        long time = 0;
        DetectedActivity biking = new DetectedActivity(DetectedActivity.ON_BICYCLE, 100);
        DetectedActivity walking = new DetectedActivity(DetectedActivity.WALKING, 100);
        DetectedActivity foot = new DetectedActivity(DetectedActivity.ON_FOOT, 100);
        DetectedActivity running = new DetectedActivity(DetectedActivity.RUNNING, 100);

        //loop over all week days to count all times
        for (TimelineDay day : week) {
            time += day.getActiveTime(biking);
            time += day.getActiveTime(walking);
            time += day.getActiveTime(foot);
            time += day.getActiveTime(running);
        }

        return time;
    }

    /**
     * Get the number of achievements in the last week
<<<<<<< HEAD
     * @return achievements from last week
=======
     *
     * @return The number of achievements
>>>>>>> d4d817a80d74905efb59914e40cde7496881d67d
     */
    public LinkedList<Achievement> getAchievementsListForWeek() {
        int achievementPoints = 0;
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 0);

        return AchievementCalculator.calculateWeekAchievements(week, new LinkedList<Achievement>());
    }

    public LinkedList<Achievement> getAchievementsListForDay() {
        return this.myHistory.getLast().getMyAchievements();
    }

        /**
         * Get the number of achievements in the last week
         * @return The number of achievements
         */
    public int getAchievementsForWeek() {
        int achievementPoints = 0;
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 0);

        //sum up all achievements of the week
        for (TimelineDay day : week) {
            achievementPoints += day.getMyAchievements().size();

            for (TimelineSegment segment : day.getMySegments()) {
                achievementPoints += segment.getMyAchievements().size();
            }
        }

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        //   calendar.set(Calendar.DAY_OF_MONTH, 1);calendar.getActualMaximum(Calendar.DATE)

        //get the TimelineDays
        for (int i = 0; i < 7; i++) {
            for (Achievement achievement : this.myAchievements) {
                if (achievement.isSameDay(calendar.getTime())) {
                    achievementPoints += 1;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return achievementPoints;
    }

    /**
<<<<<<< HEAD
     * Get the achievements in the last month
     * @return achievements in last month
=======
     * Get the number of achievements in the last month
     *
     * @return The number of achievements
>>>>>>> d4d817a80d74905efb59914e40cde7496881d67d
     */
    public LinkedList<Achievement> getAchievementsListForMonth() {
        int achievementPoints = 0;
        Date current = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //get days of week
        LinkedList<TimelineDay> month = this.getDaysOfWeekOrMonth(current, 1);

        int monthLength = calendar.getActualMaximum(Calendar.DATE);

        return AchievementCalculator.calculateMonthAchievements(month, new LinkedList<Achievement>(), monthLength);
    }

        /**
         * Get the number of achievements in the last month
         * @return The number of achievements
         */
    public int getAchievementsForMonth() {
        int achievementPoints = 0;
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 1);

        //sum up all achievements of the week
        for (TimelineDay day : week) {
            achievementPoints += day.getMyAchievements().size();

            for (TimelineSegment segment : day.getMySegments()) {
                achievementPoints += segment.getMyAchievements().size();
            }
        }

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        //get the TimelineDays
        for (int i = 0; i < calendar.getActualMaximum(Calendar.DATE); i++) {
            for (Achievement achievement : this.myAchievements) {
                if (achievement.isSameDay(calendar.getTime())) {
                    achievementPoints += 1;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return achievementPoints;
    }

    /**
     * Get the active time for the current week
     *
     * @return The active distance
     */
    public long getActiveTimeForWeek() {
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 0);

        long time = 0;
        DetectedActivity biking = new DetectedActivity(DetectedActivity.ON_BICYCLE, 100);
        DetectedActivity walking = new DetectedActivity(DetectedActivity.WALKING, 100);
        DetectedActivity foot = new DetectedActivity(DetectedActivity.ON_FOOT, 100);
        DetectedActivity running = new DetectedActivity(DetectedActivity.RUNNING, 100);

        //loop over all week days to count all times
        for (TimelineDay day : week) {
            time += day.getActiveTime(biking);
            time += day.getActiveTime(walking);
            time += day.getActiveTime(foot);
            time += day.getActiveTime(running);
        }

        return time;
    }

    /**
     * Get inactive time of a certain activity and day
     *
     * @param activity Activity we want to retrieve the inactive time for
     * @param checkDay Day we want to retrieve the inactive time for
     */
    public long getInactiveTime(DetectedActivity activity, Date checkDay) {
        long time = 0;

        //go through all days in our list
        for (TimelineDay day : this.myHistory) {
            //check if is the same day
            if (day.isSameDay(checkDay)) {
                time += day.getInactiveTime(activity);
            }
        }

        return time;
    }

    /**
     * Get active distance of a certain activity and day
     *
     * @param activity Activity we want to retrieve the active distance for
     * @param checkDay Day we want to retrieve the active distance for
     * @return
     */
    public double getActiveDistance(DetectedActivity activity, Date checkDay) {
        double distance = 0;

        //go through all days in our list
        for (TimelineDay day : this.myHistory) {
            //check if is the same day
            if (day.isSameDay(checkDay)) {
                distance += day.getActiveDistance(activity);
            }
        }

        return distance;
    }

    /**
     * Get the active distance for the current month
     *
     * @return The active distance
     */
    public double getActiveDistanceForMonth() {
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 1);

        double distance = 0;
        DetectedActivity biking = new DetectedActivity(DetectedActivity.ON_BICYCLE, 100);
        DetectedActivity walking = new DetectedActivity(DetectedActivity.WALKING, 100);
        DetectedActivity foot = new DetectedActivity(DetectedActivity.ON_FOOT, 100);
        DetectedActivity running = new DetectedActivity(DetectedActivity.RUNNING, 100);

        //loop over all week days to count all steps
        for (TimelineDay day : week) {
            distance += day.getActiveDistance(biking);
            distance += day.getActiveDistance(walking);
            distance += day.getActiveDistance(foot);
            distance += day.getActiveDistance(running);
        }

        return distance;
    }

    /**
     * Get the active distance for the current week
     *
     * @return The active distance
     */
    public double getActiveDistanceForWeek() {
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 0);

        double distance = 0;
        DetectedActivity biking = new DetectedActivity(DetectedActivity.ON_BICYCLE, 100);
        DetectedActivity walking = new DetectedActivity(DetectedActivity.WALKING, 100);
        DetectedActivity foot = new DetectedActivity(DetectedActivity.ON_FOOT, 100);
        DetectedActivity running = new DetectedActivity(DetectedActivity.RUNNING, 100);

        //loop over all week days to count all steps
        for (TimelineDay day : week) {
            distance += day.getActiveDistance(biking);
            distance += day.getActiveDistance(walking);
            distance += day.getActiveDistance(foot);
            distance += day.getActiveDistance(running);
        }

        return distance;
    }

    /**
     * Get inactive distance of a certain activity and day
     *
     * @param activity Activity we want to retrieve the inactive distance for
     * @param checkDay Day we want to retrieve the inactive distance for
     */
    public double getInactiveDistance(DetectedActivity activity, Date checkDay) {
        double distance = 0;

        //go through all days in our list
        for (TimelineDay day : this.myHistory) {
            //check if is the same day
            if (day.isSameDay(checkDay)) {
                distance += day.getInactiveDistance(activity);
            }
        }

        return distance;
    }

    /**
     * Get the Steps of the timeline
     *
     * @param checkDay Day we want to retrieve the inactive distance for
     * @return The steps of the user
     */
    public int getSteps(Date checkDay) {
        int steps = 0;

        //loop over all days to count all steps
        for (TimelineDay day : this.myHistory) {
            if (day.isSameDay(checkDay)) {
                steps += day.getSteps();
            }
        }

        return steps;
    }

    /**
     * Get the steps in the last week
     *
     * @return The steps in the last week
     */
    public int getStepsForWeek() {
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 0);

        int steps = 0;

        //loop over all week days to count all steps
        for (TimelineDay day : week) {
            steps += day.getSteps();
        }

        return steps;
    }

    /**
     * Get the Steps for the last month
     *
     * @return The steps in the last month
     */
    public int getStepsForMonth() {
        Date current = new Date();

        //get days of week
        LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 1);

        int steps = 0;

        //loop over all week days to count all steps
        for (TimelineDay day : week) {
            steps += day.getSteps();
        }

        return steps;
    }

    /**
     * Get total distance of a certain activity and day
     *
     * @param activity Activity we want to retrieve the total distance for
     * @param checkDay Day we want to retrieve the total distance for
     * @return
     */
    public double getTotalDistance(DetectedActivity activity, Date checkDay) {
        return this.getActiveDistance(activity, checkDay) + getInactiveDistance(activity, checkDay);
    }

    /**
     * Manually starts a new segment if the user wants to
     *
     * @param location The location that should be added
     * @param date     The date the location/activity was detected
     * @param activity The activity that was detected
     */
    public void manualStartNewSegment(Location location, Date date, DetectedActivity activity) {
        Log.i(TAG, "ManualAddUserStatus:  create new Segment, location resolution.");

        //check if we have a day already
        if (this.myHistory.size() == 0) {
            this.myHistory.add(new TimelineDay(date));

            //REST Call to add the new Day to the DB
            DataUpdater.getInstance().addTimelineDay(this.myHistory.getLast());

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_INTENT_DAY_OWN_INSERT);
            intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }

        //check if we still have the same day
        if (!this.myHistory.getLast().isSameDay(date)) {
            this.myHistory.add(new TimelineDay(date));

            //REST Call to add the new Day to the DB
            DataUpdater.getInstance().addTimelineDay(this.myHistory.getLast());

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_INTENT_DAY_OWN_INSERT);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }

        this.calculateAchievements(date);

        this.myHistory.getLast().manualStartNewSegment(location, date, activity);
    }

    /**
     * End a manually created segment by user choice
     *
     * @param location The location that should be added
     * @param date     The date the location/activity was detected
     */
    public void manualEndSegment(Date date, Location location) {
        Log.i(TAG, "ManualEndSegment:  create new Segment, end last one.");
        this.myHistory.getLast().manualEndSegment(date, location);
    }

    /**
     * Get total time of a certain activity and day
     *
     * @param activity Activity we want to retrieve the total time for
     * @param checkDay Day we want to retrieve the total time for
     */
    public long getTotalTime(DetectedActivity activity, Date checkDay) {
        return this.getActiveTime(activity, checkDay) + this.getInactiveTime(activity, checkDay);
    }

    /**
     * Get all Timeline Days for the selected month or week
     *
     * @param referenceDate The date for the week we want the data for
     * @param mode          The mode of the method, 0 returns all days of the week, 1 for the month
     * @return A linked list containing all TimelineDays
     */
    private LinkedList<TimelineDay> getDaysOfWeekOrMonth(Date referenceDate, int mode) {

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int maximum = 0;

        //choose if we need all days of the week or month
        if (0 == mode) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            maximum = 7;
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            maximum = calendar.getActualMaximum(Calendar.DATE);
        }

        LinkedList<TimelineDay> daysOfWeek = new LinkedList<>();

        //get the TimelineDays
        for (int i = 0; i < maximum; i++) {
            for (TimelineDay day : this.myHistory) {
                if (day.isSameDay(calendar.getTime())) {
                    daysOfWeek.add(day);
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    /**
     * Get the date of the current TimelineDay
     *
     * @return The date
     */
    private Date getLastDate() {
        return this.myHistory.getLast().getMyDate();
    }

    /**
     * Get the Month or Week Achievements
     *
     * @return The achivements
     */
    public LinkedList<Achievement> getMyAchievements() {
        return myAchievements;
    }

    /**
     * Calculate the achievements to see if we have any new achievements
     *
     * @param day A day of the week we want to calculate achievements for
     */
    public void calculateAchievements(Date day) {

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

        //if new achievements -> send intent
        if(! achievements.isEmpty()){

            //REST Call to update Timeline
            //TODO: Update for achievements of a timeline

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_INTENT_OWN_ACHIEVEMENT_UPDATE);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }
    }

    /**
     * Method can be used to update the achievements in case there was a change from the Database
     *
     * @param achievement The new achievement we want to add
     */
    public void addAchievement(Achievement achievement) {
        this.myAchievements.add(achievement);

        //Send intent to inform about update, since this method should only be used for DB based updates
        //add the DB ID
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_INTENT_OTHER_ACHIEVEMENT_UPDATE);
        intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Used to add a new location/activity to our timeline
     *
     * @param location The location we want to add
     * @param date     The date the location was detected
     * @param activity The activity that was detected
     */
    public void addUserStatus(Location location, Date date, DetectedActivity activity) {
        //check if we have a day already
        if (this.myHistory.size() == 0) {
            this.myHistory.add(new TimelineDay(date));

            //REST Call to add the new Day to the DB
            DataUpdater.getInstance().addTimelineDay(this.myHistory.getLast());

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_INTENT_DAY_OWN_INSERT);
            intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }

        //check if we still have the same day
        if (!this.myHistory.getLast().isSameDay(date)) {
            this.myHistory.add(new TimelineDay(date));

            //REST Call to add the new Day to the DB
            DataUpdater.getInstance().addTimelineDay(this.myHistory.getLast());

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT.TIMELINE_INTENT_DAY_OWN_INSERT);
            LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
        }

        this.myHistory.getLast().addUserStatus(location, date, activity);
        this.calculateAchievements(date);
    }

    /**
     * Method can be used to add a timeline day in case there was a change from the Database
     *
     * @param day    The Timeline Day we want to add
     * @param userid The DB ID of the user the timeline is from
     */
    public void setMyHistory(TimelineDay day, String userid) {
        this.myHistory.add(day);

        //sort in case the order the segments were added is wrong
        Collections.sort(this.myHistory, new SortTimelineDay());

        //Send intent to inform about update, since this method should only be used for DB based updates
        //add the DB ID
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.TIMELINE_INTENT_DAY_OTHER_INSERT);
        intent.putExtra(Constants.INTENT_EXTRAS.ID, this.ID);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userid);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Add a new location point to a manual segment
     *
     * @param location The location that should be added
     * @param date     The date the location/activity was detected
     */
    public void manualAddLocation(Date date, Location location) {
        this.myHistory.getLast().manualAddLocation(date, location);
    }

    /**
     * Get the month and week achievements for the timeline
     *
     * @return A list containing all achievements
     */
    public LinkedList<Achievement> getAchievements() {
        return new LinkedList<>(this.myAchievements);
    }

    /**
     * Get the size of the history, so how many days are already in storage
     *
     * @return The size of the history
     */
    public int getHistorySize() {
        return this.myHistory.size();
    }

    /**
     * Get a specific day of the history
     *
     * @param index The index for which we want to retrieve the day for
     * @return The TimelineDay
     */
    public TimelineDay getTimelineDay(int index) {
        //check if index is out of bounds
        if (index < 0 || index >= this.myHistory.size()) {
            Log.i(TAG, "getTimelineDay: Index out of bounds.");
            return null;
        }

        return this.myHistory.get(index);
    }

    public LinkedList<TimelineDay> getTimelineDays() {
        return this.myHistory;
    }

    /**
     * Retrieve the database ID
     *
     * @return The database ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the Datatbase ID
     *
     * @param ID The new Database ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
