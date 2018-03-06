package com.slt.restapi.data;

import com.slt.restapi.Singleton_General;

import java.util.Date;


/**
 * REST_LocationEntry
 */
public class REST_LocationEntry implements Model{
    /**
     * TAG
     */
    public String TAG;

    /**
     * int_TAG
     */
    public int int_TAG;

    /**
     * _id
     */
    public String _id;

    /**
     * myEntryDate
     */
    public Date myEntryDate;

    /**
     * myTrackDistance
     */
    public double myTrackDistance;

    /**
     * myDuration
     */
    public double myDuration;

    /**
     * myLocation
     */
    public REST_Location myLocation;

    /**
     * timelinesegment ID
     */
    public String timelinesegment;

    /**
     * parent object
     */
    public REST_TimelineSegment timelinesegmentObject;


    /**
     * Constructor to store all the data for storing or retrieving the location entry
     * @param myEntryDate
     * @param myTrackDistance
     * @param myDuration
     * @param myLocation
     * @param timelinesegmentObject
     */
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
