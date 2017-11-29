package com.slt.control;

import android.location.Location;
import com.slt.data.User;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import com.slt.data.TimelineSegment;
import com.slt.data.LocationEntry;
import com.slt.data.inferfaces.ServiceInterface;

import java.util.Date;
import java.util.LinkedList;

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


    private Timeline userTimeline;

    private LinkedList<User> userList;


    private LinkedList<DetectedActivity> test;

    private DataProvider() {
        myCurrentActivity = null;
        myCurrentLocation = null;


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
        int result = 0;

        if(myCurrentLocation == null){
            myCurrentActivity = activity;
            return 1;
        }

        Date now = new Date();
      myCurrentActivity = activity;
        userTimeline.addUserStatus(myCurrentLocation, new Date(), myCurrentActivity);

        //   Intent locationIntent = new Intent();
        //    locationIntent.setAction(LOACTION_ACTION);
        //   locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);

        //   LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);
        return result;
    }

    public int updatePosition(Location location, Date timestamp){
        int result = 0;

        if(myCurrentActivity == null){
            myCurrentLocation = location;
            return 1;
        }

        Date now = new Date();

        myCurrentLocation = location;
        userTimeline.addUserStatus(myCurrentLocation, new Date(), myCurrentActivity);

        return result;
    }

}
