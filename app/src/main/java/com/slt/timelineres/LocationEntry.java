package com.slt.timelineres;

import android.location.Location;

import java.util.Date;

/**
 * Created by Usman Ahmad on 03.12.2017.
 */

public class LocationEntry {
    private Location myLocation;
    private Date myEntryDate;
    private double myTrackDistance;
    private long myDuration;

    public Location getMyLocation() {
        return myLocation;
    }

    public Date getMyEntryDate() {
        return myEntryDate;
    }

    public double getMyTrackDistance() {
        return myTrackDistance;
    }

    public long getMyDuration() {
        return myDuration;
    }
}
