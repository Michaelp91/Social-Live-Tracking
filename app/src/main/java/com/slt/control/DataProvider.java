package com.slt.control;

import android.location.Location;
import android.util.Log;

import com.slt.data.User;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import com.slt.data.TimelineSegment;
import com.slt.data.LocationEntry;
import com.slt.data.inferfaces.ServiceInterface;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Data Provider is the central instance for data management and activity detection
 */
public class DataProvider implements ServiceInterface{
    /*
     * Tag for the Logger
     */
    private static final String TAG = "DataProvider";


    private static final DataProvider ourInstance = new DataProvider();

    public static synchronized DataProvider getInstance() {
        return ourInstance;
    }

    private DetectedActivity myCurrentActivity;
    private Location myCurrentLocation;

    private Date changeDate;
    private DetectedActivity nextActivity;

    private static final int MIN_CHANGE_ACTIVITY_INTERVAL = 10;

    private static final double MIN_CHANGE_LOCATION_DISTANCE = 10;

    private Timeline userTimeline;

    private LinkedList<User> userList;


    private LinkedList<DetectedActivity> test;

    private DataProvider() {
        myCurrentActivity = null;
        myCurrentLocation = null;
        this.changeDate = new Date();

        test = new LinkedList<>();
        userTimeline = new Timeline();

        userList = new LinkedList<>();


        DetectedActivity act = new DetectedActivity(DetectedActivity.ON_FOOT, 90);



        test.add(act);
        test.add(act);

        act = new DetectedActivity(DetectedActivity.ON_FOOT, 90);
        test.add(act);

         act = new DetectedActivity(DetectedActivity.ON_BICYCLE, 90);
        test.add(act);
        act = new DetectedActivity(DetectedActivity.STILL, 90);
        test.add(act);
         act = new DetectedActivity(DetectedActivity.ON_FOOT, 90);
        test.add(act);
        act = new DetectedActivity(DetectedActivity.WALKING, 90);
        test.add(act);
         act = new DetectedActivity(DetectedActivity.WALKING, 90);
        test.add(act);


    }

    public int updateActivity(DetectedActivity activity, Date timestamp){

        //if initializations not completed yet
        if(myCurrentLocation == null){
            myCurrentActivity = activity;
            Log.i(TAG, "updateActivity, init current Activity, no location set.");
            return 1;
        }

        if(myCurrentActivity == null){
            myCurrentActivity = activity;

            userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
            Log.i(TAG, "updateActivity, init current Activity, location set, add point.");
        }

        if(myCurrentActivity.getType() != activity.getType() && nextActivity == null){
            nextActivity = activity;
            changeDate = timestamp;
            Log.i(TAG, "updateActivity, set next Activity and change timestamp.");
            return 2;
        }

        if(nextActivity != null) {
            if (nextActivity.getType() == activity.getType() && TimeUnit.MILLISECONDS.toSeconds(
                    timestamp.getTime() - changeDate.getTime()) > MIN_CHANGE_ACTIVITY_INTERVAL) {

                Log.i(TAG, "updateActivity, update User Activity.");
                myCurrentActivity = activity;
                userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
                nextActivity = null;
                changeDate = null;

                //   Intent locationIntent = new Intent();
                //    locationIntent.setAction(LOACTION_ACTION);
                //   locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);
                //   LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);
                return 3;
            }
            else {
                Log.i(TAG, "updateActivity, change Activity, interval not yet passed: "
                        + TimeUnit.MILLISECONDS.toSeconds(timestamp.getTime() - changeDate.getTime()));
            }
        }

        //TODO Really do nothing, or update something to show a change in user data -> timestamp
        Log.i(TAG, "updateActivity, no change, nothing to do.");
        return 0;
    }

    public int updatePosition(Location location, Date timestamp){
        int result = 0;

        if(myCurrentActivity == null){
            myCurrentLocation = location;

            Log.i(TAG, "updatePosition, init current Activity, no location set.");
            return 1;
        }

        if(myCurrentLocation  != null){
            if(location.distanceTo(myCurrentLocation) < MIN_CHANGE_LOCATION_DISTANCE){
                Log.i(TAG, "updatePosition, traveled distance < defined change value, ignore update: " + location.distanceTo(myCurrentLocation));
                return 2;
            }
        }

        Log.i(TAG, "updatePosition, add new location point.");
        myCurrentLocation = location;
        userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);

        return result;
    }

}
