package com.slt.rest_trackingtimeline;

import com.slt.rest_trackingtimeline.data.Achievement;
import com.slt.rest_trackingtimeline.data.Location;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.MyActivity;
import com.slt.rest_trackingtimeline.data.Test;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class TrackingSimulator implements Runnable{


    @Override
    public void run() {
        Test t = new Test("Hello World");
        Achievement newAchievement = new Achievement(0);
        ArrayList<Achievement> myAchievements = new ArrayList<>();
        myAchievements.add(newAchievement);
        TimeLine obj = new TimeLine("5a196bf8d17b7926882f5413", myAchievements);
        TimeLineDay timelineday = new TimeLineDay(new Date(), myAchievements, obj);
        TimeLineSegment timeLineSegment = new TimeLineSegment
                ("Teststr. 84, 6442 Testhausen", "3", 2.4 ,
                        2.0, 2.5, myAchievements, timelineday, MyActivity.RUNNING, 2, 1, 0, 1.2, new Date());
        Location location = new Location(2.3, 4.2);
        LocationEntry locationentry = new LocationEntry(new Date(), 2.1, 2.0,
                location, timeLineSegment);

        DataUpdater.getInstance().Start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().setTimeline(obj);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().addTimelineDay(timelineday);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataUpdater.getInstance().addTimeLineSegment(timeLineSegment);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataUpdater.getInstance().addLocationEntry(locationentry);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
