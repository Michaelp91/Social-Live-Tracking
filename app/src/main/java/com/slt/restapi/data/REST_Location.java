package com.slt.restapi.data;

/**
 * REST_Location
 */

public class REST_Location {

    /**
     * latitude
     */
    public double latitude;

    /**
     * longitude
     */
    public double longitude;

    /**
     * saving the longitude and lattitude
     * @param latitude
     * @param longitude
     */
    public REST_Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
