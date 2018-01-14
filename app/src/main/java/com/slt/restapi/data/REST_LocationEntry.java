package com.slt.restapi.data;

import com.slt.restapi.Singleton_General;

import java.util.Date;

/**
 * Created by Usman Ahmad on 11.01.2018.
 */

public class REST_LocationEntry implements Model{
    public String TAG;
    public int int_TAG;

    public String _id;
    public Date myEntryDate;
    public double myTrackDistance;
    public double myDuration;
    public REST_Location myLocation;
    public String timelinesegment;
    public REST_TimelineSegment timelinesegmentObject;


    public REST_LocationEntry(Date myEntryDate, double myTrackDistance, double myDuration, REST_Location myLocation,
                              REST_TimelineSegment timelinesegmentObject) {
        this.myEntryDate = myEntryDate;
        this.myTrackDistance = myTrackDistance;
        this.myDuration = myDuration;
        this.myLocation = myLocation;
        this.timelinesegmentObject = timelinesegmentObject;
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "LocationEntry";
        Singleton_General.getInstance().counter++;
    }



}
