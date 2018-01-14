package com.slt.restapi.data;

import com.slt.restapi.Singleton_General;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 11.01.2018.
 */

public class REST_TimelineDay implements Model{
    public String _id;
    public String TAG;
    public int int_TAG;
    public Date myDate;
    public ArrayList<REST_Achievement> myAchievements;
    public String timeline; //timelineID
    public int MIN_SEGMENT_DURATION_IN_SECONDS;

    public REST_TimelineDay(Date myDate) {
        this.myDate = myDate;
        this.timeline = null;
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "TimelineDay";
        this.MIN_SEGMENT_DURATION_IN_SECONDS = 120;
        Singleton_General.getInstance().counter++;
    }

}
