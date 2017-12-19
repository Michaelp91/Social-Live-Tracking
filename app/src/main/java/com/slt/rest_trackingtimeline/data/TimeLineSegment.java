package com.slt.rest_trackingtimeline.data;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineSegment {
    private DetectedActivity myActivity;
    private String startAddress;
    private String POI;
    private double totalDistance;
    private double duration;
    private String pictures;
    private TimeLineDay timeLineDay;
}
