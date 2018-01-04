package com.slt.rest_trackingtimeline.data;

import com.google.android.gms.location.DetectedActivity;
import com.slt.rest_trackingtimeline.Singleton_General;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineSegment implements Model{
    public String TAG;
    public int myActivity;
    public int int_TAG;
    public String _id;
    public String startAddress;
    public String POI;
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
    public TimeLineDay timeLineDayObject;
    public ArrayList<Achievement> myAchievements;

    public TimeLineSegment(String startAddress, String POI, double activeDistance, double inactiveDistance, double duration, ArrayList<Achievement> myAchievements,
                            TimeLineDay timeLineDayObject, int myActivity, double activeTime, double inactiveTime,
                           double userSteps, double startPlace, Date startTime) {
        this.startAddress = startAddress;
        this.POI = POI;
        this.activeDistance = activeDistance;
        this.inactiveDistance = inactiveDistance;
        this.duration = duration;
        this.myAchievements = myAchievements;
        this.timeLineDayObject = timeLineDayObject;
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "TimelineSegment";
        this.myActivity = myActivity;
        this.activeTime = activeTime;
        this.inactiveTime = inactiveTime;
        this.userSteps = userSteps;
        this.startPlace = startPlace;
        this.startTime = startTime;
        Singleton_General.getInstance().counter++;
    }

    public void setTimeLineDay(String timeLineDay) {
        this.timeLineDay = timeLineDay;
    }
}
