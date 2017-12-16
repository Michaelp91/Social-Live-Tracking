package com.slt.control;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.slt.data.TimelineSegment;
import com.slt.definitions.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.slt.control.ApplicationController.getInstance;


/**
 * Task Address Resolver returns the most likely place the device is currently at
 */
public class AddressResolver extends AsyncTask<Object, String, String> {

    /**
     * Tag for the Logger
     */
    private static final String TAG = "AdressResolver";

    /**
     * Geo coder that will resolve the address
     */
    private Geocoder myGeocoder;

    /**
     * Address that is detected
     */
    private String myAddress;

    /**
     * Segment used to store the place that was found
     */
    private TimelineSegment myTimelineSegment;

    /**
     * Location the device is currently at
     */
    private Location myLocation;

    /**
     * Constructor, initializes the needed attributes
     */
    public AddressResolver(){
        //Create a new geo coder
        myGeocoder = new Geocoder(ApplicationController.getContext(), Locale.getDefault());
        myAddress = "";
    }

    /**
     *  Overwritten doInBackground Method, extracts the parameters and starts the address resolving
     * @param objects Parameters the task was called with, the first has to be a TimelineSegment
     *                used to store the result, the second has to be a Location the device is
     *                currently at.
     * @return The detected address
     */
    @Override
    protected String doInBackground(Object... objects) {

        //Check and wait for a network connection
        while(!isNetworkAvailable()) {
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            //Extract the parameters
            Log.d(TAG, "Entered do in Background");
            myTimelineSegment = (TimelineSegment) objects[0];
            myLocation = (Location) objects[1];

            //Get the address for the location
            myAddress = getAddress();
            Log.d(TAG, "Exited do in Background");
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return myAddress;
    }

    /**
     * Checks the Network Status to return if we have a network connection
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
     * Overwritten Method onPostExecute, sends an Intend after the Task is completed
     * @param result Result of the execution of the task
     */
    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute Entered");

        //Store the result of the address resolution
        myTimelineSegment.setAddress(myAddress);

        //Send an intent for the resolved address
        Intent locationIntent = new Intent();
        locationIntent.setAction(Constants.INTENT.ADDRESS_RESOLVED);
        locationIntent.putExtra(Constants.INTENT.ADDRESS_MESSAGE, myAddress);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(locationIntent);

        Log.d(TAG, "onPostExecute Exit");
    }

    /**
     * Method returns the address for a given Location
     * @return The address we found for the Loaction
     */
    private String getAddress(){
        List<Address> addresses = null;

        //Try to resolve the address from the Location
        try {
            addresses = myGeocoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1);
        } catch (IOException ioException) {
            Log.e(TAG, "Service Not Available", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e(TAG, "Invalid Latitude or Longitude Used. " +
                    "Latitude = " + myLocation.getLatitude() + ", Longitude = " +
                    myLocation.getLongitude(), illegalArgumentException);
        }

        //If we find no address return the latitude and longitude as the address
        if (addresses == null || addresses.size()  == 0) {
            return  "Latitude: " + myLocation.getLatitude() + ", Longitude: " +
                    myLocation.getLongitude();
        } else {
            //In all other cases build a string from the first address we dound
            Address address = addresses.get(0);
            StringBuilder completeAddress = new StringBuilder();

            //Build the String
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                completeAddress.append(address.getAddressLine(i));
                if(0 == i)
                    completeAddress.append(", ");
            }

            Log.e(TAG, "Resolved Address: " + completeAddress);
            return completeAddress.toString();
        }
    }
}
