package com.slt.rest_trackingtimeline.data;

import android.location.Location;

import com.slt.data.TimelineSegment;

import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class LocationEntry {
    private Location location;
    private Date myEntryDate;
    private double myTrackDistance;
    private double myDuration;
    private TimelineSegment timelineSegment;
}
