package com.slt.control;

import com.slt.data.Achievement;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;

import java.util.LinkedList;

/**
 * Created by Thorsten on 27.11.2017.
 */

public class AchievementCalculator {

    public static LinkedList<Achievement> calculateSegmentAchievements(double activeDistande,
                                                                       double inactiveDistance,
                                                                       long activeTime,
                                                                       long inactiveTime,
                                                            LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }

    public static LinkedList<Achievement> calculateDayAchievements(LinkedList<TimelineSegment> segment,
                                                                   LinkedList<Achievement> achievementLists){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }

    public static LinkedList<Achievement> calculateWeekAchievements(LinkedList<TimelineDay> days,
                                                                    LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }

    public static LinkedList<Achievement> calculateMonthAchievements(LinkedList<TimelineDay> days,
                                                                     LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }
}
