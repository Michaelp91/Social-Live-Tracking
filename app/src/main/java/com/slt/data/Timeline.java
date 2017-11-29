package com.slt.data;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.AchievementCalculator;

import android.util.Log;
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
    private LinkedList<Achievement> myAchievements;


    public long getActiveTime(DetectedActivity activity, Date checkDay) {
        long time = 0;

        for(TimelineDay day : this.myHistory){
            if(day.isSameDay(checkDay)){
                time += day.getActiveTime(activity);
            }
        }

        return time;
    }

    public long getInactiveTime(DetectedActivity activity, Date checkDay) {
        long time = 0;

        for(TimelineDay day : this.myHistory){
            if(day.isSameDay(checkDay)){
                time += day.getInactiveTime(activity);
            }
        }

        return time;
    }

    public double getActiveDistance(DetectedActivity activity, Date checkDay) {
        double distance = 0;

        for(TimelineDay day : this.myHistory){
            if(day.isSameDay(checkDay)){
                distance += day.getActiveDistance(activity);
            }
        }

        return distance;
    }

    public double getInactiveDistance(DetectedActivity activity, Date checkDay) {
        double distance = 0;

        for(TimelineDay day : this.myHistory){
            if(day.isSameDay(checkDay)){
                distance += day.getInactiveDistance(activity);
            }
        }

        return distance;
    }

    public double getTotalDistance(DetectedActivity activity, Date checkDay){
        return this.getActiveDistance(activity, checkDay)+ getInactiveDistance(activity, checkDay);
    }

    public long getTotalTime(DetectedActivity activity, Date checkDay){
        return this.getActiveTime(activity, checkDay) + this.getInactiveTime(activity, checkDay);
    }




    public Timeline(){
        myHistory = new LinkedList<TimelineDay>();
        myAchievements = new LinkedList<>();
    }


    public void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator
                .calculateWeekAchievements(this.myHistory, this.myAchievements);

        this.myAchievements.addAll(achievements);

        achievements = AchievementCalculator.calculateMonthAchievements(this.myHistory,
                this.myAchievements);
        this.myAchievements.addAll(achievements);
    }

    public void addUserStatus(Location location, Date date, DetectedActivity activity) {
        if (this.myHistory.size() == 0){
            this.myHistory.add(new TimelineDay(date));
        }

        if(!this.myHistory.getLast().isSameDay(date)) {
            this.myHistory.add(new TimelineDay(date));
        }

        this.myHistory.getLast().addUserStatus(location, date, activity);
        this.calculateAchievements();
    }


    public LinkedList<Achievement> getAchievements(){
        return new LinkedList<>(this.myAchievements);
    }

    public int getHistorySize(){
        return this.myHistory.size();
    }

   public TimelineDay getTimelineDay(int index){
        if(index < 0 || index >= this.myHistory.size()){
            Log.i(TAG, "getTimelineDay: Index out of bounds.");
            return null;
        }

        return this.myHistory.get(index);
   }


}
