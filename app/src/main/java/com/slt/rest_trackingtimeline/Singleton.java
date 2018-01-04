package com.slt.rest_trackingtimeline;

import com.slt.rest_trackingtimeline.data.FullTimeLine;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.Test;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 25.12.2017.
 */

public class Singleton {
    private static Singleton singleton;
    private ArrayList<Test> response;
    private FullTimeLine responses;
    private TimeLine response_timeLine;
    private TimeLineDay response_timelineDay;
    private TimeLineSegment response_timelineSegment;
    private LocationEntry response_locationEntry;

    public static Singleton getInstance() {
        singleton = (singleton == null)?new Singleton():singleton;
        return singleton;
    }

    public ArrayList<Test> getTestResponse() {
        return response;
    }

    public TimeLine getResponse_timeLine() {
        return response_timeLine;
    }

    public void setResponse_timeLine(TimeLine response_timeLine) {
        this.response_timeLine = response_timeLine;
    }

    public TimeLineDay getResponse_timelineDay() {
        return response_timelineDay;
    }

    public void setResponse_timelineDay(TimeLineDay response_timelineDay) {
        this.response_timelineDay = response_timelineDay;
    }

    public TimeLineSegment getResponse_timelineSegment() {
        return response_timelineSegment;
    }

    public void setResponse_timelineSegment(TimeLineSegment response_timelineSegment) {
        this.response_timelineSegment = response_timelineSegment;
    }

    public LocationEntry getResponse_locationEntry() {
        return response_locationEntry;
    }

    public void setResponse_locationEntry(LocationEntry response_locationEntries) {
        this.response_locationEntry = response_locationEntries;
    }

    public FullTimeLine getResponses() {
        return responses;
    }
}
