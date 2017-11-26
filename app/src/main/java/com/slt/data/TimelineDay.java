package com.slt.data;

import android.location.Location;
import android.text.format.DateUtils;

import com.google.android.gms.location.DetectedActivity;
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
    private TimelineSegment currentSegment;

    /**
     *
     */
    private Date myDate;

    /**
     *
     */
    private LinkedList<Achievement> myAchievements;

    //TODO add achievement calcuation

    //TODO statistics calculation

    public TimelineDay(Date myDate) {
        this.mySegments = new LinkedList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date truncatedDate = calendar.getTime();
        this.myDate = myDate;
        this.myAchievements = new LinkedList<>();
        this.currentSegment = null;
    }

    //TODO merge segment request


    public boolean isSameDay(Date date){
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

        if(this.currentSegment == null){
            currentSegment = new TimelineSegment(location, date, activity);
            return;
        }

        this.currentSegment.addLocationPoint(location, date);

        if(this.currentSegment.compareActivities(activity)){
            this.mySegments.add(currentSegment);

            this.currentSegment = new TimelineSegment(location, date, activity);

            Object[] ResolutionData = new Object[2];
            ResolutionData[0] = this.currentSegment;
            ResolutionData[1] = location;

            AddressResolver addressResolver = new AddressResolver();
            addressResolver.execute(ResolutionData);

            PlacesResolver placesResolver = new PlacesResolver();
            placesResolver.execute(ResolutionData);
        }
    }

    public int getSegmentSize(){
        return mySegments.size();
    }

    public TimelineSegment getSegments(int index) {
        return mySegments.get(index);
    }

    public Date getMyDate() {
        return myDate;
    }

    public LinkedList<Achievement> getMyAchievements() {
        return new LinkedList<>(myAchievements);
    }
}
