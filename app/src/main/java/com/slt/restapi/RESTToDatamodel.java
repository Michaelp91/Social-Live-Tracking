package com.slt.restapi;

import android.location.Location;

import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
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

    public void buildFriendsObject(ArrayList<REST_User_Functionalities> rest_friends) {
        ArrayList<User> friends = new ArrayList<>();
        for(REST_User_Functionalities r_u_f: rest_friends) {
            //TODO: :(
            User u = new User(r_u_f.userName, r_u_f.email, r_u_f.foreName, r_u_f.lastName, null, r_u_f.myAge, r_u_f.myCity, r_u_f._id);
            friends.add(u);
        }
    }

    public static void buildCompleteUserObjects() {
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

            TimelineDay t_d = TemporaryDB.getInstance().h_timelinedayResolver.get(r_t_s.timeLineDay);
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
        }





    }
}
