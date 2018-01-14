package com.slt.restapi;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.slt.data.LocationEntry;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.restapi.data.*;
import com.slt.restapi.data.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class TrackingSimulator implements Runnable{

    HashMap<Test, String> test;
    ArrayList<Test> allTests = new ArrayList<>();
    @Override
    public void run() {

        /*
        test = new HashMap<>();
        Test t = new Test("Hello World");
        allTests.add(t);
        test.put(t, "Hello World");

        Log.d("Test", test.get(t));

        t.test ="Hello Test";

        test.put(t, "Hello Test");

        Log.d("Test", test.get(t));

        allTests.remove(t);

        Log.d("Test", test.get(t));
        */



        /*
        test.put(t, "Hello Test");


        Set<Test> tests = test.keySet();
        Iterator<Test> it = tests.iterator();
        Test t2 = it.next();
        */
        //Log.d("Test",  t2.test);



        REST_Achievement newAchievement = new REST_Achievement(0);
        ArrayList<REST_Achievement> myAchievements = new ArrayList<>();
        myAchievements.add(newAchievement);

        String json = new Gson().toJson(myAchievements);
        REST_Timeline obj = new REST_Timeline("5a196bf8d17b7926882f5413", myAchievements);
      //  REST_TimelineDay timelineday = new REST_TimelineDay(new Date(), myAchievements, obj);
       /*
        REST_TimelineSegment timeLineSegment = new REST_TimelineSegment
                ("Teststr. 84, 6442 Testhausen", "3", 2.4 ,
                        2.0, 2.5, myAchievements, timelineday, MyActivity.RUNNING,
                        2, 1, 0, 1.2, new Date());*/


        REST_Location rest_location = new REST_Location(2.3, 4.2);

        /*
        REST_LocationEntry locationentry = new REST_LocationEntry(new Date(), 2.1, 2.0,
                rest_location, timeLineSegment);*/

        TimelineDay timelineDay = new TimelineDay(new Date());
        Location location = new Location("");
        location.setLongitude(4.2);
        location.setLatitude(2.3);
        DetectedActivity myActivity = new DetectedActivity(3, 3);
        TimelineSegment timelineSegment = new TimelineSegment(new Date(), myActivity, new Date());
        LocationEntry locationEntry = new LocationEntry(location, new Date(), location, new Date());




        DataUpdater.getInstance().Start();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().setTimeline(obj);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().addTimelineDay(timelineDay);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().addTimeLineSegment(timelineSegment, timelineDay);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataUpdater.getInstance().addLocationEntry(locationEntry, timelineSegment);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().updateTimelineDay(timelineDay);

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().updateTimelineSegment(timelineSegment);

        /*
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataUpdater.getInstance().deleteTimelineSegment(timelineSegment);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataUpdater.getInstance().deleteLocationEntry(locationEntry);
        */
    }
}
