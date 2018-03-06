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
 * Helper Singleton
 */
public class TemporaryDB {
    /**
     * singleton
     */
    private static final TemporaryDB ourInstance = new TemporaryDB();

    /**
     * list of locationEntries
     */
    private ArrayList<REST_LocationEntry> locationEntries;

    /**
     * timeline
     */
    private REST_Timeline timeline;

    /**
     * list of timeline days
     */
    private ArrayList<REST_TimelineDay> timelineDays;

    /**
     * list of timeline segments
     */
    private ArrayList<REST_TimelineSegment> timeLineSegments;

    /**
     * rest user object
     */
    private REST_User_Functionalities appUser;

    /**
     * user object
     */
    private User model_appUser;

    private User friend;

    /**
     * all the hashmaps for mapping non rest -> rest objects
     */
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


    /**
     * initialize collections
     */
    public void initCollections() {
        h_locationEntries = new HashMap<>();
        h_timelineDays = new HashMap<>();
        h_timelineSegments = new HashMap<>();
        h_users = new HashMap<>();

        timelineDaysByTags = new HashMap<>();
        timeLineSegmentsByTags = new HashMap<>();
        locationEntriesByTags = new HashMap<>();


        //Identifying Data model object, after that edit this identified object by adding children
        h_userResolver = new HashMap<>();
        h_timelineResolver = new HashMap<>();
        h_timelinedayResolver = new HashMap<>();
        h_timelinesegmentResolver = new HashMap<>();
        h_locationentryResolver = new HashMap<>();

        //For loops
        h_rest_userResolver = new HashMap<>();
        h_rest_timelineResolver = new HashMap<>();
        h_rest_timelinedayResolver = new HashMap<>();
        h_rest_timelinesegmentResolver = new HashMap<>();
        h_rest_locationentryResolver = new HashMap<>();
    }


    /**
     * getter
     * @return ourInstance
     */
    public static TemporaryDB getInstance() {
        return ourInstance;
    }

    /**
     * Constructor
     */
    private TemporaryDB() {
        locationEntries = new ArrayList<>();
        timelineDays = new ArrayList<>();
        timeLineSegments = new ArrayList<>();
    }

    /**
     * setter
     * @param locationEntries
     */
    public void setLocationEntries(ArrayList<REST_LocationEntry> locationEntries) {

        for(REST_LocationEntry r_l: locationEntries) {


            r_l.int_TAG = Singleton_General.getInstance().counter;
            this.locationEntriesByTags.put(r_l.int_TAG, r_l);
            Singleton_General.getInstance().counter++;
        }
    }

    /**
     * setter
     * @param timelineDays
     */
    public void setTimelineDays(ArrayList<REST_TimelineDay> timelineDays) {

        for(REST_TimelineDay r_t_d: timelineDays) {
            r_t_d.int_TAG = Singleton_General.getInstance().counter;
            this.timelineDaysByTags.put(r_t_d.int_TAG, r_t_d);
            Singleton_General.getInstance().counter++;
        }
    }

    /**
     * setter
     * @param timeLineSegments
     */
    public void setTimeLineSegments(ArrayList<REST_TimelineSegment> timeLineSegments) {

        for(REST_TimelineSegment r_t_s: timeLineSegments) {
            r_t_s.int_TAG = Singleton_General.getInstance().counter;
            this.timeLineSegmentsByTags.put(r_t_s.int_TAG, r_t_s);
            Singleton_General.getInstance().counter++;
        }
    }

    /**
     * add timeline segment
     * @param r_t_s
     */
    public void addTimeLineSegment(REST_TimelineSegment r_t_s) {
        this.timeLineSegmentsByTags.put(r_t_s.int_TAG, r_t_s);
    }

    /**
     * setter
     * @param timeline
     */
    public void setTimeline(REST_Timeline timeline) {
        this.timeline = timeline;
    }

    /**
     * getter
     * @return
     */
    public REST_Timeline getTimeline() {
        return timeline;
    }

    /**
     * add timeline day
     * @param t
     */
    public void addTimelineDay(REST_TimelineDay t) {
        int tag = t.int_TAG;

        timelineDaysByTags.put(tag, t);
        this.timelineDays.add(t);
    }

    /**
     * find timeline day by object
     * @param search
     * @return
     */
    public REST_TimelineDay findTimeLineDayByObject(REST_TimelineDay search) {

        REST_TimelineDay toSearch = timelineDaysByTags.get(search.int_TAG);

        if(toSearch != null) {
            return toSearch;
        } else {
            return null;
        }
    }


    /**
     * find timeline segment by object
     * @param search
     * @return rest timeline segment object
     */
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


    /**
     * getter
     * @return user
     */

    public User getChoosedFriend() {
        return friend;
    }

    /**
     * setter
     * @param u
     */
    public void setChoosedFriend(User u) {
        this.friend = u;
    }

    /**
     * getter
     * @return
     */
    public REST_User_Functionalities getAppUser() {
        return appUser;
    }


    /**
     * setter
     * @param appUser
     */
    public void setAppUser(REST_User_Functionalities appUser) {
        this.appUser = appUser;
    }

    /**
     * setter
     * @param u
     */
    public void setModel_AppUser(User u){
        this.model_appUser = u;
    }

    /**
     * getter
     * @return
     */
    public User getModel_AppUser() {
        return model_appUser;
    }
}
