package com.slt.restapi.data;

import com.slt.restapi.Singleton_General;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * REST_TimelineSegment
 */

public class REST_TimelineSegment implements Model {

    /**
     * TAG
     */
    public String TAG;

    /**
     * myActivity
     */
    public int myActivity;

    /**
     * int_TAG
     */
    public int int_TAG;

    /**
     * _id
     */
    public String _id;

    /**
     * startAddress
     */
    public String startAddress;


    /**
     * activeDistance
     */
    public double activeDistance;

    /**
     * inactiveDistance
     */
    public double inactiveDistance;

    /**
     * activeTime
     */
    public double activeTime;

    /**
     * inactiveTime
     */
    public double inactiveTime;

    /**
     * userSteps
     */
    public int userSteps;

    /**
     * startPlace
     */
    public String startPlace;

    /**
     * startTime
     */
    public Date startTime;

    /**
     * duration
     */
    public double duration;

    /**
     * timeline day Id
     */
    public String timeLineDay;

    /**
     * timelineday object
     */
    public REST_TimelineDay timeLineDayObject;

    /**
     * list of achievements
     */
    public ArrayList<REST_Achievement> myAchievements;

    /**
     * list of user comments
     */
    public LinkedList<String> usercomments;

    /**
     * list of images
     */
    public LinkedList<String> images;

    /**
     * Constructor
     * @param startTime
     * @param myActivity
     * @param timeLineDayObject
     */
    public REST_TimelineSegment(Date startTime, int myActivity, REST_TimelineDay timeLineDayObject) {
        this.int_TAG = Singleton_General.getInstance().counter;
        this.TAG = "TimelineSegment";
        this.timeLineDayObject = timeLineDayObject;
        this.myActivity = myActivity;
        this.startTime = startTime;
        this.usercomments = new LinkedList<>();
        this.images = new LinkedList<>();
        Singleton_General.getInstance().counter++;
    }

    public LinkedList<String> getImages() {
        return images;
    }

    public void setImages(LinkedList<String> images) {
        this.images = images;
    }

    public void setUserComments(LinkedList<String> usercomments) {
        this.usercomments = usercomments;
    }

    public LinkedList<String> getUsercomments() {
        return usercomments;
    }



    public void setTimeLineDay(String timeLineDay) {
        this.timeLineDay = timeLineDay;
    }
}
