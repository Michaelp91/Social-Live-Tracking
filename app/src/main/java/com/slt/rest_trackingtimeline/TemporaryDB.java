package com.slt.rest_trackingtimeline;

import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class TemporaryDB {
    private static final TemporaryDB ourInstance = new TemporaryDB();

    private ArrayList<LocationEntry> locationEntries;
    private TimeLine timeline;
    private ArrayList<TimeLineDay> timelineDays;
    private ArrayList<TimeLineSegment> timeLineSegments;

    public static TemporaryDB getInstance() {
        return ourInstance;
    }

    private TemporaryDB() {
        locationEntries = new ArrayList<>();
        timelineDays = new ArrayList<>();
        timeLineSegments = new ArrayList<>();
    }

    public void setLocationEntries(ArrayList<LocationEntry> locationEntries) {
        this.locationEntries = locationEntries;
    }

    public void setTimelineDays(ArrayList<TimeLineDay> timelineDays) {
        this.timelineDays = timelineDays;
    }

    public void setTimeLineSegments(ArrayList<TimeLineSegment> timeLineSegments) {
        this.timeLineSegments = timeLineSegments;
    }

    public void addLocationEntry(LocationEntry l) {
        this.locationEntries.add(l);
    }

    public ArrayList<LocationEntry> getLocationEntries() {
        return locationEntries;
    }

    public void setTimeline(TimeLine timeline) {
        this.timeline = timeline;
    }

    public TimeLine getTimeline() {
        return timeline;
    }

    public void addTimelineDay(TimeLineDay t) {
        this.timelineDays.add(t);
    }


    public ArrayList<TimeLineSegment> findTimelineSegmentsByTDayId(String tId) {
        ArrayList<TimeLineSegment> choosedSegments = new ArrayList<>();
        for(TimeLineSegment t: timeLineSegments) {
            if(t.timeLineDay == tId) {
                choosedSegments.add(t);
            }
        }

        return choosedSegments;
    }


    public ArrayList<LocationEntry> findLocationEntriesByTSegmentId(String segmentId) {
        ArrayList<LocationEntry> choosedEntries = new ArrayList<>();
        for(LocationEntry l: locationEntries) {
            if(l.timelinesegment == segmentId) {
                choosedEntries.add(l);
            }
        }

        return choosedEntries;
    }

    public TimeLineDay findTimeLineDayByObject(TimeLineDay search) {

        for(TimeLineDay result: timelineDays) {
            if(result.int_TAG == search.int_TAG) {
                return result;
            }
        }


        return null;
    }

    public ArrayList<TimeLineDay> getTimelineDays() {
        return timelineDays;
    }

    public void addTimeLineSegment(TimeLineSegment t) {
        this.timeLineSegments.add(t);
    }

    public TimeLineSegment findTimeLineSegmentByObject(TimeLineSegment search) {
        for(TimeLineSegment result: timeLineSegments) {
            if(result.int_TAG == search.int_TAG) {
                return result;
            }
        }

        return null;
    }

    public ArrayList<TimeLineSegment> getTimeLineSegments() {
        return timeLineSegments;
    }



}
