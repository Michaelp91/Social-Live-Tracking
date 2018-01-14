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



        Timeline timeline = new Timeline();
        TimelineDay timelineDay = new TimelineDay(new Date());
        Location location = new Location("");
        location.setLongitude(4.2);
        location.setLatitude(2.3);
        DetectedActivity myActivity = new DetectedActivity(3, 3);
        TimelineSegment timelineSegment = new TimelineSegment(location, new Date(), myActivity, new Date());
        LocationEntry locationEntry = new LocationEntry(location, new Date(), location, new Date());

        /**Tracking Begins*/

        DataUpdater.getInstance().Start();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create Request: Timeline
        DataUpdater.getInstance().setTimeline(timeline);
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

        /**Tracking Ends*/



        //ASYNC
        // After Registration
        //GET "'non Login User' After that jump to the Method FurtherOperations
        //Remember: you can access the "non login User" via TemporaryDB.getInstance().getModel_AppUser()
        OtherRestCalls.retrieveUser_Functionalities("max.mustermann@web.de");





    }

    public static void FurtherOperations() {

        //Get "Non Login User"
        User user = TemporaryDB.getInstance().getModel_AppUser();
        //SYNC: user.getMyImageName() is "Test.png"
        Bitmap bitmap = UsefulMethods.LoadImage(user);
        user.setMyImageName("Test2.png");

        //SYNC: Now Upload the Bitmap and jump to FurtherOperations 2
        UsefulMethods.UploadImageView(bitmap, "Test2.png");

    }

    public static void FurtherOperations2() {
        //SYNC: Retrieve Friends without Timeline Objects
        ArrayList<User> friendList =  OtherRestCalls.retrieveFriends();

        //SYNC: Retrieve all Users without Timeline Objects
        ArrayList<User> allUsers = RetrieveOperations.getInstance().retrieveAllUserLists();

        //SYNC: Retrieve All Friends including their Timelines
        ArrayList<User> friendListIncludingTimeline = OtherRestCalls.retrieveFriendsIncludingTimelines();

        //ASYNC: Update User, After that: Jump to FurtherOperations3
        TemporaryDB.getInstance().getAppUser().myCity = "Tannenhausen";
        OtherRestCalls.updateUser(TemporaryDB.getInstance().getModel_AppUser());
    }

    public static void FurtherOperations3() {

        //SYNC:Get the Complete Timeline:
        Timeline t = RetrieveOperations.getInstance().getCompleteTimeline(TemporaryDB.getInstance().getModel_AppUser());
    }
}
