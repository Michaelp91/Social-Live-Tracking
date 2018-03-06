package com.slt.restapi.data;

import com.slt.restapi.Singleton_General;

import java.util.ArrayList;
import java.util.Date;

/**
 * REST_TimelineDay
 */

public class REST_TimelineDay implements Model{

    /**
     * _id
     */
    public String _id;

    /**
     * TAG
     */
    public String TAG;

    /**
     * int_TAG
     */
    public int int_TAG;

    /**
     * myDate
     */
    public Date myDate;

    /**
     * list of achievements
     */
    public ArrayList<REST_Achievement> myAchievements;

    /**
     * timeline id
     */
    public String timeline; //timelineID

    /**
     * MIN_SEGMENT_DURATION_IN_SECONDS
     */
    public int MIN_SEGMENT_DURATION_IN_SECONDS;


    /**
     * constructor
     * @param myDate
     */
    public REST_TimelineDay(Date myDate) {
        this.myDate = myDate;
        this.timeline = null;
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "TimelineDay";
        this.MIN_SEGMENT_DURATION_IN_SECONDS = 120;
        Singleton_General.getInstance().counter++;
    }

}
