package com.slt.restapi.data;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 11.01.2018.
 */

public class REST_Timeline implements Model{
    public String TAG;
    public String _id;
    public String user; //UserID
    public ArrayList<REST_Achievement> myAchievements;

    public REST_Timeline(String user, ArrayList<REST_Achievement> myAchievements) {
        this.user = user;
        this.myAchievements = myAchievements;
        this.TAG = "Timeline";
    }

    public REST_Timeline() {

    }
}
