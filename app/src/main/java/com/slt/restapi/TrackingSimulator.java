package com.slt.restapi;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.data.*;
import com.slt.restapi.data.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class TrackingSimulator implements Runnable{

    @Override
    public void run() {
       TrackingTest1();
    }


    /**Scenario:
     *
     * 1. After the User is logged in, Other Informations has to be retrieved(City, last Location, etc.)
     * 2. The User has no Timeline Data in the Database
     * 3. The Tracking Process is starting:
     *  3.1 Create Requests
     *  3.2 Update Requests
     *  3.3 Delete Requests
     *
     * 4.
     *
     *
     */

    public static void TrackingTest1() {
        User user = OtherRestCalls.retrieveUser_Functionalities("max.mustermann@web.de");
        Timeline timeline = new Timeline();
        TimelineDay timelineDay = new TimelineDay(new Date());
        Location location = new Location("");
        location.setLongitude(4.2);
        location.setLatitude(2.3);
        DetectedActivity myActivity = new DetectedActivity(3, 100);
        TimelineSegment timelineSegment = new TimelineSegment( myActivity, new Date(), false);
        timelineSegment.setStartPlace("City Gallerie");
        timelineSegment.setStartAddress("Goldbacherstr. 2, 63739 Aschaffenburg");

        LocationEntry locationEntry = new LocationEntry(location, new Date(), location, new Date());

        DataUpdater.getInstance().Start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create Request: Timeline
        DataUpdater.getInstance().setTimeline(timeline);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create Request: TimelineDay
        DataUpdater.getInstance().addTimelineDay(timelineDay);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create Request: TimeLineSegment
        DataUpdater.getInstance().addTimeLineSegment(timelineSegment, timelineDay);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create Request: LocationEntry
        DataUpdater.getInstance().addLocationEntry(locationEntry, timelineSegment);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //update Request: TimelineDay
        DataUpdater.getInstance().updateTimelineDay(timelineDay);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //update Request: TimelineSegment
        DataUpdater.getInstance().updateTimelineSegment(timelineSegment);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        //Delete Request: timelineSegment
        DataUpdater.getInstance().deleteTimelineSegment(timelineSegment);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Delete Request: LocationEntry
        DataUpdater.getInstance().deleteLocationEntry(locationEntry);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        user.setMyImageName("Test.png");
        user.setMyCity("Tannenhausen");
        OtherRestCalls.updateUser();
        */

    }


    /**Scenario:
     * 1. The User retrieves his Timeline data from the database
     * 2. The Tracking Process is starting:
     *  2.1 Create Requests
     *  2.2 Update Requests
     *  2.3 Delete Requests
     */
    public static void TrackingTest2() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Get Complete Timeline from logged in User
                Timeline t = RetrieveOperations.getInstance().getCompleteTimeline();
                TimelineDay timelineDay = new TimelineDay(new Date());
                Location location = new Location("");
                location.setLongitude(4.2);
                location.setLatitude(2.3);
                DetectedActivity myActivity = new DetectedActivity(3, 3);
                TimelineSegment timelineSegment = new TimelineSegment( myActivity, new Date(), false);
                LocationEntry locationEntry = new LocationEntry(location, new Date(), location, new Date());

                DataUpdater.getInstance().Start();

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Create Request: TimelineDay
                DataUpdater.getInstance().addTimelineDay(timelineDay);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Create Request: TimeLineSegment
                DataUpdater.getInstance().addTimeLineSegment(timelineSegment, timelineDay);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Create Request: LocationEntry
                DataUpdater.getInstance().addLocationEntry(locationEntry, timelineSegment);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //update Request: TimelineDay
                DataUpdater.getInstance().updateTimelineDay(timelineDay);

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //update Request: TimelineSegment
                DataUpdater.getInstance().updateTimelineSegment(timelineSegment);

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        /*
        //Delete Request: timelineSegment
        DataUpdater.getInstance().deleteTimelineSegment(timelineSegment);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Delete Request: LocationEntry
        DataUpdater.getInstance().deleteLocationEntry(locationEntry);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        //ASYNC: Update User, After that: Jump to FurtherOperations3


    }

    public static void UploadTest(final User user)  {
        //SYNC: user.getMyImageName() is "Test.png"

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = UsefulMethods.LoadImage(user);
                user.setMyImageName("Test2.png");

                //ASYNC: Now Upload the Bitmap and jump to FurtherOperations
                UsefulMethods.UploadImageView(bitmap, user.getMyImageName());
            }
        }).start();

    }

    public static void FurtherOperations() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //SYNC: Retrieve Friends without Timeline Objects
                ArrayList<User> friendList =  OtherRestCalls.retrieveFriends();

                //SYNC: Retrieve all Users without Timeline Objects
                ArrayList<User> allUsers = RetrieveOperations.getInstance().retrieveAllUserLists();

                //SYNC: Retrieve All Friends including their Timelines
                ArrayList<User> friendListIncludingTimeline = OtherRestCalls.retrieveFriendsIncludingTimelines();
                TrackingTest2();
            }
        }).start();

    }

    public static void FurtherOperations2() {
        //SYNC:Get the Complete Timeline:

    }
}
