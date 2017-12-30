package com.slt.rest_trackingtimeline.data;

import com.slt.rest_trackingtimeline.Singleton;
import com.slt.rest_trackingtimeline.Singleton_General;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineDay implements Model{
    public int TAG;
    public Date myDate;
    public ArrayList<Achievement> myAchievements;
    public String timeline; //timelineID
    public TimeLine timeLineObject;

    public TimeLineDay(Date myDate, ArrayList<Achievement> myAchievements, TimeLine timeLineObject) {
        this.myDate = myDate;
        this.myAchievements = myAchievements;
        this.timeline = null;
        this.timeLineObject = timeLineObject;
        this.TAG = Singleton_General.getInstance().counter;
        Singleton_General.getInstance().counter++;
    }
}
