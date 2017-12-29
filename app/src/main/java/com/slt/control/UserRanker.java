package com.slt.control;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import  com.slt.data.User;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;

/**
 * UserRanker used to Rank all users in the friend list
 */
public class UserRanker {
    /**
     * Tag for the logger
     */
    public static final String TAG = "UserRanker";

    /**
     * Definition of the possible Modes of the Ranker
     */
    public interface METHODS {
        public static int TIME = 0;
        public static int DISTANCE= 1;
        public static int STEPS = 2;
        public static int ACHIEVEMENTS = 3;
    };

    /**
     * Implementation of a comparator used to sort by distance in the last week
     */
    public class SortWeekDistance implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            double u1 = user1.getMyTimeline().getActiveDistanceForWeek();
            double u2 = user2.getMyTimeline().getActiveDistanceForWeek();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }

    /**
     * Implementation of a comparator used to sort by distance in the last month
     */
    public class SortMonthDistance implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            double u1 = user1.getMyTimeline().getActiveDistanceForMonth();
            double u2 = user2.getMyTimeline().getActiveDistanceForMonth();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }

    /**
     * Implementation of a comparator used to sort by steps in the last month
     */
    public class SortMonthSteps implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            int u1 = user1.getMyTimeline().getStepsForMonth();
            int u2 = user2.getMyTimeline().getStepsForMonth();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }

    /**
     * Implementation of a comparator used to sort by steps in the last week
     */
    public class SortWeekSteps implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            int u1 = user1.getMyTimeline().getStepsForWeek();
            int u2 = user2.getMyTimeline().getStepsForWeek();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }

    /**
     * Implementation of a comparator used to sort by time in the last week
     */
    public class SortWeekTime implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            long u1 = user1.getMyTimeline().getActiveTimeForWeek();
            long u2 = user2.getMyTimeline().getActiveTimeForWeek();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }



    /**
     * Implementation of a comparator used to sort by time in the last month
     */
    public class SortMonthTime implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            long u1 = user1.getMyTimeline().getActiveTimeForMonth();
            long u2 = user2.getMyTimeline().getActiveTimeForMonth();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }

    /**
     * Implementation of a comparator used to sort by achievement count in the last week
     */
    public class SortWeekAchievements implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            long u1 = user1.getMyTimeline().getAchievementsForWeek();
            long u2 = user2.getMyTimeline().getAchievementsForWeek();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }



    /**
     * Implementation of a comparator used to sort by achievement count in the last month
     */
    public class SortMonthAchievements implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1 The first user to compare
         * @param user2 The second user to compare
         * @return 0 if both are the same, positive if u1 > u2, negative if u1 < u2
         */
        @Override
        public int compare(User user1, User user2) {
            //get the values
            long u1 = user1.getMyTimeline().getAchievementsForMonth();
            long u2 = user2.getMyTimeline().getAchievementsForMonth();

            //return the comparision results
            if(u1 == u2)
                return 0;
            if(u1 > u2)
                return 1;
            else
                return -1;
        }
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a week
     * @param user Our own user
     * @param friends The friendlist of the user
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A HashMap with the users including the own user and their ranks attached
     */
    public HashMap<User, Integer> userWeekRanking(User user, LinkedList<User> friends,  int method){
        LinkedList<User> allUsers = new LinkedList<>();
        HashMap<User, Integer> result = new HashMap<>();

        allUsers.add(user);
        allUsers.addAll(friends);

        int rank = 0;
        long lastTime = -1;
        int lastSteps = -1;
        double lastDistance = -1;
        int lastAchievementCount = -1;

        switch (method){
            case METHODS.TIME:
                //sort the data
                Collections.sort(allUsers, new SortWeekTime());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getActiveTimeForWeek() != lastTime) {
                        rank++;
                        lastTime = next.getMyTimeline().getActiveTimeForWeek();
                    }
                    result.put(next, rank);
                }

                break;

            case METHODS.DISTANCE:
                //sort the data
                Collections.sort(allUsers, new SortWeekDistance());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getActiveDistanceForWeek() != lastDistance) {
                        rank++;
                        lastDistance = next.getMyTimeline().getActiveDistanceForWeek();
                    }
                    result.put(next, rank);
                }

                break;

            case METHODS.STEPS:
                //sort the data
                Collections.sort(allUsers, new SortWeekSteps());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getStepsForWeek() != lastSteps) {
                        rank++;
                        lastSteps = next.getMyTimeline().getStepsForWeek();
                    }
                    result.put(next, rank);
                }

                break;

            case METHODS.ACHIEVEMENTS:
                //sort the data
                Collections.sort(allUsers, new SortWeekAchievements());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getAchievementsForWeek() != lastAchievementCount) {
                        rank++;
                        lastAchievementCount = next.getMyTimeline().getAchievementsForWeek();
                    }
                    result.put(next, rank);
                }

                break;
        }

        return result;
    }


    /**
     * Get a list of all users in order of their rank in relation to all friends for a month
     * @param user Our own user
     * @param friends The friendlist of the user
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A HashMap with the users including the own user and their ranks attached
     */
    public HashMap<User, Integer> userMonthRanking(User user, LinkedList<User> friends,  int method){
        LinkedList<User> allUsers = new LinkedList<>();
        HashMap<User, Integer> result = new HashMap<>();

        allUsers.add(user);
        allUsers.addAll(friends);

        int rank = 0;
        long lastTime = -1;
        int lastSteps = -1;
        double lastDistance = -1;
        int lastAchievementCount = -1;

        switch (method){
            case METHODS.TIME:
                //sort the data
                Collections.sort(allUsers, new SortMonthTime());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getActiveTimeForMonth() != lastTime) {
                        rank++;
                        lastTime = next.getMyTimeline().getActiveTimeForMonth();
                    }
                    result.put(next, rank);
                }

                break;

            case METHODS.DISTANCE:
                //sort the data
                Collections.sort(allUsers, new SortMonthDistance());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getActiveDistanceForMonth() != lastDistance) {
                        rank++;
                        lastDistance = next.getMyTimeline().getActiveDistanceForMonth();
                    }
                    result.put(next, rank);
                }

                break;

            case METHODS.STEPS:
                //sort the data
                Collections.sort(allUsers, new SortMonthSteps());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getStepsForMonth() != lastSteps) {
                        rank++;
                        lastSteps = next.getMyTimeline().getStepsForMonth();
                    }
                    result.put(next, rank);
                }

                break;

            case METHODS.ACHIEVEMENTS:
                //sort the data
                Collections.sort(allUsers, new SortMonthAchievements());

                // for all users check the value and attach the rank, if changed value increment rank
                for (User next : allUsers) {
                    if (next.getMyTimeline().getAchievementsForMonth() != lastAchievementCount) {
                        rank++;
                        lastAchievementCount = next.getMyTimeline().getAchievementsForMonth();
                    }
                    result.put(next, rank);
                }

                break;
        }

        return result;
    }
}
