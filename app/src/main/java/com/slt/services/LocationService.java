package com.slt.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.slt.MainActivity;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.inferfaces.ServiceInterface;
import com.slt.definitions.Constants;



import java.text.DateFormat;
import java.util.Date;
import com.slt.R;

import static com.slt.R.mipmap.ic_launcher;

/***
 *  Service class to get Location Updates
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    /*
     * Stores the tag for the logger
     */
    private static final String TAG = "LocationService";

    /**
     * Provides the entry point to Google Play services
     */
    protected GoogleApiClient myGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi
     */
    protected LocationRequest myLocationRequest;

    /**
     * Interface to the DataProvider
     */
    private ServiceInterface myDataProvider;

    /**
     * used to stop the service
     */
    public static volatile boolean shouldContinue = true;

    /***
     *  Default onCreate Method
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }


    /***
     *  Overwritten onStartCommand Method that also starts our GoogleAPI Callbacks
     * @param intent Intent to start the Service with, currently unused
     * @param flags Option flags for the Service, currently unused
     * @param startId StartId of the Service, currently unused
     * @return - Returns START_STICKY since our Service should stay active
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Location Service started ");

        //create connection to DataProvider
        myDataProvider = DataProvider.getInstance();

        //Create the client for the Google API
        myGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Create request parameters for the location requests
        myLocationRequest = LocationRequest.create();
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        myLocationRequest.setInterval(Constants.LOCATION.UPDATE_INTERVAL);
        myLocationRequest.setFastestInterval(Constants.LOCATION.MIN_UPDATE_INTERVAL);

        myGoogleApiClient.connect();

        //Create a forground process with a window to notify the user
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //start the foreground service so we have no limitations for update with later android
        // versions
        startForeground(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, SharedResources.getInstance().getForegroundNotification());

        //return start sticky so the process is restarted automatically
        return START_STICKY;
    }

    /***
     * Overwritten onDestroy method, destroys the API client and stops the forground service
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "Connection Destroyed");
        LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleApiClient, this);
        myGoogleApiClient.disconnect();
        stopForeground(true);

        super.onDestroy();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        Log.i(TAG, "Connected to GoogleApiClient");

        //Create request parameters for the location requests
        myLocationRequest = LocationRequest.create();
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        myLocationRequest.setInterval(Constants.LOCATION.UPDATE_INTERVAL);
        myLocationRequest.setFastestInterval(Constants.LOCATION.MIN_UPDATE_INTERVAL);

        try {
            //starts the request for the location Google API
            LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);

        } catch (SecurityException ex) {
            Log.i(TAG, "Security Exception " + ex.getMessage());
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        // check the condition
        if (!shouldContinue) {
            stopSelf();
            return;
        }


        Log.i(TAG, "Location Changed");

        // Neglect location updates with low accuracy
        if (location.getAccuracy() > Constants.LOCATION.ACCURRACY) {
            return;
        }

        //added for logfile generation
        String time = DateFormat.getTimeInstance().format(new Date());
        String locationData ="Latitude: " + location.getLatitude()+ " Longitude: "+
                location.getLongitude() + " Time: +" + time;
            Log.d(TAG, "Location Data: " + locationData);

            //starts the adding of the location information
        myDataProvider.updatePosition(location, new Date());
    }

    /***
     * Overwritten onConnectionSuspended method, connects the API Client
     * @param cause Cause for the suspended connection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection Suspended");
        myGoogleApiClient.connect();
    }

    /***
     *  Overwritten onConnectionFailed method, currently only writes a log on a failed connection
     * @param result Result that resulted in a failed connection
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        //Write to Log
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /***
     *  Overwritten onBind method, only sends an unsupportedException since we do not want binding
     * @param intent - Intent that was sent
     * @return - Nothing returned since we disallow binding
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not allowed");
    }

}





