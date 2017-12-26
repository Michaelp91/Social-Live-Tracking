package com.slt.rest_trackingtimeline.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public class TimeLineDay {
    public Date myDate;
    public ArrayList<Achievement> myAchievements;
    public String timeline; //timelineID

    public TimeLineDay(Date myDate, ArrayList<Achievement> myAchievements, String timeline) {
        this.myDate = myDate;
        this.myAchievements = myAchievements;
        this.timeline = timeline;
    }
}
