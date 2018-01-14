package com.slt.restapi;

import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.data.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

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

    private HashMap<String, ArrayList<REST_TimelineSegment>> timelineSegmentsByTimelineDayId = new HashMap<>();
    private HashMap<String, ArrayList<REST_LocationEntry>> locationEntriesByTimelineSegmentId = new HashMap<>();


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





    public static TemporaryDB getInstance() {
        return ourInstance;
    }

    private TemporaryDB() {
        locationEntries = new ArrayList<>();
        timelineDays = new ArrayList<>();
        timeLineSegments = new ArrayList<>();
    }

    public void setLocationEntries(ArrayList<REST_LocationEntry> locationEntries) {
        this.locationEntries = locationEntries;
    }

    public void setTimelineDays(ArrayList<REST_TimelineDay> timelineDays) {
        this.timelineDays = timelineDays;
    }

    public void setTimeLineSegments(ArrayList<REST_TimelineSegment> timeLineSegments) {
        this.timeLineSegments = timeLineSegments;
    }

    public void addLocationEntry(REST_LocationEntry l) {
        int tag = l.int_TAG;
        String timelineSegmentId = l.timelinesegment;

        locationEntriesByTags.put(tag, l);
        ArrayList<REST_LocationEntry> locationEntries = locationEntriesByTimelineSegmentId.get(timelineSegmentId);

        if(locationEntries != null) {
            locationEntries.add(l);
        } else {
            locationEntries = new ArrayList<>();
            locationEntries.add(l);
        }

        locationEntriesByTimelineSegmentId.put(timelineSegmentId, locationEntries);
    }

    public ArrayList<REST_LocationEntry> getLocationEntries() {
        return locationEntries;
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


    public ArrayList<REST_TimelineSegment> findTimelineSegmentsByTDayId(String tId) {
        ArrayList<REST_TimelineSegment> choosedSegments = timelineSegmentsByTimelineDayId.get(tId);

        if(choosedSegments != null) {
            return choosedSegments;
        } else {
            return new ArrayList<REST_TimelineSegment>();
        }
    }

    public void removeTimelineSegmentsByTDayId(String tId) {
        ArrayList<REST_TimelineSegment> rest_timelineSegments = timelineSegmentsByTimelineDayId.get(tId);

        for(REST_TimelineSegment r_t_s: rest_timelineSegments) {
            int int_tag = r_t_s.int_TAG;
            timeLineSegmentsByTags.remove(int_tag);
        }

        timelineSegmentsByTimelineDayId.remove(tId);
    }

    public void removeLocationEntriesByTSegmentId(String t_s_Id) {
        ArrayList<REST_LocationEntry> rest_locationEntries = locationEntriesByTimelineSegmentId.get(t_s_Id);

        for(REST_LocationEntry r_t_s: rest_locationEntries) {
            int int_tag = r_t_s.int_TAG;
            locationEntriesByTags.remove(int_tag);
        }

        timelineSegmentsByTimelineDayId.remove(t_s_Id);
    }


    public ArrayList<REST_LocationEntry> findLocationEntriesByTSegmentId(String segmentId) {
        ArrayList<REST_LocationEntry> choosedEntries = locationEntriesByTimelineSegmentId.get(segmentId);

        if(choosedEntries != null) {
            return choosedEntries;
        } else {
            return new ArrayList<REST_LocationEntry>();
        }
    }

    public REST_TimelineDay findTimeLineDayByObject(REST_TimelineDay search) {

        REST_TimelineDay toSearch = timelineDaysByTags.get(search.int_TAG);

        if(toSearch != null) {
            return toSearch;
        } else {
            return null;
        }
    }

    public ArrayList<REST_TimelineDay> getTimelineDays() {
        return timelineDays;
    }

    public void addTimeLineSegment(REST_TimelineSegment t) {
        int tag = t.int_TAG;
        String timelineDayId = t.timeLineDay;

        timeLineSegmentsByTags.put(tag, t);
        ArrayList<REST_TimelineSegment> timelinesegments = timelineSegmentsByTimelineDayId.get(timelineDayId);

        if(timelinesegments != null) {
            timelinesegments.add(t);
        } else {
            timelinesegments = new ArrayList<>();
            timelinesegments.add(t);
        }

        timelineSegmentsByTimelineDayId.put(timelineDayId, timelinesegments);

        this.timeLineSegments.add(t);
    }



    public REST_TimelineSegment findTimeLineSegmentByObject(REST_TimelineSegment search) {

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

    public REST_User_Functionalities getAppUser() {
        return appUser;
    }

    public void setAppUser(REST_User_Functionalities appUser) {
        //TODO: :(
        model_appUser = new User(appUser.userName, appUser.email, appUser.foreName, appUser.lastName, null, appUser.myAge, appUser.myCity, appUser._id);
        this.appUser = appUser;
    }

    public User setModel_AppUser(User u){return model_appUser;}
    public User getModel_AppUser() {
        return model_appUser;
    }
}
