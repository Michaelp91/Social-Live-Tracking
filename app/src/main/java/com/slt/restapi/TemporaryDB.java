package com.slt.restapi;

import android.util.Log;

import com.slt.data.Achievement;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.data.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class TemporaryDB {
    private static final TemporaryDB ourInstance = new TemporaryDB();

    private ArrayList<REST_LocationEntry> locationEntries;
    private REST_Timeline timeline;
    private ArrayList<REST_TimelineDay> timelineDays;
    private ArrayList<REST_TimelineSegment> timeLineSegments;
    private REST_User_Functionalities appUser;
    private User model_appUser;

    public HashMap<LocationEntry, REST_LocationEntry> h_locationEntries = new HashMap<>();
    public HashMap<TimelineDay, REST_TimelineDay> h_timelineDays = new HashMap<>();
    public HashMap<TimelineSegment, REST_TimelineSegment> h_timelineSegments = new HashMap<>();
    public HashMap<User, REST_User_Functionalities> h_users = new HashMap<>();

    private HashMap<Integer, REST_TimelineDay> timelineDaysByTags = new HashMap<>();
    private HashMap<Integer, REST_TimelineSegment> timeLineSegmentsByTags = new HashMap<>();
    private HashMap<Integer, REST_LocationEntry> locationEntriesByTags = new HashMap<>();


    //Identifying Data model object, after that edit this identified object by adding children
    public HashMap<String, User> h_userResolver = new HashMap<>();
    public HashMap<String, Timeline> h_timelineResolver = new HashMap<>();
    public HashMap<String, TimelineDay> h_timelinedayResolver = new HashMap<>();
    public HashMap<String, TimelineSegment> h_timelinesegmentResolver = new HashMap<>();
    public HashMap<String, LocationEntry> h_locationentryResolver = new HashMap<>();

    //For loops
    public HashMap<String, REST_User_Functionalities> h_rest_userResolver = new HashMap<>();
    public HashMap<String, REST_Timeline> h_rest_timelineResolver = new HashMap<>();
    public HashMap<String, REST_TimelineDay> h_rest_timelinedayResolver = new HashMap<>();
    public HashMap<String, REST_TimelineSegment> h_rest_timelinesegmentResolver = new HashMap<>();
    public HashMap<String, REST_LocationEntry> h_rest_locationentryResolver = new HashMap<>();

    private User friend;




    public static TemporaryDB getInstance() {
        return ourInstance;
    }

    private TemporaryDB() {
        locationEntries = new ArrayList<>();
        timelineDays = new ArrayList<>();
        timeLineSegments = new ArrayList<>();
    }

    public void setLocationEntries(ArrayList<REST_LocationEntry> locationEntries) {

        for(REST_LocationEntry r_l: locationEntries) {


            r_l.int_TAG = Singleton_General.getInstance().counter;
            this.locationEntriesByTags.put(r_l.int_TAG, r_l);
            Singleton_General.getInstance().counter++;
        }
    }

    public void setTimelineDays(ArrayList<REST_TimelineDay> timelineDays) {

        for(REST_TimelineDay r_t_d: timelineDays) {
            r_t_d.int_TAG = Singleton_General.getInstance().counter;
            this.timelineDaysByTags.put(r_t_d.int_TAG, r_t_d);
            Singleton_General.getInstance().counter++;
        }
    }

    public void setTimeLineSegments(ArrayList<REST_TimelineSegment> timeLineSegments) {

        for(REST_TimelineSegment r_t_s: timeLineSegments) {
            r_t_s.int_TAG = Singleton_General.getInstance().counter;
            this.timeLineSegmentsByTags.put(r_t_s.int_TAG, r_t_s);
            Singleton_General.getInstance().counter++;
        }
    }

    public void addTimeLineSegment(REST_TimelineSegment r_t_s) {
        this.timeLineSegmentsByTags.put(r_t_s.int_TAG, r_t_s);
    }

    public void setTimeline(REST_Timeline timeline) {
        this.timeline = timeline;
    }

    public REST_Timeline getTimeline() {
        return timeline;
    }

    public void addTimelineDay(REST_TimelineDay t) {
        int tag = t.int_TAG;

        timelineDaysByTags.put(tag, t);
        this.timelineDays.add(t);
    }

    public REST_TimelineDay findTimeLineDayByObject(REST_TimelineDay search) {

        REST_TimelineDay toSearch = timelineDaysByTags.get(search.int_TAG);

        if(toSearch != null) {
            return toSearch;
        } else {
            return null;
        }
    }



    public REST_TimelineSegment findTimeLineSegmentByObject(REST_TimelineSegment search) {
        if(search == null) {
            Log.i("TemporaryDB: ", "----------------------------- REST Null Timeline Segment");
            return null;
        }

        REST_TimelineSegment toSearch = timeLineSegmentsByTags.get(search.int_TAG);

        if(toSearch != null) {
            return toSearch;
        } else {
            return null;
        }
    }

    public ArrayList<REST_TimelineSegment> getTimeLineSegments() {
        return timeLineSegments;
    }

    public User getChoosedFriend() {
        return friend;
    }

    public void setChoosedFriend(User u) {
        this.friend = u;
    }

    public REST_User_Functionalities getAppUser() {
        return appUser;
    }


    public void setAppUser(REST_User_Functionalities appUser) {
        this.appUser = appUser;
    }

    public void setModel_AppUser(User u){
        this.model_appUser = u;
    }
    public User getModel_AppUser() {
        return model_appUser;
    }
}
