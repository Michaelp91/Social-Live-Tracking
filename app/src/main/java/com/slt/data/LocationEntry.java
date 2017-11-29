package com.slt.data;

import android.location.Location;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Thorsten on 06.11.2017.
 */

public class LocationEntry {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "LocationEntry";

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

    public LocationEntry(Location newLocation, Date newDate, Location lastLocation, Date lastDate){
        myLocation = newLocation;
        myEntryDate = newDate;
        myTrackDistance = calculateDistance(lastLocation, newLocation);
        myDuration = calculateTimeDifference(lastDate,newDate);

        Log.i(TAG, "Creating new LocationEntry, Duration: "+ getDuration() + " Distance: " + Double.toString(myTrackDistance));
    }


    private double calculateDistance(Location last, Location next){
        if (last == null) {
            return 0.0;
        }

        return next.distanceTo(last);
    }

    private long calculateTimeDifference(Date last, Date current){
        if (last == null) {
            return 0;
        }

        if(last.compareTo(current) == 0)
            return 0;

        if(last.before(current))
            return current.getTime() - last.getTime();
        return last.getTime() - current.getTime();
    }

    public double getLatitude(){
        return myLocation.getLatitude();
    }

    public double getLongitude(){
        return myLocation.getLongitude();
    }

    private String getDuration(){
        DateFormat dateFormat = new SimpleDateFormat("HH/mm/ss", Locale.GERMAN);
        Date date = new Date(myDuration);
        return dateFormat.format(date);
    }
}
