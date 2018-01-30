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

    @Override
    public void onCreate() {
        super.onCreate();

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

            //ignore if the confidence is too small
            if(confidence < 25){
                return;
            }

            // Get the type
            int activityType = mostProbableActivity.getType();

            //create a log entry
            Date timestamp = new Date();
            Log.i(TAG, "Intent Received, Activity: " + mostProbableActivity.toString() +
                    " Confidence:" + confidence + " Type: " + activityType + " Date: " + DateFormat.getTimeInstance().format(timestamp));

            myDataProvider.updateActivity(mostProbableActivity, timestamp);
        }
    }
}