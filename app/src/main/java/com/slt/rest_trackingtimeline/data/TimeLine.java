package com.slt.rest_trackingtimeline.data;

import java.util.ArrayList;

/**
 * Created by Usman Ahmad on 26.12.2017.
 */

public class TimeLine implements Model{
    public String TAG;
    public String _id;
    public String user; //UserID
    public ArrayList<Achievement> myAchievements;

    public TimeLine(String user, ArrayList<Achievement> myAchievements) {
        this.user = user;
        this.myAchievements = myAchievements;
        this.TAG = "Timeline";
    }

    public TimeLine() {

    }
}
