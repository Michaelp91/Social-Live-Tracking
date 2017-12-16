package com.slt.control;

import com.slt.data.Achievement;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;

import java.util.LinkedList;

/**
 * AchievementCalculator, the class is used to calculate all achievements a user has.
 */
public class AchievementCalculator {
    /**
     * Method is used to calculate the achievements for a single segment, directly calculated on the
     * statistics for the segments.
     * @param activeDistance The Distance the user was active.
     * @param inactiveDistance The Distance the user was inactive
     * @param activeTime The time the user was active
     * @param inactiveTime The time the user was inactive
     * @param steps The steps the user has made if it is a walking segment
     * @param achievementList The list of user achievements to check for double achievements for a
     *                        segment
     * @return
     */
    public static LinkedList<Achievement> calculateSegmentAchievements(double activeDistance,
                                                                       double inactiveDistance,
                                                                       long activeTime,
                                                                       long inactiveTime,
                                                            int steps,
                                                            LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }

    /**
     * Method is used to calculate Day achievements
     * @param segment A linked list containing all the segments for the day
     * @param achievementLists The list of user achievements to check for double achievements for a
     *                        segment
     * @return
     */
    public static LinkedList<Achievement> calculateDayAchievements(LinkedList<TimelineSegment> segment,
                                                                   LinkedList<Achievement> achievementLists){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }

    /**
     * Method is used to calculate achievements for a whole week
     * @param days The days that should be used to calculate the week achievements
     * @param achievementList The list of user achievements to check for double achievements for a
     *                        segment
     * @return
     */
    public static LinkedList<Achievement> calculateWeekAchievements(LinkedList<TimelineDay> days,
                                                                    LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }

    /**
     *  Method is used to calculate achievements for a whole month
     * @param days The days that should be used to calculate the month achievements
     * @param achievementList The list of user achievements to check for double achievements for a
     *                        segment
     * @return
     */
    public static LinkedList<Achievement> calculateMonthAchievements(LinkedList<TimelineDay> days,
                                                                     LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        return achievements;
    }
}
