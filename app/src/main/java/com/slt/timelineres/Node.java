package com.slt.timelineres;

/**
 * Created by Usman Ahmad on 05.11.2017.
 */

public class Node {
    private int nodeId;
    private float latitude;
    private float longitude;
    private String station;
    private String address;
    private String startzeit;

    public Node(int nodeId, float latitude, float longitude, String station, String address)
    {
        this.nodeId = nodeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.station = station;
        this.address = address;
    }

    public int getNodeId() {
        return nodeId;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getStation() {
        return station;
    }

    public String getAddress() {
        return address;
    }

    public String getStartzeit() {
        return startzeit;
    }
}
