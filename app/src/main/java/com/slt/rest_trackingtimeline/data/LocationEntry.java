package com.slt.rest_trackingtimeline.data;


import com.slt.data.TimelineSegment;

import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class LocationEntry {
    public String _id;
    public Date myEntryDate;
    public double myTrackDistance;
    public double myDuration;
    public Location myLocation;
    public String timelinesegment;

    public LocationEntry(Date myEntryDate, double myTrackDistance, double myDuration, Location myLocation, String timelinesegment) {
        this.myEntryDate = myEntryDate;
        this.myTrackDistance = myTrackDistance;
        this.myDuration = myDuration;
        this.myLocation = myLocation;
        this.timelinesegment = timelinesegment;
    }
}
