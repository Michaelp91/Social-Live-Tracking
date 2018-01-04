package com.slt.rest_trackingtimeline.data;

import com.slt.rest_trackingtimeline.Singleton;
import com.slt.rest_trackingtimeline.Singleton_General;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineDay implements Model{
    public String _id;
    public String TAG;
    public int int_TAG;
    public Date myDate;
    public ArrayList<Achievement> myAchievements;
    public String timeline; //timelineID
    public TimeLine timeLineObject;
    public int MIN_SEGMENT_DURATION_IN_SECONDS;

    public TimeLineDay(Date myDate, ArrayList<Achievement> myAchievements, TimeLine timeLineObject) {
        this.myDate = myDate;
        this.myAchievements = myAchievements;
        this.timeline = null;
        this.timeLineObject = timeLineObject;
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "TimelineDay";
        this.MIN_SEGMENT_DURATION_IN_SECONDS = 120;
        Singleton_General.getInstance().counter++;
    }
}
