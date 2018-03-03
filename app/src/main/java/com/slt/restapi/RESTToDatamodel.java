package com.slt.restapi;

import android.location.Location;

import com.slt.data.Achievement;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.data.REST_Achievement;
import com.slt.restapi.data.REST_LocationEntry;
import com.slt.restapi.data.REST_Timeline;
import com.slt.restapi.data.REST_TimelineDay;
import com.slt.restapi.data.REST_TimelineSegment;
import com.slt.restapi.data.REST_User_Functionalities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Usman Ahmad on 14.01.2018.
 */

public class RESTToDatamodel {

    public User buildUserObject(REST_User_Functionalities r_u_f) {
        //TODO: :(
        User user = new User(r_u_f.userName, r_u_f.email, r_u_f.foreName, r_u_f.lastName, null, r_u_f.myAge, r_u_f.myCity, r_u_f._id);

        return user;
    }

    public static ArrayList<User> buildUsersObject(ArrayList<REST_User_Functionalities> rest_users) {
        ArrayList<User> users = new ArrayList<>();
        for(REST_User_Functionalities r_u_f: rest_users) {
            //TODO: :(
            User u = new User(r_u_f.userName, r_u_f.email, r_u_f.foreName, r_u_f.lastName, null, r_u_f.myAge, r_u_f.myCity, r_u_f._id);
            users.add(u);
        }

        return users;
    }

    public static ArrayList<User> buildCompleteUserObjects() {
        ArrayList<User> timelineUsers = new ArrayList<>();

        Iterator iterator = TemporaryDB.getInstance().h_rest_locationentryResolver.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_LocationEntry r_l_e = (REST_LocationEntry) entry.getValue();
            LocationEntry l_e = TemporaryDB.getInstance().h_locationentryResolver.get(r_l_e._id);

            TimelineSegment t_s = TemporaryDB.getInstance().h_timelinesegmentResolver.get(r_l_e.timelinesegment);
            t_s.addLocationEntry(l_e, null);
        }

        iterator = TemporaryDB.getInstance().h_rest_timelinesegmentResolver.entrySet().iterator();

        int steps = 0;
        double duration = 0;
        double distance = 0;

        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_TimelineSegment r_t_s = (REST_TimelineSegment) entry.getValue();
            TimelineSegment t_s = TemporaryDB.getInstance().h_timelinesegmentResolver.get(r_t_s._id);
            t_s.setID(r_t_s._id);

            TimelineDay t_d = TemporaryDB.getInstance().h_timelinedayResolver.get(r_t_s.timeLineDay);

            if(t_d.getActivity_totalUsersteps().get(t_s.getMyActivity().getType()) != null) {
                steps += t_d.getActivity_totalUsersteps().get(t_s.getMyActivity().getType());
                duration += t_d.getActivity_totalDuration().get(t_s.getMyActivity().getType());
                distance += t_d.getActivity_totalDistance().get(t_s.getMyActivity().getType());

                t_d.getActivity_totalUsersteps().put(t_s.getMyActivity().getType(), steps);
                t_d.getActivity_totalDuration().put(t_s.getMyActivity().getType(), duration);
                t_d.getActivity_totalDistance().put(t_s.getMyActivity().getType(), distance);
            } else {
                t_d.getActivity_totalUsersteps().put(t_s.getMyActivity().getType(), steps);
                t_d.getActivity_totalDuration().put(t_s.getMyActivity().getType(), duration);
                t_d.getActivity_totalDistance().put(t_s.getMyActivity().getType(), distance);
            }

            t_d.insertTimelineSegment(t_s, null);
        }

        iterator = TemporaryDB.getInstance().h_rest_timelinedayResolver.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_TimelineDay r_t_d = (REST_TimelineDay) entry.getValue();
            TimelineDay t_s = TemporaryDB.getInstance().h_timelinedayResolver.get(r_t_d._id);

            Timeline t = TemporaryDB.getInstance().h_timelineResolver.get(r_t_d.timeline);
            t.setMyHistory(t_s, null);
        }

        iterator = TemporaryDB.getInstance().h_rest_timelineResolver.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_Timeline r_t = (REST_Timeline) entry.getValue();
            Timeline t = TemporaryDB.getInstance().h_timelineResolver.get(r_t._id);

            User u = TemporaryDB.getInstance().h_userResolver.get(r_t.user);
            u.setTimeline(t);
            timelineUsers.add(u);
        }

        return timelineUsers;
    }

    public static Timeline buildCompleteTimelineObject() {
        Timeline t = null;

        Iterator iterator = TemporaryDB.getInstance().h_rest_locationentryResolver.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_LocationEntry r_l_e = (REST_LocationEntry) entry.getValue();
            LocationEntry l_e = TemporaryDB.getInstance().h_locationentryResolver.get(r_l_e._id);

            TimelineSegment t_s = TemporaryDB.getInstance().h_timelinesegmentResolver.get(r_l_e.timelinesegment);
            t_s.addLocationEntry(l_e, null);
        }

        iterator = TemporaryDB.getInstance().h_rest_timelinesegmentResolver.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_TimelineSegment r_t_s = (REST_TimelineSegment) entry.getValue();
            TimelineSegment t_s = TemporaryDB.getInstance().h_timelinesegmentResolver.get(r_t_s._id);
            t_s.setID(r_t_s._id);

            TimelineDay t_d = TemporaryDB.getInstance().h_timelinedayResolver.get(r_t_s.timeLineDay);

            int steps = t_s.getUserSteps();
            double duration = t_s.getDuration();
            double distance = t_s.getActiveDistance();

            if(t_d.getActivity_totalUsersteps().get(t_s.getMyActivity().getType()) != null) {
                steps += t_d.getActivity_totalUsersteps().get(t_s.getMyActivity().getType());
                duration += t_d.getActivity_totalDuration().get(t_s.getMyActivity().getType());
                distance += t_d.getActivity_totalDistance().get(t_s.getMyActivity().getType());

                t_d.getActivity_totalUsersteps().put(t_s.getMyActivity().getType(), steps);
                t_d.getActivity_totalDuration().put(t_s.getMyActivity().getType(), duration);
                t_d.getActivity_totalDistance().put(t_s.getMyActivity().getType(), distance);
            } else {
                t_d.getActivity_totalUsersteps().put(t_s.getMyActivity().getType(), steps);
                t_d.getActivity_totalDuration().put(t_s.getMyActivity().getType(), duration);
                t_d.getActivity_totalDistance().put(t_s.getMyActivity().getType(), distance);
            }

            t_d.insertTimelineSegment(t_s, null);
        }

        iterator = TemporaryDB.getInstance().h_rest_timelinedayResolver.entrySet().iterator();

        REST_Timeline r_t = TemporaryDB.getInstance().getTimeline();
        t = new Timeline();


        for(REST_Achievement r_a: r_t.myAchievements) {
            Achievement a = new Achievement(r_a.achievement, null);
            t.addAchievement(a);
        }


        while(iterator.hasNext()) {
            Map.Entry entry =(Map.Entry) iterator.next();
            REST_TimelineDay r_t_d = (REST_TimelineDay) entry.getValue();
            TimelineDay t_s = TemporaryDB.getInstance().h_timelinedayResolver.get(r_t_d._id);
            t.setMyHistory(t_s, null);
        }

        return t;

    }
}
