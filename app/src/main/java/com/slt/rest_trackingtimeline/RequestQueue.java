package com.slt.rest_trackingtimeline;

import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class RequestQueue {
    private static final RequestQueue ourInstance = new RequestQueue();
    private ArrayList<LocationEntry> locationEntries;
    private Timeline timeline;
    private ArrayList<TimeLineDay> timelineDays;
    private ArrayList<TimeLineSegment> timeLineSegments;

    public static RequestQueue getInstance() {
        return ourInstance;
    }

    private RequestQueue() {

    }



}
