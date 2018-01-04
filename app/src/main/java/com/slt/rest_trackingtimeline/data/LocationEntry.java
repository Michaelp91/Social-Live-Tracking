package com.slt.rest_trackingtimeline.data;


import com.slt.data.TimelineSegment;
import com.slt.rest_trackingtimeline.Singleton_General;

import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class LocationEntry implements Model{
    public String TAG;
    public int int_TAG;

    public String _id;
    public Date myEntryDate;
    public double myTrackDistance;
    public double myDuration;
    public Location myLocation;
    public String timelinesegment;
    public TimeLineSegment timelinesegmentObject;


    public LocationEntry(Date myEntryDate, double myTrackDistance, double myDuration, Location myLocation,
                            TimeLineSegment timelinesegmentObject) {
        this.myEntryDate = myEntryDate;
        this.myTrackDistance = myTrackDistance;
        this.myDuration = myDuration;
        this.myLocation = myLocation;
        this.timelinesegment = timelinesegment;
        this.timelinesegmentObject = timelinesegmentObject;
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "LocationEntry";
        Singleton_General.getInstance().counter++;
    }
}
