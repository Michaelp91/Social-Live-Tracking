package com.slt.control;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Stores the instance of the Google API so every activity can acess it as needed
 */
public class SharedResources {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "SharedResources";

    /**
     * Stores the instance of the shared resources
     */
    private static final SharedResources ourInstance = new SharedResources();

    /**
     * Stores the instance of the API Client
     */
    private GoogleApiClient myGoogleApiClient;

    /**
     * Get the instance of the shared resources
     * @return The instance of the shared resources
     */
    public static SharedResources getInstance() {
        return ourInstance;
    }

    /**
     * Get the instance of the API Client
     * @return The stored instance of the Google API Client
     */
    public GoogleApiClient getMyGoogleApiClient() {
        return myGoogleApiClient;
    }

    /**
     * Set the instance of the Google API Client
     * @param myGoogleApiClient The instance that should be stored
     */
    public void setMyGoogleApiClient(GoogleApiClient myGoogleApiClient) {
        this.myGoogleApiClient = myGoogleApiClient;
    }

    /**
     * Private constructor
     */
    private SharedResources() {
        myGoogleApiClient = null;
    }
}
