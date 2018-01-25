package com.slt.restapi;

import android.util.Log;


import com.google.android.gms.location.DetectedActivity;
import com.slt.data.*;
import com.slt.restapi.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class DataUpdater implements Runnable{
    private static final DataUpdater ourInstance = new DataUpdater();
    private Thread updater;

    private ArrayList<REST_LocationEntry> queue_locationEntries;
    private Timeline timeline;
    private REST_Timeline rest_timeline;
    private ArrayList<REST_TimelineDay> queue_timelineDays;
    private ArrayList<REST_TimelineSegment> queue_timeLineSegments;
    private boolean isAlive;

    //TODO:Hashmap <TrackingObject, RestObject>

    //For Creates
    private HashMap<LocationEntry, REST_LocationEntry> h_queue_locationEntries = new HashMap<>();
    private HashMap<TimelineSegment, REST_TimelineSegment> h_queue_timelineSegments = new HashMap<>();
    private HashMap<TimelineDay, REST_TimelineDay> h_queue_timelineDays = new HashMap<>();
    private HashMap<TimelineSegment, REST_TimelineSegment> h_queue_timelinesegments = new HashMap<>();
    private HashMap<User, REST_User_Functionalities> h_queue_user_functionalities = new HashMap<>();

    //For Updates
    private HashMap<LocationEntry, REST_LocationEntry> h_queue_locationEntries_update = new HashMap<>();
    private HashMap<TimelineSegment, REST_TimelineSegment> h_queue_timelineSegments_update = new HashMap<>();
    private HashMap<TimelineDay, REST_TimelineDay> h_queue_timelineDays_update = new HashMap<>();
    private HashMap<TimelineSegment, REST_TimelineSegment> h_queue_timelinesegments_update = new HashMap<>();
    private HashMap<User, REST_User_Functionalities> h_queue_user_functionalities_update = new HashMap<>();

    //For Deletes
    private HashMap<LocationEntry, REST_LocationEntry> h_queue_locationEntries_delete = new HashMap<>();
    private HashMap<TimelineSegment, REST_TimelineSegment> h_queue_timelineSegments_delete = new HashMap<>();
    private HashMap<TimelineDay, REST_TimelineDay> h_queue_timelineDays_delete = new HashMap<>();
    private HashMap<TimelineSegment, REST_TimelineSegment> h_queue_timelinesegments_delete = new HashMap<>();
    private HashMap<User, REST_User_Functionalities> h_queue_user_functionalities_delete = new HashMap<>();



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
        isAlive = true;
        timeline = null;
        updater = new Thread(new DataUpdater());
        updater.start();
    }

    public void Terminate() {
        isAlive = false;
        Locks.getInstance().lock.notifyAll();
    }

    public void create() throws InterruptedException {
        boolean requestSuccessful = true;

        if (timeline != null) {
            requestSuccessful = UpdateOperations_Synchron.createTimeLine(rest_timeline);

            if(requestSuccessful) {
                Log.d("Create", "Timeline is created.");
                timeline = null;
            } else {
          //      Log.d("Create", "Timeline is not created.");
            }
        }

        REST_Timeline timeLine = TemporaryDB.getInstance().getTimeline();

        if(timeLine != null) {
            Iterator iterator = h_queue_timelineDays.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry =(Map.Entry) iterator.next();
                REST_TimelineDay r_t_d = (REST_TimelineDay) entry.getValue();

                r_t_d.timeline = timeLine._id;
                TimelineDay t_d = (TimelineDay) entry.getKey();
                requestSuccessful = UpdateOperations_Synchron.createTimeLineDay(t_d, r_t_d);

                if (requestSuccessful) {
                    Log.d("Create", "Timeline Day is created.");
                    iterator.remove();
                } else {
          //          Log.d("Create", "Timeline Day is not created.");
                }
            }

            iterator = h_queue_timelinesegments.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                REST_TimelineSegment r_t_s = (REST_TimelineSegment) entry.getValue();
                REST_TimelineDay timeLineDayToFind = r_t_s.timeLineDayObject;
                REST_TimelineDay timeLineDay = TemporaryDB.getInstance().findTimeLineDayByObject(timeLineDayToFind);

                if(timeLineDay != null) {
                    r_t_s.timeLineDay = timeLineDay._id;
                    TimelineSegment t_s = (TimelineSegment) entry.getKey();
                    requestSuccessful = UpdateOperations_Synchron.createTimeLineSegment(t_s, r_t_s);

                    if (requestSuccessful) {
                        Log.d("Create", "Timeline Segment is created.");
                        iterator.remove();
                    } else {
               //         Log.d("Create", "Timeline Segment is not created.");
                    }

                } else {
            //        Log.d("Create", "Timeline Segment is not Created.");
                }
            }

            iterator = h_queue_locationEntries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                REST_LocationEntry r_l_e = (REST_LocationEntry) entry.getValue();
                REST_TimelineSegment search = r_l_e.timelinesegmentObject;
                REST_TimelineSegment timeLineSegment = TemporaryDB.getInstance().findTimeLineSegmentByObject(search);

                if(timeLineSegment != null) {
                    r_l_e.timelinesegment = timeLineSegment._id;
                    LocationEntry l_e = (LocationEntry) entry.getKey();
                    requestSuccessful = UpdateOperations_Synchron.createLocationEntry(l_e, r_l_e);

                    if (requestSuccessful) {

                        Log.d("Create", "Location Entry is created.");
                        iterator.remove();
                    } else {
              //          Log.d("Create", "Location Entry is not created.");
                    }
                } else {
            //        Log.d("Create", "Location Entry is not created.");
                }
            }

       //     Log.d("Create", "End");

        }

    }

    public void update() {
        REST_Timeline timeLine = TemporaryDB.getInstance().getTimeline();

        if(timeLine != null) {
            Iterator iterator = h_queue_timelinesegments_update.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry entry =(Map.Entry) iterator.next();
                REST_TimelineSegment r_t_s = (REST_TimelineSegment) entry.getValue();
                TimelineSegment t_s = (TimelineSegment) entry.getKey();
                boolean requestSuccessful = UpdateOperations_Synchron.updateTimelineSegment(t_s, r_t_s);

                if (requestSuccessful) {
                    Log.d("Create", "Timeline Segment is updated.");
                    iterator.remove();
                } else {
            //        Log.d("Create", "Timeline Segment is not updated.");
                }
            }

            iterator = h_queue_timelineDays_update.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry entry =(Map.Entry) iterator.next();
                REST_TimelineDay r_t_d = (REST_TimelineDay) entry.getValue();
                TimelineDay t_d = (TimelineDay) entry.getKey();
                boolean requestSuccessful = UpdateOperations_Synchron.updateTimelineDay(t_d, r_t_d);

                if (requestSuccessful) {
                    Log.d("Create", "Timeline Day is updated.");
                    iterator.remove();
                } else {
            //        Log.d("Create", "Timeline Day is not updated.");
                }
            }
        }
    }

    public void delete() {
        REST_Timeline timeLine = TemporaryDB.getInstance().getTimeline();

        if(timeLine != null) {
            Iterator iterator = h_queue_timelinesegments_delete.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry entry =(Map.Entry) iterator.next();
                REST_TimelineSegment r_t_s = (REST_TimelineSegment) entry.getValue();
                TimelineSegment t_s = (TimelineSegment) entry.getKey();
                boolean requestSuccessful = UpdateOperations_Synchron.deleteTimelineSegment(t_s, r_t_s);

                if (requestSuccessful) {
                    Log.d("Create", "Timeline Segment is deleted.");
                    iterator.remove();
                } else {
           //         Log.d("Create", "Timeline Segment is not deleted.");
                }
            }

            iterator = h_queue_locationEntries_delete.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry entry =(Map.Entry) iterator.next();
                REST_LocationEntry r_l_e = (REST_LocationEntry) entry.getValue();
                LocationEntry l_e = (LocationEntry) entry.getKey();
                boolean requestSuccessful = UpdateOperations_Synchron.deleteLocationEntry(l_e, r_l_e);

                if (requestSuccessful) {
                    Log.d("Create", "Location Entry is deleted.");
                    iterator.remove();
                } else {
           //         Log.d("Create", "Location Entry is not deleted.");
                }
            }
        }
    }

    public void run() {
        if(!isAlive) {

        }

        while(isAlive) {
            synchronized (Locks.getInstance().lock) {
                try {
                    ourInstance.create();
                    ourInstance.update();
                    ourInstance.delete();

                    if(ourInstance.h_queue_timelineDays.isEmpty()
                            && ourInstance.h_queue_timelineSegments.isEmpty()
                            && ourInstance.h_queue_locationEntries.isEmpty()
                            && ourInstance.h_queue_timelinesegments_update.isEmpty()
                            && ourInstance.h_queue_timelineDays_update.isEmpty()
                            && ourInstance.h_queue_timelinesegments_delete.isEmpty()
                            && ourInstance.h_queue_locationEntries_delete.isEmpty()) {

                        Locks.getInstance().lock.wait();
                    }
                    //TODO: Might REMOVE , could LEAD TO CONTENTIONS
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public ArrayList<REST_LocationEntry> getQueue_locationEntries() {
        return queue_locationEntries;
    }


    public void addLocationEntry(LocationEntry l, TimelineSegment t_s) { //TODO: Tracking Object as a parameter
        //TODO: create REST Object and insert it into hashmap
        REST_TimelineSegment r_t_s =  TemporaryDB.getInstance().h_timelineSegments.get(t_s);


            REST_Location r_l = new REST_Location(l.getLatitude(), l.getLongitude());
            REST_LocationEntry obj = new REST_LocationEntry(
                    l.getMyEntryDate(), l.getMyTrackDistance(), l.getMyDuration(), r_l, r_t_s);

            h_queue_locationEntries.put(l, obj);
            TemporaryDB.getInstance().h_locationEntries.put(l, obj);
            synchronized (Locks.getInstance().lock) {
                Locks.getInstance().lock.notify();
            }

    }

    public void deleteLocationEntry(LocationEntry l_e) {
        REST_LocationEntry r_l_e = TemporaryDB.getInstance().h_locationEntries.get(l_e);
        h_queue_locationEntries_delete.put(l_e, r_l_e);
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public REST_Timeline getTimeline() {
        return rest_timeline;
    }

    /**
     * Create a Timeline for logged in User
     */
    public synchronized void setTimeline(Timeline t) {
        REST_User_Functionalities rest_user = TemporaryDB.getInstance().getAppUser();
        ArrayList<REST_Achievement> rest_achievements = new ArrayList<>();

        for(Achievement a: t.getAchievements()) {
            REST_Achievement r_a = new REST_Achievement(a.getAchievement());
            rest_achievements.add(r_a);
        }

        timeline = t;
        rest_timeline = new REST_Timeline(rest_user._id, rest_achievements);
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public ArrayList<REST_TimelineDay> getQueue_timelineDays() {
        return queue_timelineDays;
    }

    public void addTimelineDay(TimelineDay t_d) {


            REST_TimelineDay r_t_d = new REST_TimelineDay(t_d.getMyDate());
            r_t_d.timeline = TemporaryDB.getInstance().getTimeline()._id;
            h_queue_timelineDays.put(t_d, r_t_d);
            TemporaryDB.getInstance().h_timelineDays.put(t_d, r_t_d);
            synchronized (Locks.getInstance().lock) {
                Locks.getInstance().lock.notify();
            }

    }

    public void updateTimelineDay(TimelineDay t_d) {
        REST_TimelineDay r_t_d = TemporaryDB.getInstance().h_timelineDays.get(t_d);

        ArrayList<Achievement> achievements = new ArrayList<>();
        ArrayList<REST_Achievement> rest_achievements = new ArrayList<>();
        for(Achievement a: t_d.getMyAchievements()) {
            REST_Achievement r_a = new REST_Achievement(a.getAchievement());
            rest_achievements.add(r_a);
        }

        //FOR TEST PURPOSES:
        rest_achievements.add(new REST_Achievement(2));

        r_t_d.myAchievements = rest_achievements;


        h_queue_timelineDays_update.put(t_d, r_t_d);

        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public ArrayList<REST_TimelineSegment> getQueue_timeLineSegments() {
        return queue_timeLineSegments;
    }

    public void addTimeLineSegment(TimelineSegment t_s, TimelineDay t_d) {
        REST_TimelineDay r_t_d = TemporaryDB.getInstance().h_timelineDays.get(t_d);

            DetectedActivity myActivity = t_s.getMyActivity();
            REST_TimelineSegment r_t_s = new REST_TimelineSegment(t_s.getStartTime(), myActivity.getType(), r_t_d);

            h_queue_timelinesegments.put(t_s, r_t_s);
            TemporaryDB.getInstance().h_timelineSegments.put(t_s, r_t_s);
            synchronized (Locks.getInstance().lock) {
                Locks.getInstance().lock.notify();
            }

    }

    public void deleteTimelineSegment(TimelineSegment t_s) {
        REST_TimelineSegment r_t_s = TemporaryDB.getInstance().h_timelineSegments.get(t_s);
        h_queue_timelinesegments_delete.put(t_s, r_t_s);
        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

    public void updateTimelineSegment(TimelineSegment t_s) {
        REST_TimelineSegment r_t_s = TemporaryDB.getInstance().h_timelineSegments.get(t_s);
        r_t_s.activeDistance = t_s.getActiveDistance();
        r_t_s.activeTime = t_s.getActiveTime();
        r_t_s.duration = t_s.getDuration();
        r_t_s.inactiveDistance = t_s.getInactiveDistance();
        r_t_s.inactiveTime = t_s.getInactiveTime();
        r_t_s.startPlace = t_s.getStartPlace();
        r_t_s.myActivity = t_s.getMyActivity().getType();
        r_t_s.startAddress = t_s.getStartAddress();
        r_t_s.userSteps = t_s.getUserSteps();

        ArrayList<REST_Achievement> rest_achievements = new ArrayList<>();
        for(Achievement a: t_s.getMyAchievements()) {
            REST_Achievement r_a = new REST_Achievement(a.getAchievement());
            rest_achievements.add(r_a);
        }

        h_queue_timelinesegments_update.put(t_s, r_t_s);

        synchronized (Locks.getInstance().lock) {
            Locks.getInstance().lock.notify();
        }
    }

}
