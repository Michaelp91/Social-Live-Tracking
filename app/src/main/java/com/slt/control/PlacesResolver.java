package com.slt.control;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import com.slt.data.TimelineSegment;
import com.slt.definitions.Constants;

import static com.slt.control.ApplicationController.getContext;
import static com.slt.control.ApplicationController.getInstance;

/**
 * Task Places Resolver returns the most likely place the device is currently at
 */
public class PlacesResolver extends AsyncTask<Object, String, String> implements
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * Tag for the Logger
     */
    private static final String TAG = "PlacesResolver";

    /**
     * Segment used to store the place that was found
     */
    private TimelineSegment myTimelineSegment;

    /**
     * Location the device is currently at
     */
    private Location myLocation;

    /**
     * Place that is detected
     */
    private String myPlace;

    /**
     * Google API Client we use to get the current place from
     */
    private GoogleApiClient myGoogleApiClient;


    /**
     * Overwritten doInBackground Method, extracts the parameters and starts the place resolving
     *
     * @param objects Parameters the task was called with, the first has to be a TimelineSegment
     *                used to store the result, the second has to be a Location the device is
     *                currently at.
     * @return The detected place
     */
    @Override
    protected String doInBackground(Object... objects) {

        //Create an API Client for places detection
        myGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .build();
        myGoogleApiClient.connect();

        //Check and wait for network connection
        while (!isNetworkAvailable()) {
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Log.d(TAG, "Entered do in Background");

            //Extract the parameters
            myTimelineSegment = (TimelineSegment) objects[0];
            myLocation = (Location) objects[1];

            //Check and wait for the API to be connected
            while (!myGoogleApiClient.isConnected()) {
                synchronized (this) {
                    try {
                        wait(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Get the current place
            callPlaceDetectionApi();

            Log.d(TAG, "Exited do in Background");
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        //Notify about a change in the TimelineSegment
        Intent locationIntent = new Intent();
        locationIntent.setAction(Constants.INTENT.PLACE_RESOLVED);
        locationIntent.putExtra(Constants.INTENT.PLACE_MESSAGE, myPlace);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(locationIntent);

        return myPlace;
    }

    /**
     * Overwritten default onConnectionFailed Method, simply writes to a logger
     *
     * @param connectionResult The result we got from trying to connect the API
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Simply write a message to the logger and do nothing else
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    /**
     * Checks the Network Status to return if we have a network connection
     *
     * @return A Boolean saying if the network is up or down
     */
    private boolean isNetworkAvailable() {
        //Get a ConnectivityManager for the network status
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null;

        //get the network information
        if (connectivityManager != null)
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //Check and return network status
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * @throws SecurityException Send if we do not have the required permission
     */
    private void callPlaceDetectionApi() throws SecurityException {
        //Get the current place of the device
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(myGoogleApiClient, null);

        //Set the result callback
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {


                //Store the first, most likely place we found if it is likely at all
                if (likelyPlaces.getStatus().isSuccess()) {

                    //Create a Location from the place
                    Location targetLocation = new Location("");
                    targetLocation.setLatitude(likelyPlaces.get(0).getPlace().getLatLng().latitude);
                    targetLocation.setLongitude(likelyPlaces.get(0).getPlace().getLatLng().longitude);

                    //Calculate distance between last detected location and the place
                    float distance = myLocation.distanceTo(targetLocation);

                    Log.i(TAG, String.format("Place '%s' with " +
                                    "likelihood: %g, Latitude: %g, Longitude: %g, Distance: %g",
                            likelyPlaces.get(0).getPlace().getName(),
                            likelyPlaces.get(0).getLikelihood(),
                            likelyPlaces.get(0).getPlace().getLatLng().latitude,
                            likelyPlaces.get(0).getPlace().getLatLng().longitude, distance));

                    myPlace = (String) likelyPlaces.get(0).getPlace().getName();
                } else {
                    myPlace = "Unknown";
                }


                //Store the result in the TimelineSegment
                myTimelineSegment.setPlace(myPlace);

                likelyPlaces.release();
            }
        });
    }
}
