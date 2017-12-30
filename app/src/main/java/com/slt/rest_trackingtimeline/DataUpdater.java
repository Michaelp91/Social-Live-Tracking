package com.slt.rest_trackingtimeline;

import android.util.Log;


import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.rest_trackingtimeline.data.Achievement;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class DataUpdater implements Runnable{
    private static final DataUpdater ourInstance = new DataUpdater();
    private Thread updater;

    private ArrayList<LocationEntry> queue_locationEntries;
    private TimeLine timeline;
    private ArrayList<TimeLineDay> queue_timelineDays;
    private ArrayList<TimeLineSegment> queue_timeLineSegments;
    private boolean isAlive;

    public static DataUpdater getInstance() {
        return ourInstance;
    }

    private DataUpdater() {
        isAlive = true;

        timeline = null;
        queue_locationEntries = new ArrayList<>();
        queue_timelineDays = new ArrayList<>();
        queue_timeLineSegments = new ArrayList<>();
    }

    public void Start() {
        updater = new Thread(new DataUpdater());
        updater.start();
    }

    public void Terminate() {
        isAlive = false;
        Locks.getInstance().lock.notifyAll();
    }

    public void update() throws InterruptedException {
        if (timeline != null) {
            UpdateOperations_Synchron.createTimeLine(timeline);
            Log.d("Create", "Timeline is created.");
            timeline = null;
        }

        TimeLine timeLine = Singleton.getInstance().getResponse_timeLine();

        for(TimeLineDay t_d: queue_timelineDays ) {
            t_d.timeline = timeLine._id;
            UpdateOperations_Synchron.createTimeLineDay(t_d);
            Log.d("Create", "Timeline Day is created.");
            queue_timelineDays.remove(t_d);
        }

        for(TimeLineSegment t_s: queue_timeLineSegments ) {
            TimeLineDay timeLineDayToFind = t_s.timeLineDayObject;
            TimeLineDay timeLineDay = TemporaryDB.getInstance().findTimeLineDayByObject(timeLineDayToFind);
            t_s.timeLineDay = timeLineDay.timeline;
            UpdateOperations_Synchron.createTimeLineSegment(t_s);
            Log.d("Create", "Timeline Segment is created.");
            queue_timeLineSegments.remove(t_s);
        }

        for(LocationEntry l_e: queue_locationEntries) {
            TimeLineSegment search = l_e.timelinesegmentObject;
            TimeLineSegment timeLineSegment = TemporaryDB.getInstance().findTimeLineSegmentByObject(search);
            l_e.timelinesegment = timeLineSegment._id;
            UpdateOperations_Synchron.createLocationEntry(l_e);

            Log.d("Create", "Location Entry is created.");
            queue_locationEntries.remove(l_e);
        }

        Log.d("Create", "End");

        if(queue_timelineDays.isEmpty() || queue_timeLineSegments.isEmpty() || queue_locationEntries.isEmpty()) {
            Locks.getInstance().lock.wait();
        }

    }

    public void run() {
        while(isAlive) {
            synchronized (Locks.getInstance().lock) {
                try {
                    ourInstance.update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public ArrayList<LocationEntry> getQueue_locationEntries() {
        return queue_locationEntries;
    }

    public void addLocationEntry(LocationEntry l) {
        queue_locationEntries.add(l);
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public TimeLine getTimeline() {
        return timeline;
    }

    public synchronized void setTimeline(TimeLine t) {

        timeline = t;
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public ArrayList<TimeLineDay> getQueue_timelineDays() {
        return queue_timelineDays;
    }

    public void addTimelineDay(TimeLineDay t) {

        queue_timelineDays.add(t);
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public ArrayList<TimeLineSegment> getQueue_timeLineSegments() {
        return queue_timeLineSegments;
    }

    public void addTimeLineSegment(TimeLineSegment t) {
        queue_timeLineSegments.add(t);
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

}
