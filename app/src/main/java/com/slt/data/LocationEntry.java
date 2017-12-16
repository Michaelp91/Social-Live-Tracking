package com.slt.data;

import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class stores a single gps location point and computes the basic statistic data
 */

public class LocationEntry {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "LocationEntry";

    /**
     *  The location that was detected by GPS
     */
    private Location myLocation;

    /**
     * The date the location was detected
     */
    private Date myEntryDate;

    /**
     *  The distance that was covered between the two GPS points
     */
    private double myTrackDistance;

    /**
     *  The duration that elapsed between the last and the current location
     */
    private long myDuration;

    /**
     * The constructor to initialize the data, add a last location and date to calculate statistics
     * @param newLocation The location to store.
     * @param newDate The date the new location was detected
     * @param lastLocation The last location that was detected for statistics, might be null
     * @param lastDate The date of the last location used for statistics, might be null
     */
    public LocationEntry(Location newLocation, Date newDate, @Nullable Location lastLocation, @Nullable Date lastDate){
        myLocation = newLocation;
        myEntryDate = newDate;

        //calculate the statistics
        myTrackDistance = calculateDistance(lastLocation, newLocation);
        myDuration = calculateTimeDifference(lastDate,newDate);

        Log.i(TAG, "Creating new LocationEntry, Duration: "+ getDuration() + " Distance: " + Double.toString(myTrackDistance));
    }

    /**
     * Calculates the distance to the last location
     * @param last The last location that was detected, might be null
     * @param next The current location
     * @return
     */
    private double calculateDistance(@Nullable Location last, Location next){
        //if no last location
        if (last == null) {
            return 0.0;
        }

        return next.distanceTo(last);
    }

    /**
     * Calculate the time difference to the last time a location was detected
     * @param last The date of the last location entry, might be null
     * @param current The current date for the last location
     * @return
     */
    private long calculateTimeDifference(@Nullable Date last, Date current){
        // check if the last date is set
        if (last == null) {
            return 0;
        }

        //check if time has passed at all
        if(last.compareTo(current) == 0)
            return 0;

        //check if timestamps are in the correct order
        if(last.before(current))
            return current.getTime() - last.getTime();

        //calculate the passed time
        return last.getTime() - current.getTime();
    }

    /**
     * Get the latitude of the location
     * @return The latitude of the location
     */
    public double getLatitude(){
        return myLocation.getLatitude();
    }

    /**
     * Get the longitude of the location
     * @return The longitude of the location
     */
    public double getLongitude(){
        return myLocation.getLongitude();
    }

    /**
     * Get the Duration as a String
     * @return The duration as a String
     */
    private String getDuration(){
        DateFormat dateFormat = new SimpleDateFormat("HH/mm/ss", Locale.GERMAN);
        Date date = new Date(myDuration);
        return dateFormat.format(date);
    }

    /**
     * Get the location that has been stored
     * @return The location
     */
    public Location getMyLocation() {
        return myLocation;
    }

    /**
     * Get the Date the location was detected
     * @return The Date the location was detected
     */
    public Date getMyEntryDate() {
        return myEntryDate;
    }

    /**
     * Get the distance that was covered between the last and the current location
     * @return The distance to the last location
     */
    public double getMyTrackDistance() {
        return myTrackDistance;
    }

    /**
     * The duration that passed since the last detected location
     * @return The duration
     */
    public long getMyDuration() {
        return myDuration;
    }
}
