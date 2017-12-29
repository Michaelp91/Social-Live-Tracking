package com.slt.control;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import  com.slt.data.User;

import java.util.LinkedList;
import java.util.Comparator;

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
    };

    /**
     * Implementation of a comparator used to sort by distance in the last week
     */
    public class SortWeekDistance implements Comparator<User>{
        /**
         * Overwritten comparision method
         * @param user1
         * @param user2
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
         * @param user1
         * @param user2
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
         * @param user1
         * @param user2
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
         * @param user1
         * @param user2
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
         * @param user1
         * @param user2
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
         * @param user1
         * @param user2
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
     * Get a list of all users in order of their rank in relation to all friends for a week
     * @param user Our own user
     * @param friends The friendlist of the user
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A linked list containing a linked list with the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public static LinkedList<LinkedList<User>> userWeekRanking(User user, LinkedList<User> friends,  int method){

        return null;
    }


    /**
     * Get a list of all users in order of their rank in relation to all friends for a month
     * @param user Our own user
     * @param friends The friendlist of the user
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A linked list containing a linked list with the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public static LinkedList<LinkedList<User>> userMonthRanking(User user, LinkedList<User> friends,  int method){

        return null;
    }


}
