package com.slt.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.slt.control.SharedResources;
import com.slt.data.inferfaces.ServiceInterface;
import com.slt.control.DataProvider;
import com.slt.definitions.Constants;

import java.text.DateFormat;
import java.util.Date;


/**
 * Service for receiving Activity Intents and passing the for further processing
 */
public class ActivityService extends IntentService {

    /*
     * Tag for the Logger
     */
    private static final String TAG = "ActivityService";

    /**
     * Last Detected Activity
     */
    private DetectedActivity currentActivity;

    /**
     * Count of detected before Change
     */
    private int counter;

    /**
     * Define min. number of detections of activity before change
     */
    private final int MIN_DETECTION_COUNT = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        currentActivity = null;
        counter = 0;

        //start the foreground service so we have no limitations for update with later android
        // versions
        if (Build.VERSION.SDK_INT > 26) {
            startForeground(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, SharedResources.getInstance().getForegroundNotification());
        }
    }


    /**
     * Destructor to remove notification bar
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);
    }

    /**
     * Interface to the DataProvider
     */
    private ServiceInterface myDataProvider;

    /**
     * Constructor
     */
    public ActivityService() {
        super("ActivityService");

        //create connection to DataProvider
        myDataProvider = DataProvider.getInstance();


    }

    /**
     * Overwritten onHandleIntentMethod, gets Activity for the intent and starts process of adding it
     * @param intent The intent that was provided to the service
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Intent Received");
        // If the intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) {
            // Get the update
            ActivityRecognitionResult result =
                    ActivityRecognitionResult.extractResult(intent);

            DetectedActivity mostProbableActivity
                    = result.getMostProbableActivity();

            // Get the confidence % (probability)
            int confidence = mostProbableActivity.getConfidence();

            //ignore if the confidence is too small or it is tilting
            if(confidence < 25 || mostProbableActivity.getType() == DetectedActivity.TILTING){
                return;
            }

            // Get the type
            int activityType = mostProbableActivity.getType();

            //create a log entry
            Date timestamp = new Date();
            Log.i(TAG, "Intent Received, Activity: " + mostProbableActivity.toString() +
                    " Confidence:" + confidence + " Type: " + activityType + " Date: " + DateFormat.getTimeInstance().format(timestamp));

            //if no current activity set use it and set the activity
            if(currentActivity == null ){
                currentActivity = mostProbableActivity;
                myDataProvider.updateActivity(mostProbableActivity, timestamp);
                return;
            }
            else {
                //if we have another activity
                if(currentActivity != mostProbableActivity){
                    currentActivity = mostProbableActivity;
                    counter = 0;
                    return;
                }

                //if we have the new activity, but we detected it not for enough times
                if(currentActivity == mostProbableActivity && counter < MIN_DETECTION_COUNT){
                    counter++;
                    return;
                }

                myDataProvider.updateActivity(mostProbableActivity, timestamp);
            }
        }
    }
}