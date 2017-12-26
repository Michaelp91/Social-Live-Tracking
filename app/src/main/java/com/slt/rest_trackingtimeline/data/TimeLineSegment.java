package com.slt.rest_trackingtimeline.data;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineSegment {
    public String _id;
    public String startAddress;
    public String POI;
    public double totalDistance;
    public double duration;
    //private String pictures;
    //public DetectedActivity myActivity;
    public String timeLineDay;
    public ArrayList<Achievement> myAchievements;

    public TimeLineSegment(String startAddress, String POI, double totalDistance, double duration, String timeLineDay, ArrayList<Achievement> myAchievements) {
        this.startAddress = startAddress;
        this.POI = POI;
        this.totalDistance = totalDistance;
        this.duration = duration;
        this.timeLineDay = timeLineDay;
        this.myAchievements = myAchievements;
    }
}
