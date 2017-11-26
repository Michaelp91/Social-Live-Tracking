package com.slt.data;

import java.util.LinkedList;

/**
 * Created by Thorsten on 07.11.2017.
 */

public class User {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "User";

    private LinkedList<Achievement> myAchievements;
    private Timeline myTimeline;


    public User(){
        myAchievements = new LinkedList<Achievement>();
        myTimeline = new Timeline();

    }
}
