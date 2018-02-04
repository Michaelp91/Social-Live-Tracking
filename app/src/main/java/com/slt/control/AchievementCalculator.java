package com.slt.control;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Achievement;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.definitions.Constants;
import com.slt.restapi.RetrieveOperations;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * AchievementCalculator, the class is used to calculate all achievements a user has.
 */
public class AchievementCalculator {

    /**
     * Tag for the Logger
     */
    private static final String TAG = "Tupeln_AchievementImage_and_Info Calculator";

    /**
     * Checks if the achievement is already in the list
     * @param achievement The achievement we want to compare to
     * @param achievements The list we check against
     * @return True if the achievement is in the list, false otherwise
     */
    private static Boolean isAchievementInList(int achievement, LinkedList<Achievement> achievements){
        Boolean result = false;

        //check all elements of the list
        for(Achievement ac : achievements) {
            if(ac.getAchievement() == achievement){
                result = true;
                break;
            }
        }
        return result;
    }

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

        //Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(activeDistance >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_DISTANCE_SEGMENT_METER){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_DISTANCE_SEGMENT, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_DISTANCE_SEGMENT,
                        new Date()));
            }
        }

        //Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(steps >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_STEPS_SEGMENT){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_STEPS_SEGMENT, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_STEPS_SEGMENT,
                        new Date()));
            }
        }

        //Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(activeTime >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_ENDURANCE_SEGMENT_MINUTES){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_ENDURANCE_SEGMENT, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_ENDURANCE_SEGMENT,
                        new Date()));
            }
        }

        //Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(activeTime >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_ENDURANCE_SEGMENT_MINUTES){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_ENDURANCE_SEGMENT, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_ENDURANCE_SEGMENT,
                        new Date()));
            }
        }

        return achievements;
    }

    /**
     * Method is used to calculate Day achievements
     * @param segments A linked list containing all the segments for the day
     * @param achievementList The list of user achievements to check for double achievements for a
     *                        segment
     * @return
     */
    public static LinkedList<Achievement> calculateDayAchievements(LinkedList<TimelineSegment> segments,
                                                                   LinkedList<Achievement> achievementList){
        LinkedList<Achievement> achievements = new LinkedList<>();

        long activeTime = 0;
        double activeDistance = 0.0;
        int steps = 0;

        //callculate the active Distance and time over all segments
        for (TimelineSegment segment : segments){
            if(segment.getMyActivity().getType() == DetectedActivity.ON_BICYCLE ||
                    segment.getMyActivity().getType() == DetectedActivity.ON_FOOT ||
                    segment.getMyActivity().getType() == DetectedActivity.WALKING ||
                    segment.getMyActivity().getType() == DetectedActivity.RUNNING) {
                activeTime += segment.getActiveTime();
                activeDistance += segment.getActiveDistance();
                steps += segment.getUserSteps();
            }
        }

        //Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(activeTime >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_DAY_DURATION_MINUTES){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_DURATION, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_DAY_DURATION,
                        new Date()));
            }
        }

        //Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(steps >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_DAY_STEPS_COUNT){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_STEPS, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_DAY_STEPS,
                        new Date()));
            }
        }

        // Check if condition for segment achievement is fullfilled and if achievement not
        // already set
        if(activeDistance >= Constants.ACHIEVEMENT_DEFINITIONS.SPORT_DAY_DISTANCE_METERS){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_DISTANCE, achievementList)){
                achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_DAY_DISTANCE,
                        new Date()));
            }
        }

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
        int count = 0;

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //calculate how many sport achievements the user has
        for(TimelineDay day : days){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_DISTANCE, day.getMyAchievements()) ||
                    isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_STEPS, day.getMyAchievements()) ||
                    isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_DURATION, day.getMyAchievements())){
                count++;
            }
        }

        //if it is a whole week
        if(count == 7 && isAchievementInList(Constants.ACHIEVEMENT.SPORT_WEEK_STREAK, achievementList)){
            achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_WEEK_STREAK, calendar.getTime()));
        }

        return achievements;
    }

    /**
     *  Method is used to calculate achievements for a whole month
     * @param days The days that should be used to calculate the month achievements
     * @param achievementList The list of user achievements to check for double achievements for a
     *                        segment
     * @param monthLength The length of the month the achievement should be calculated for
     * @return
     */
    public static LinkedList<Achievement> calculateMonthAchievements(LinkedList<TimelineDay> days,
                                                                     LinkedList<Achievement>
                                                                             achievementList, int monthLength){
        LinkedList<Achievement> achievements = new LinkedList<>();
        int count = 0;

        //Set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //calculate how many sport achievements the user has
        for(TimelineDay day : days){
            if(isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_DISTANCE, day.getMyAchievements()) ||
                    isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_STEPS, day.getMyAchievements()) ||
                    isAchievementInList(Constants.ACHIEVEMENT.SPORT_DAY_DURATION, day.getMyAchievements())){
                count++;
            }
        }

        //if it is a whole month
        if(count == monthLength && isAchievementInList(Constants.ACHIEVEMENT.SPORT_MONTH_STREAK, achievementList)){
            achievements.add(new Achievement(Constants.ACHIEVEMENT.SPORT_MONTH_STREAK, calendar.getTime()));
        }

        return achievements;
    }

    public static LinkedList<Achievement> getAchievements(int period) {

        //Timeline timeline = RetrieveOperations.getInstance().getCompleteTimeline();
        Timeline timeline = DataProvider.getInstance().getOwnUser().getMyTimeline();

        switch(period) {
            case 0: // day
                return timeline.getAchievementsListForDay();
            case 1: // week
                return timeline.getAchievementsListForWeek();
            case 2: // month
                return timeline.getAchievementsListForMonth();
            default:
                System.err.println("No such period od time.");
                break;
        }

        return null;
    }
}
