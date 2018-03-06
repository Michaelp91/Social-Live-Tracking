package com.slt.restapi;

import com.slt.restapi.data.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Singleton for retrieving data from the server
 */

public class Singleton {

    /**
     * singleton
     */
    private static Singleton singleton;

    /**
     * attributes for storing the data from the database using gson
     */
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

    /**
     * getter
     * @return singleton
     */
    public static Singleton getInstance() {
        singleton = (singleton == null)?new Singleton():singleton;
        return singleton;
    }

    /**
     * getter
     * @return list of all other users(logged in user not included)
     */

    public ArrayList<REST_User_Functionalities> getResponse_allusers() {
        return response_allusers;
    }


    /**
     * getter
     * @return user object
     */
    public REST_User_Functionalities getResponse() {
        return response;
    }

    /**
     * getter
     * @return timeline object
     */
    public REST_Timeline getResponse_timeLine() {
        return response_timeLine;
    }

    /**
     * setter
     * @param response_timeLine
     */
    public void setResponse_timeLine(REST_Timeline response_timeLine) {
        this.response_timeLine = response_timeLine;
    }

    /**
     * getter
     * @return timeline day object
     */
    public REST_TimelineDay getResponse_timelineDay() {
        return response_timelineDay;
    }


    /**
     * setter
     * @param response_timelineDay
     */
    public void setResponse_timelineDay(REST_TimelineDay response_timelineDay) {
        this.response_timelineDay = response_timelineDay;
    }

    /**
     * getter
     * @return response_timelineSegment
     */
    public REST_TimelineSegment getResponse_timelineSegment() {
        return response_timelineSegment;
    }

    /**
     * setter
     * @param response_timelineSegment response_timelineSegment
     */
    public void setResponse_timelineSegment(REST_TimelineSegment response_timelineSegment) {
        this.response_timelineSegment = response_timelineSegment;
    }

    /**
     * setter
     * @return response_locationEntry
     */
    public REST_LocationEntry getResponse_locationEntry() {
        return response_locationEntry;
    }

    /**
     * setter
     * @param response_locationEntries
     */
    public void setResponse_locationEntry(REST_LocationEntry response_locationEntries) {
        this.response_locationEntry = response_locationEntries;
    }

    /**
     * getter
     * @return full timeline object
     */
    public FullTimeLine getResponses() {
        return responses;
    }


    /**
     * getter
     * @return return user object
     */
    public REST_User_Functionalities getResponse_user_functionalities() {
        return response_user_functionalities;
    }

    /**
     * getter
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * getter
     * @return list of friends
     */
    public ArrayList<REST_User_Functionalities> getResponse_friends() {
        return response_friends;
    }
}
