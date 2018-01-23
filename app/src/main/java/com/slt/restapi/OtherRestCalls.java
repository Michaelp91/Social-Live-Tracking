package com.slt.restapi;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.data.Achievement;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.fragments.LoginFragment;
import com.slt.fragments.RegisterFragment;
import com.slt.restapi.data.REST_Achievement;
import com.slt.restapi.data.REST_Location;
import com.slt.restapi.data.REST_LocationEntry;
import com.slt.restapi.data.REST_Timeline;
import com.slt.restapi.data.REST_TimelineDay;
import com.slt.restapi.data.REST_TimelineSegment;
import com.slt.restapi.data.REST_User;
import com.slt.restapi.data.REST_User_Functionalities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 13.01.2018.
 */

public class OtherRestCalls {

    public static User createUser_Functionalities(String email) {

        REST_User_Functionalities r_u_f = new REST_User_Functionalities();
        r_u_f.email = email;
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createUser_Functionalities(r_u_f);

        JsonObject jsonObject = null;
        try {
            jsonObject =  call.execute().body();
            Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        } catch (Exception e) {
            return null;
        }

        if(jsonObject != null) {

            Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);

            r_u_f = test.getResponse_user_functionalities();

            User user = new User(r_u_f.userName, r_u_f.email, r_u_f.foreName, r_u_f.lastName, null, r_u_f.myAge, r_u_f.myCity, "");
            user.setMyImageName(r_u_f.myImage);

            TemporaryDB.getInstance().setAppUser(r_u_f);
            TemporaryDB.getInstance().setModel_AppUser(user);
            TemporaryDB.getInstance().h_users.put(user, r_u_f);

            return user;
        } else {
            return null;
        }
    }


    public static User retrieveUser_Functionalities(final String email) {

        REST_User_Functionalities r_u_f = new REST_User_Functionalities();
        r_u_f.email = email;

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getUser_Functionalities(r_u_f);

        JsonObject jsonObject = null;
        try {
            jsonObject =  call.execute().body();
        } catch (Exception e) {
            return null;
        }


        if(jsonObject != null) {

            Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);

            r_u_f = test.getResponse_user_functionalities();

            User user = new User(r_u_f.userName, r_u_f.email, r_u_f.foreName, r_u_f.lastName, null, r_u_f.myAge, r_u_f.myCity, "");
            user.setMyImageName(r_u_f.myImage);


            TemporaryDB.getInstance().setAppUser(r_u_f);
            TemporaryDB.getInstance().setModel_AppUser(user);
            TemporaryDB.getInstance().h_users.put(user, r_u_f);

            return user;
        } else {
            return null;
        }
    }

    public static ArrayList<User> retrieveFriends() {
        TemporaryDB.getInstance().h_rest_userResolver = new HashMap<>();
        TemporaryDB.getInstance().h_userResolver = new HashMap<>();
        TemporaryDB.getInstance().h_users = new HashMap<>();

        ArrayList<User> friends = new ArrayList<>();
        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();

        if(r_u_f != null) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.getFriends(r_u_f);

            JsonObject jsonObject = null;
            try {
                jsonObject =  call.execute().body();
            } catch (Exception e) {
                return new ArrayList<>();
            }

            Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
            ArrayList<REST_User_Functionalities> rest_user_functionalities = test.getResponse_friends();

            for(REST_User_Functionalities r_user_f: rest_user_functionalities) {
                //TODO: Bitmap !=null :)
                User u = new User(r_user_f.userName, r_user_f.email, r_user_f.foreName, r_user_f.lastName, null, r_user_f.myAge, r_user_f.myCity, r_user_f._id);
                u.setMyImageName(r_user_f.myImage);

                Location location = new Location("");
                location.setLongitude(r_user_f.lastLocation.longitude);
                location.setLatitude(r_user_f.lastLocation.latitude);
                u.setLastLocation(location, null);

                TemporaryDB.getInstance().h_rest_userResolver.put(r_u_f._id, r_u_f);
                TemporaryDB.getInstance().h_userResolver.put(u.getID(), u);
                TemporaryDB.getInstance().h_users.put(u, r_user_f);
                friends.add(u);
            }
        }

        return friends;
    }

    public static ArrayList<User> retrieveFriendsIncludingTimelines() {

        TemporaryDB.getInstance().h_rest_timelineResolver = new HashMap<>();
        TemporaryDB.getInstance().h_timelineResolver = new HashMap<>();
        TemporaryDB.getInstance().h_rest_timelinedayResolver = new HashMap<>();
        TemporaryDB.getInstance().h_timelinedayResolver = new HashMap<>();
        TemporaryDB.getInstance().h_rest_timelinesegmentResolver = new HashMap<>();
        TemporaryDB.getInstance().h_timelinesegmentResolver = new HashMap<>();
        TemporaryDB.getInstance().h_rest_locationentryResolver = new HashMap<>();
        TemporaryDB.getInstance().h_locationentryResolver = new HashMap<>();

        ArrayList<User> friends = new ArrayList<>();
        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();

        if(r_u_f != null) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.getCompleteTimeLines(r_u_f);

            JsonObject jsonObject = null;
            try {
                jsonObject =  call.execute().body();
            } catch (Exception e) {
                return new ArrayList<>();
            }

            Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
            ArrayList<REST_Timeline> rest_timelines = test.getResponses().timelines;
            ArrayList<REST_TimelineDay> rest_timelinesdays = test.getResponses().timelinedays;
            ArrayList<REST_TimelineSegment> rest_timelinesegments = test.getResponses().timelinesegments;
            ArrayList<REST_LocationEntry> rest_locationEntries = test.getResponses().locationEntries;

            for(REST_Timeline r_t: rest_timelines) {
                Timeline t = new Timeline();
                t.setID(r_t._id);
                for(REST_Achievement r_a: r_t.myAchievements) {
                    Achievement a = new Achievement(r_a.achievement, null);//TODO: Fill Timestamp
                    t.addAchievement(a);
                }

                TemporaryDB.getInstance().h_rest_timelineResolver.put(r_t._id, r_t);
                TemporaryDB.getInstance().h_timelineResolver.put(t.getID(), t);
            }

            for(REST_TimelineDay r_t_d: rest_timelinesdays) {
                TimelineDay t_d = new TimelineDay(r_t_d.myDate);
                t_d.setID(r_t_d._id);

                for(REST_Achievement r_a: r_t_d.myAchievements) {
                    Achievement a = new Achievement(r_a.achievement, null);
                    t_d.addAchievement(a, null); //TODO: Fill Userid in buildcompleteUserObjects
                }
                TemporaryDB.getInstance().h_rest_timelinedayResolver.put(r_t_d._id, r_t_d);
                TemporaryDB.getInstance().h_timelinedayResolver.put(t_d.getID(), t_d);
            }

            for(REST_TimelineSegment r_t_s: rest_timelinesegments) {
                DetectedActivity detectedActivity = new DetectedActivity(r_t_s.myActivity, 100);
                TimelineSegment t_s = new TimelineSegment(detectedActivity, r_t_s.startTime, false);
                t_s.setID(r_t_s._id);
                for(REST_Achievement r_a: r_t_s.myAchievements) {
                    Achievement a = new Achievement(r_a.achievement, null);
                    t_s.addAchievement(a, null); //TODO: Fill Userid in buildcompleteUserObjects
                }

                TemporaryDB.getInstance().h_rest_timelinesegmentResolver.put(r_t_s._id, r_t_s);
                TemporaryDB.getInstance().h_timelinesegmentResolver.put(t_s.getID(), t_s);
            }

            for(REST_LocationEntry r_l_e: rest_locationEntries) {
                Location l = new Location(""); //TODO: PROVIDER?
                l.setLatitude(r_l_e.myLocation.latitude);
                l.setLongitude(r_l_e.myLocation.longitude);
                LocationEntry l_e = new LocationEntry(l, r_l_e.myEntryDate, null, null); //TODO:lastLocation, lastDate?
                l_e.setID(r_l_e._id);
                TemporaryDB.getInstance().h_rest_locationentryResolver.put(r_l_e._id, r_l_e);
                TemporaryDB.getInstance().h_locationentryResolver.put(l_e.getID(), l_e);
            }

            friends = RESTToDatamodel.buildCompleteUserObjects();
        }

        return friends;
    }


    public static void updateUser(boolean includeFriendsUpdate) {


        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        User user = TemporaryDB.getInstance().getModel_AppUser();
        r_u_f.email = user.getEmail();
        r_u_f.foreName = user.getForeName();
        r_u_f.lastName = user.getLastName();

        if(includeFriendsUpdate) {
            LinkedList<User> users = user.getUserList();
            ArrayList<String> ids = new ArrayList<>();

            for (User u : users) {
                REST_User_Functionalities friend = TemporaryDB.getInstance().h_users.get(u);

                if (friend != null) {
                    ids.add(friend._id);
                }
            }

            r_u_f.friends = ids;
        }


        Location l = user.getLastLocation();

        if(l == null) {
            l = new Location("");
          l.setLatitude(0);
          l.setLongitude(0);
        }
        REST_Location r_l = new REST_Location(l.getLatitude(), l.getLongitude());

        r_u_f.lastLocation = r_l;
        r_u_f.lastLocationUpdateDate = user.getLastLocationUpdateDate();
        r_u_f.myAge = user.getMyAge();
        r_u_f.myCity = user.getMyCity();
        r_u_f.myImage = user.getMyImageName();

        if(r_u_f != null) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.updateUser_Functionalities(r_u_f);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        //TODO Wieder aktivieren wenn es geht
                 //       TrackingSimulator.UploadTest(TemporaryDB.getInstance().getModel_AppUser());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        }
    }


}
