package com.slt.restapi.data;

import com.slt.restapi.Singleton_General;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 11.01.2018.
 */

public class REST_TimelineSegment implements Model{
    public String TAG;
    public int myActivity;
    public int int_TAG;
    public String _id;
    public String startAddress;
    public double POI;
    public double activeDistance;
    public double inactiveDistance;
    public double activeTime;
    public double inactiveTime;
    public double userSteps;
    public double startPlace;
    public Date startTime;
    public double duration;
    //private String pictures;
    public String timeLineDay;
    public REST_TimelineDay timeLineDayObject;
    public ArrayList<REST_Achievement> myAchievements;

    public REST_TimelineSegment(Date startTime, int myActivity, REST_TimelineDay timeLineDayObject) {
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "TimelineSegment";
        this.timeLineDayObject = timeLineDayObject;
        this.myActivity = myActivity;
        this.startTime = startTime;
        Singleton_General.getInstance().counter++;
    }



    public void setTimeLineDay(String timeLineDay) {
        this.timeLineDay = timeLineDay;
    }
}
