package com.slt.restapi.data;

import java.util.ArrayList;

/**
 * REST_Timeline
 */

public class REST_Timeline implements Model{

    /**
     * TAG
     */
    public String TAG;

    /**
     * _id
     */
    public String _id;

    /**
     * user id
     */
    public String user; //UserID

    /**
     * list of achievments
     */
    public ArrayList<REST_Achievement> myAchievements;

    /**
     * constructor
     * @param user
     * @param myAchievements
     */
    public REST_Timeline(String user, ArrayList<REST_Achievement> myAchievements) {
        this.user = user;
        this.myAchievements = myAchievements;
        this.TAG = "Timeline";
    }

    /**
     * standard constructor
     */
    public REST_Timeline() {

    }
}
