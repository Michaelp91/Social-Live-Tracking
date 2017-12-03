package com.slt.timelineres;

import com.slt.data.Achievement;
import com.slt.data.LocationEntry;

import java.util.LinkedList;

/**
 * Created by Usman Ahmad on 03.12.2017.
 */

public class TimelineSegment {
    private LinkedList<LocationEntry> myLocationPoints;
    private double totalDistance;
    private long duration;
    private LinkedList<Achievement> myAchievements;


    public LinkedList<LocationEntry> getMyLocationPoints() {
        return myLocationPoints;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public long getDuration() {
        return duration;
    }
}
