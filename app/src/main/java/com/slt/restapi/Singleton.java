package com.slt.restapi;

import com.slt.restapi.data.*;
import com.slt.restapi.data.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 25.12.2017.
 */

public class Singleton {
    private static Singleton singleton;
    private REST_User_Functionalities response;
    private FullTimeLine responses;
    private int request_timelinesegmentId;
    private ArrayList<REST_Achievement> achievements;
    private REST_Timeline response_timeLine;
    private REST_TimelineDay response_timelineDay;
    private REST_TimelineSegment response_timelineSegment;
    private REST_LocationEntry response_locationEntry;
    private REST_User_Functionalities response_user_functionalities;
    private ArrayList<REST_User_Functionalities> response_friends;
    private ArrayList<REST_User_Functionalities> response_allusers;
    private String message;

    public static Singleton getInstance() {
        singleton = (singleton == null)?new Singleton():singleton;
        return singleton;
    }

    public ArrayList<REST_User_Functionalities> getResponse_allusers() {
        return response_allusers;
    }



    public REST_User_Functionalities getResponse() {
        return response;
    }

    public REST_Timeline getResponse_timeLine() {
        return response_timeLine;
    }

    public void setResponse_timeLine(REST_Timeline response_timeLine) {
        this.response_timeLine = response_timeLine;
    }

    public REST_TimelineDay getResponse_timelineDay() {
        return response_timelineDay;
    }

    public void setResponse_timelineDay(REST_TimelineDay response_timelineDay) {
        this.response_timelineDay = response_timelineDay;
    }

    public REST_TimelineSegment getResponse_timelineSegment() {
        return response_timelineSegment;
    }

    public void setResponse_timelineSegment(REST_TimelineSegment response_timelineSegment) {
        this.response_timelineSegment = response_timelineSegment;
    }

    public REST_LocationEntry getResponse_locationEntry() {
        return response_locationEntry;
    }

    public void setResponse_locationEntry(REST_LocationEntry response_locationEntries) {
        this.response_locationEntry = response_locationEntries;
    }

    public FullTimeLine getResponses() {
        return responses;
    }

    public REST_User_Functionalities getResponse_user_functionalities() {
        return response_user_functionalities;
    }

    public void setResponse_user_functionalities(REST_User_Functionalities response_user_functionalities) {
        this.response_user_functionalities = response_user_functionalities;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<REST_User_Functionalities> getResponse_friends() {
        return response_friends;
    }
}
