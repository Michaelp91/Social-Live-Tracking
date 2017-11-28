package com.slt.timelineres;

/**
 * Created by Usman Ahmad on 06.11.2017.
 */

public class Route {
    private TimelineHeader tHeader;
    private int routeId;
    private Node start;
    private Node destination;
    private Route previousRoute;
    private Route nextRoute;
    private float distance;
    private float duration; //= Dauer
    private String activity;

    public Route(int routeId, Node start, Node destination, float distance, float duration, TimelineHeader tHeader) {
        this.routeId = routeId;
        this.start = start;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.tHeader = tHeader;
    }

    public String getActivity() {
        return activity;
    }

    public TimelineHeader gettHeader() {
        return tHeader;
    }

    public void setPreviousRoute(Route previousRoute) {
        this.previousRoute = previousRoute;
    }

    public void setNextRoute(Route nextRoute) {
        this.nextRoute = nextRoute;
    }

    public int getRouteId() {
        return routeId;
    }

    public Node getStart() {
        return start;
    }

    public Node getDestination() {
        return destination;
    }

    public Route getPreviousRoute() {
        return previousRoute;
    }

    public Route getNextRoute() {
        return nextRoute;
    }

    public float getDistance() {
        return distance;
    }

    public float getDuration() {
        return duration;
    }
}
