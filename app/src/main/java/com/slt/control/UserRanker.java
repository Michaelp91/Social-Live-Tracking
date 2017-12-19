package com.slt.control;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import  com.slt.data.User;

import java.util.LinkedList;

/**
 * UserRanker used to Rank all users in the friend list
 */
public class UserRanker {

    /**
     * Get the Rank of the user in relation to its friends list for a week
     * @param myTimeline The timeline of the user
     * @param friends The friendlist of the user
     * @param activity - The activity we want to compare for, if null will return a comparison of
     *                 the sum of all sport activities
     * @return The rank of the user in comparison to their friends
     */
    public static int ownWeekRank(Timeline myTimeline, LinkedList<User> friends, DetectedActivity activity){

        return -1;
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a week
     * @param myTimeline The timeline of the user
     * @param friends The friendlist of the user
     * @param activity - The activity we want to compare for, if null will return a comparison of
     *                 the sum of all sport activities
     * @return A linked list containing a linked list with the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public static LinkedList<LinkedList<User>> userWeekRanking(Timeline myTimeline, LinkedList<User> friends, DetectedActivity activity){

        return null;
    }

    /**
     * Get the Rank of the user in relation to its friends list for a month
     * @param myTimeline The timeline of the user
     * @param friends The friendlist of the user
     * @param activity - The activity we want to compare for, if null will return a comparison of
     *                 the sum of all sport activities
     * @return The rank of the user in comparison to their friends
     */
    public static int ownMonthRank(Timeline myTimeline, LinkedList<User> friends, DetectedActivity activity){

        return -1;
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a month
     * @param myTimeline The timeline of the user
     * @param friends The friendlist of the user
     * @param activity - The activity we want to compare for, if null will return a comparison of
     *                 the sum of all sport activities
     * @return A linked list containing a linked list with the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public static LinkedList<LinkedList<User>> userMonthRanking(Timeline myTimeline, LinkedList<User> friends, DetectedActivity activity){

        return null;
    }


}
