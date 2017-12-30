package com.slt.rest_trackingtimeline.data;

import com.google.android.gms.location.DetectedActivity;
import com.slt.rest_trackingtimeline.Singleton_General;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineSegment implements Model{
    public int TAG;
    public String _id;
    public String startAddress;
    public String POI;
    public double totalDistance;
    public double duration;
    //private String pictures;
    //public DetectedActivity myActivity;
    public String timeLineDay;
    public TimeLineDay timeLineDayObject;
    public ArrayList<Achievement> myAchievements;

    public TimeLineSegment(String startAddress, String POI, double totalDistance, double duration, ArrayList<Achievement> myAchievements,
                            TimeLineDay timeLineDayObject) {
        this.startAddress = startAddress;
        this.POI = POI;
        this.totalDistance = totalDistance;
        this.duration = duration;
        this.myAchievements = myAchievements;
        this.timeLineDayObject = timeLineDayObject;
        this.TAG = Singleton_General.getInstance().counter;
        Singleton_General.getInstance().counter++;
    }

    public void setTimeLineDay(String timeLineDay) {
        this.timeLineDay = timeLineDay;
    }
}
