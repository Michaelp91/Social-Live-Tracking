package com.slt.restapi;

import android.location.Location;
import android.support.v4.app.FragmentTransitionImpl;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.TimelineActivity;
import com.slt.data.Achievement;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.fragments.FragmentTimeline;
import com.slt.restapi.data.*;
import com.slt.restapi.data.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class RetrieveOperations {
    public FragmentTimeline context;
    private static final RetrieveOperations ourInstance = new RetrieveOperations();
    public static RetrieveOperations getInstance() {
        return ourInstance;
    }

    public ArrayList<User> retrieveAllUserLists() {
        String debug = "True";
        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getAllUsers(r_u_f);
        JsonObject jsonObject = null;
        try {
            jsonObject =  call.execute().body();
        } catch (Exception e) {
            return new ArrayList<>();
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        ArrayList<REST_User_Functionalities> rest_allUsers =  test.getResponse_allusers();
        ArrayList<User> users = RESTToDatamodel.buildUsersObject(rest_allUsers);
        int i = 0;
        for(User u: users) {
            TemporaryDB.getInstance().h_users.put(u, rest_allUsers.get(i));
            i++;
        }

        return users;
    }

    public Timeline getCompleteTimeline() {
        User user = TemporaryDB.getInstance().getModel_AppUser();

        TemporaryDB.getInstance().h_rest_timelineResolver = new HashMap<>();
        TemporaryDB.getInstance().h_rest_timelinedayResolver = new HashMap<>();
        TemporaryDB.getInstance().h_timelinedayResolver = new HashMap<>();
        TemporaryDB.getInstance().h_rest_timelinesegmentResolver = new HashMap<>();
        TemporaryDB.getInstance().h_timelinesegmentResolver = new HashMap<>();
        TemporaryDB.getInstance().h_rest_locationentryResolver = new HashMap<>();
        TemporaryDB.getInstance().h_locationentryResolver = new HashMap<>();
        TemporaryDB.getInstance().h_timelineResolver = new HashMap<>();

        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getCompleteTimeLine(r_u_f);

        JsonObject jsonObject = null;
        try {
            jsonObject =  call.execute().body();
        } catch (Exception e) {
            return null;
        }

        String json = jsonObject.toString();
        Singleton test = new Gson().fromJson(json, Singleton.class);

        FullTimeLine responses = test.getResponses();

        TemporaryDB.getInstance().setLocationEntries(responses.locationEntries);
        TemporaryDB.getInstance().setTimelineDays(responses.timelinedays);
        TemporaryDB.getInstance().setTimeline(responses.timeline);
        TemporaryDB.getInstance().setTimeLineSegments(responses.timelinesegments);

        for(REST_TimelineDay r_t_d: responses.timelinedays) {
            TimelineDay t_d = new TimelineDay(r_t_d.myDate);
            t_d.setID(r_t_d._id);

            for(REST_Achievement r_a: r_t_d.myAchievements) {
                Achievement a = new Achievement(r_a.achievement, null);
                t_d.addAchievement(a, null); //TODO: Fill Userid in buildcompleteUserObjects
            }
            TemporaryDB.getInstance().h_rest_timelinedayResolver.put(r_t_d._id, r_t_d);
            TemporaryDB.getInstance().h_timelinedayResolver.put(t_d.getID(), t_d);
            TemporaryDB.getInstance().h_timelineDays.put(t_d, r_t_d);
        }

        for(REST_TimelineSegment r_t_s: responses.timelinesegments) {
            DetectedActivity detectedActivity = new DetectedActivity(r_t_s.myActivity, 100);
            TimelineSegment t_s = new TimelineSegment(detectedActivity, r_t_s.startTime);
            t_s.setStartAddress(r_t_s.startAddress);
            t_s.setStartPlace(r_t_s.startPlace);
            t_s.setID(r_t_s._id);
            t_s.setStrUserComments(r_t_s.usercomments);
            for(REST_Achievement r_a: r_t_s.myAchievements) {
                Achievement a = new Achievement(r_a.achievement, null);
                t_s.addAchievement(a, null); //TODO: Fill Userid in buildcompleteUserObjects
            }

            TemporaryDB.getInstance().h_rest_timelinesegmentResolver.put(r_t_s._id, r_t_s);
            TemporaryDB.getInstance().h_timelinesegmentResolver.put(t_s.getID(), t_s);
            TemporaryDB.getInstance().h_timelineSegments.put(t_s, r_t_s);
        }

        for(REST_LocationEntry r_l_e: responses.locationEntries) {
            Location l = new Location(""); //TODO: PROVIDER?
            l.setLatitude(r_l_e.myLocation.latitude);
            l.setLongitude(r_l_e.myLocation.longitude);
            LocationEntry l_e = new LocationEntry(l, r_l_e.myEntryDate, null, null); //TODO:lastLocation, lastDate?
            l_e.setID(r_l_e._id);
            TemporaryDB.getInstance().h_rest_locationentryResolver.put(r_l_e._id, r_l_e);
            TemporaryDB.getInstance().h_locationentryResolver.put(l_e.getID(), l_e);
            TemporaryDB.getInstance().h_locationEntries.put(l_e, r_l_e);
        }



        Timeline t = RESTToDatamodel.buildCompleteTimelineObject();

        LinkedList<Achievement> myAchievements = t.getMyAchievements();
        ArrayList<REST_Achievement> rest_achievements = new ArrayList<>();

        for(Achievement a: myAchievements) {
            REST_Achievement r_a = new REST_Achievement(a.getAchievement());
            rest_achievements.add(r_a);
        }

        TemporaryDB.getInstance().getTimeline().myAchievements = rest_achievements;


        return t;
    }

    public static void getTestData2(Test test) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getTest2(test);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                //Type listType = new TypeToken<List<Test>>() {}.getType();
                Singleton test = new Gson().fromJson(json, Singleton.class);
                boolean debug = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                boolean debug = true;
            }
        });
    }

    public static void getTestData(Test test) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getTest(test);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                Test t = new Gson().fromJson(json, Test.class);
                boolean debug = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                boolean debug = true;
            }
        });
    }

    public REST_TimelineDay getTimeLineDay(String userId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<REST_TimelineDay> call = api.getTimeLineDay(userId);
        REST_TimelineDay newTimeLineDay = call.execute().body();

        return newTimeLineDay;
    }

    public REST_TimelineSegment getTimelineSegment(String timelineDayId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<REST_TimelineSegment> call = api.getTimeLineSegment(timelineDayId);
        REST_TimelineSegment newTimeLineSegment = call.execute().body();

        return newTimeLineSegment;
    }

    public REST_LocationEntry getLocationEntry(String timelineSegmentId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<REST_LocationEntry> call = api.getLocationEntry(timelineSegmentId);
        REST_LocationEntry newLocationEntry = call.execute().body();

        return newLocationEntry;
    }
}
