package com.slt.control;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.TimelineSegment;
import com.slt.data.LocationEntry;
import com.slt.data.inferfaces.ServiceInterface;

import java.util.Date;

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

    private DataProvider() {
        myCurrentActivity = new DetectedActivity(DetectedActivity.UNKNOWN, 100);
        myCurrentLocation = new Location("dummyprovider");
        myCurrentLocation.setLongitude(0.0);
        myCurrentLocation.setLatitude(0.0);
    }

    public int updateActivity(DetectedActivity activity, Date timestamp){
        int result = 0;


        //   Intent locationIntent = new Intent();
        //    locationIntent.setAction(LOACTION_ACTION);
        //   locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);

        //   LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);
        return result;
    }

    public int updatePosition(Location location, Date timestamp){
        int result = 0;
        Date now = new Date();


        return result;
    }

}
