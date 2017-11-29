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
     *
     */
    private LinkedList<TimelineSegment> mySegments;

    /**
     *
     */
    private Date myDate;

    /**
     *
     */
    private LinkedList<Achievement> myAchievements;




    public void calculateAchievements(){
        LinkedList<Achievement> achievements = AchievementCalculator
                .calculateDayAchievements(this.mySegments, this.myAchievements);

        this.myAchievements.addAll(achievements);
    }

    public long getActiveTime(DetectedActivity activity){
        long time = 0;

        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                time += segment.getActiveTime();
            }
        }

        return time;
    }

    public long getInactiveTime(DetectedActivity activity){
        long time = 0;

        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                time += segment.getInactiveTime();
            }
        }

        return time;
    }

    public double getActiveDistance(DetectedActivity activity) {
        double distance = 0;

        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null) {
                distance += segment.getActiveDistance();
            }
        }

        return distance;
    }

    public double getInactiveDistance(DetectedActivity activity) {
        double distance = 0;

        for(TimelineSegment segment : this.mySegments){
            if(segment.compareActivities(activity) || activity == null){
                distance += segment.getInactiveDistance();
            }
        }

        return distance;
    }

    public double getTotalDistance(DetectedActivity activity){
        return this.getActiveDistance(activity)+ getInactiveDistance(activity);
    }

    public long getTotalTime(DetectedActivity activity) {
        return this.getActiveTime(activity) + this.getInactiveTime(activity);
    }


    public TimelineDay(Date myDate) {
        this.mySegments = new LinkedList<>();

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


    public void mergeLastSegment(){
        if(this.mySegments.size() < 2){
            Log.i(TAG, "mergeLastSegment: Only one element, nothing to merge.");
            return;
        }
        int index = this.mySegments.size() -1;
        this.mergeSegments(index);
    }

    public void mergeSegments(int index){
        if(index >= this.mySegments.size() || index < 1){
            Log.i(TAG, "mergeSegments: Out of Bounds.");
            return;
        }

        if(this.mySegments.size() < 2 ){
            Log.i(TAG, "mergeSegments: Only one element, nothing to merge.");
            return;
        }

        TimelineSegment current = mySegments.get(index);
        mySegments.get(index-1).mergeTimelineSegments(current);

        mySegments.remove(index);
        this.calculateAchievements();
    }


    public boolean isSameDay(Date date){
        if(date == null){
            Log.i(TAG, "isSameDay: date is null");
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date truncatedDate = calendar.getTime();

        return myDate.compareTo(truncatedDate) == 0;
    }

    public void addUserStatus(Location location, Date date, DetectedActivity activity){

        if(this.mySegments.size() == 0){
            this.mySegments.add(new TimelineSegment(location, date, activity));
            return;
        }

        this.mySegments.getLast().addLocationPoint(location, date);

        if(!this.mySegments.getLast().compareActivities(activity)){

            this.mySegments.add(new TimelineSegment(location, date, activity));

            Object[] ResolutionData = new Object[2];
            ResolutionData[0] = this.mySegments.getLast();
            ResolutionData[1] = location;

            AddressResolver addressResolver = new AddressResolver();
            addressResolver.execute(ResolutionData);

            PlacesResolver placesResolver = new PlacesResolver();
            placesResolver.execute(ResolutionData);
        }

        this.calculateAchievements();
    }

    public int getSegmentSize(){
        return mySegments.size();
    }

    public TimelineSegment getSegment(int index) {
        if(index < 0  || index >= this.mySegments.size()){
            Log.i(TAG, "getSegment: Index out of bounds.");
            return null;
        }
        return mySegments.get(index);
    }

    public Date getMyDate() {
        return myDate;
    }

    public LinkedList<Achievement> getMyAchievements() {
        return new LinkedList<>(myAchievements);
    }
}
