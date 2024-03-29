package com.slt.control;

import android.location.Location;
import android.util.Log;

import com.slt.data.Achievement;
import com.slt.data.User;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import com.slt.data.inferfaces.ServiceInterface;
import com.slt.restapi.OtherRestCalls;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Data Provider is the central instance for data management and activity detection
 */
public class DataProvider implements ServiceInterface {
    /*
     * Tag for the Logger
     */
    private static final String TAG = "DataProvider";

    /**
     * The instance of the DataProvider
     */
    private static final DataProvider ourInstance = new DataProvider();

    /**
     * Used to retrieve the instance
     *
     * @return The instance of the DataProvider
     */
    public static synchronized DataProvider getInstance() {
        return ourInstance;
    }

    /**
     * Current activity of the user
     */
    private DetectedActivity myCurrentActivity;

    /**
     * Current location of the user
     */
    private Location myCurrentLocation;

    /**
     * Defines the min. distance that has to be between two locations before it is accepted
     */
    private static final double MIN_CHANGE_LOCATION_DISTANCE = 25;

    /**
     * The timeline of the user of the application
     */
    private Timeline userTimeline;

    /**
     * our own user's data
     */
    private User ownUser;

    /**
     * Defines the max. distance for nearby users
     */
    private static final double NEARBY_PEOPLE_DISTANCE = 2000;

    /*
     *  Stores all users including all that are friends
     */
    private LinkedList<User> allUsers;

    /**
     * All users for which we also hold data, so our friend list
     */
    private LinkedList<User> userList;

    /**
     * Determines whether the user started a manual activity
     */
    private Boolean manualMode;

    /**
     * Constructor to initialize the data
     */
    private DataProvider() {
        //at the beginning we do not have any information
        myCurrentActivity = null;
        myCurrentLocation = null;
        this.manualMode = false;
        this.userTimeline = null;

        this.userList = new LinkedList<>();
        this.allUsers = new LinkedList<>();

        this.ownUser = null;

    }

    public Timeline getUserTimeline() {
        return userTimeline;
    }

    /**
     * Add a new unknown Timeline Segment and clear all data
     */
    public void clearData() {
        //  this.userTimeline.addUserStatus(this.myCurrentLocation, new Date(), new DetectedActivity(DetectedActivity.UNKNOWN, 100));


        myCurrentActivity = null;
        myCurrentLocation = null;
        this.manualMode = false;
        this.userTimeline = null;

        this.userList.clear();
        this.allUsers.clear();

        this.ownUser = null;
    }


    /**
     * The method used to react on a change of the activity
     *
     * @param activity  The activity that should be processed
     * @param timestamp The timestamp the activity was detected
     * @return 0 if nothing was changed, 1 if we initialized the data, 2 if a new activity was
     * detected but not used, 3 if the data was entered
     */
    public synchronized int updateActivity(DetectedActivity activity, Date timestamp) {

        //return if we no longer have a user
        if (this.ownUser == null || this.userTimeline == null)
            return 0;

        // if we are in manual mode ignore the change since the user now has the control
        if (manualMode) {
            return 0;
        }

        //if we do not have a location yet, store the activity and do nothing
        if (myCurrentLocation == null) {
            myCurrentActivity = activity;
            SharedResources.getInstance().updateNotification(null, null, timestamp, this.myCurrentActivity);

            Log.i(TAG, "updateActivity, init current Activity, no location set.");
            return 1;
        }

        //if we do not have a current activity yet, store it and add it to the user data
        if (myCurrentActivity == null) {
            myCurrentActivity = activity;
            userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
            SharedResources.getInstance().updateNotification(myCurrentLocation, userTimeline.getTimelineDays().getLast(), timestamp, this.myCurrentActivity);

            Log.i(TAG, "updateActivity, init current Activity, location set, add point.");
            return 3;
        }

        //if we do have the first occurrence of another activity, store it and the timestamp
        if (myCurrentActivity.getType() != activity.getType()) {
            myCurrentActivity = activity;
            SharedResources.getInstance().updateNotification(myCurrentLocation, userTimeline.getTimelineDays().getLast(), timestamp, myCurrentActivity);

            userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
            Log.i(TAG, "updateActivity, add next Activity.");
            return 2;
        }

        Log.i(TAG, "updateActivity, no change, nothing to do.");
        return 0;
    }


    /**
     * Used to update the list of all users from the DB
     *
     * @param users
     */
    public void updateAllUsers(LinkedList<User> users) {
        //Set the new users retrieved
        this.allUsers.clear();
        this.allUsers.addAll(users);

        //remove the own user if he has been added
        User remove = null;

        for (User user : this.allUsers) {
            if (user.getEmail().equals(this.ownUser.getEmail())) {
                remove = user;
            }
        }

        this.allUsers.remove(remove);
    }

    /**
     * Method looks for users by their last name and returns them
     *
     * @param name The last name of the user to look for
     * @return A list containing all users with that name, might be empty
     */
    public LinkedList<User> getUserByName(String name) {
        LinkedList<User> result = new LinkedList<>();

        //check for users with that name
        for (User check : this.allUsers) {
            if (check.getLastName().contains(name)) {
                result.add(check);
            }
        }

        return result;
    }

    /**
     * Get the own user
     *
     * @return The own user
     */
    public User getOwnUser() {
        return ownUser;
    }

    public LinkedList<Achievement> getOwnUserAchievements(int period) {
        return getUserTimeline().getOwnUserAchievements(period);
    }

    /**
     * Set the own user
     *
     * @param ownUser The user to set
     */
    public void setOwnUser(User ownUser) {
        this.ownUser = ownUser;
    }

    /**
     * Little Hack to ensure the user timeline is also the current timeline for easier retrieval
     */
    public void syncTimelineToUser() {
        this.userTimeline = this.ownUser.getMyTimeline();
    }

    /**
     * Used to update friend list from the DB
     *
     * @param users The users to set
     */
    public void changeFriendList(LinkedList<User> users) {
        this.userList.clear();
        this.userList.addAll(users);
        this.ownUser.setfriends(users);
    }

    /**
     * Method to look for a  user by his username
     *
     * @param username The username we look for
     * @return A list of users with the username if he exists, null if not
     */
    public LinkedList<User> getUserByUsername(String username) {
        LinkedList<User> result = new LinkedList<>();

        //check for user that has this username
        for (User check : this.allUsers) {
            if (check.getUserName().contains(username)) {
                result.add(check);
                break;
            }
        }

        return result;
    }

    /**
     * Method checks for all users with a specific email address and returns them
     *
     * @param email The email address we look for
     * @return A list containing all users with that email, might be empty
     */
    public LinkedList<User> getUserByEMail(String email) {
        LinkedList<User> result = new LinkedList<>();

        //check for users with that email address
        for (User check : this.allUsers) {
            if (check.getEmail().contains(email)) {
                result.add(check);
            }
        }

        return result;
    }

    /**
     * Method used to find users in the nearby distance
     *
     * @param distance Distance in which to look for users, if set to 0 the default distance will be
     *                 Used
     * @return A list containing all nearby users, might be empty
     */
    public LinkedList<User> getNearbyUsers(double distance) {
        double checkDistance = DataProvider.NEARBY_PEOPLE_DISTANCE;

        //check if parameter is used
        if (distance > 0) {
            checkDistance = distance;
        }
        LinkedList<User> result = new LinkedList<>();

        //check for users in nearby distance
        for (User check : this.allUsers) {
            if (check.getLastLocation().distanceTo(this.myCurrentLocation) <= checkDistance) {
                result.add(check);
            }
        }

        return result;
    }

    /**
     * The method used to react on a change of the location
     *
     * @param location  The location that was detected
     * @param timestamp The timestamp the location was detected
     * @return 0 if do nothing with the data, 1 if we initialized the data, 2 if we added the
     * location
     */
    public synchronized int updatePosition(Location location, Date timestamp) {

        //return if we no longer have a user
        if (this.ownUser == null || this.userTimeline == null)
            return 0;

        // if we are in manual mode update the values
        if (manualMode) {
            // if location did not really change
            if (location.distanceTo(myCurrentLocation) < MIN_CHANGE_LOCATION_DISTANCE) {
                Log.i(TAG, "updatePosition, manual mode, traveled distance < defined change value, ignore update: "
                        + location.distanceTo(myCurrentLocation));
                return 0;
            }

            this.myCurrentLocation = location;
            this.ownUser.setLastLocation(location, timestamp);

            //REST Call to update the user position
            OtherRestCalls.updateUser(false);


            this.userTimeline.addUserStatus(location, timestamp, this.myCurrentActivity);

            SharedResources.getInstance().updateNotification(location, userTimeline.getTimelineDays().getLast(), timestamp, this.myCurrentActivity);

            return 0;
        }


        //if we do not have an activity yet, store location
        if (myCurrentActivity == null) {
            myCurrentLocation = location;
            this.ownUser.setLastLocation(location, timestamp);

            //REST Call to update the user position
            OtherRestCalls.updateUser(false);

            Log.i(TAG, "updatePosition, init current Activity, no location set.");
            SharedResources.getInstance().updateNotification(location, null, new Date(), null);

            return 1;
        }

        //if we already have a current location
        if (myCurrentLocation != null) {
            //check if we changed by the min. distance, if no ignore new location
            if (location.distanceTo(myCurrentLocation) < MIN_CHANGE_LOCATION_DISTANCE) {
                Log.i(TAG, "updatePosition, traveled distance < defined change value, ignore update: "
                        + location.distanceTo(myCurrentLocation));
                return 0;
            }
        }

        //add the location to the timeline
        myCurrentLocation = location;
        userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
        this.ownUser.setLastLocation(location, timestamp);
        SharedResources.getInstance().updateNotification(location, userTimeline.getTimelineDays().getLast(), timestamp, this.myCurrentActivity);
        Log.i(TAG, "updatePosition, add new location point.");
        return 2;
    }

    /**
     * Used to retrieved the data for other users
     *
     * @return A linked list containing all the users
     */
    public LinkedList<User> getUserList() {
        return userList;
    }

    /**
     * Used to add the data for a user
     *
     * @param user The user we want to add
     */
    public void addUser(User user) {
        this.userList.add(user);
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a week
     *
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A linked list containing the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public HashMap<User, Integer> userWeekRanking(int method) {
        UserRanker ranker = new UserRanker();
        return ranker.userWeekRanking(this.ownUser, this.userList, method);
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a month
     *
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A linked list containing the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public HashMap<User, Integer> userMonthRanking(int method) {
        UserRanker ranker = new UserRanker();
        return ranker.userMonthRanking(this.ownUser, this.userList, method);
    }

    /**
     * Allows to get if the user has added an activity manually
     *
     * @return A Boolean showing if the manual mode is active or not
     */
    public Boolean getManualMode() {
        return manualMode;
    }

    /**
     * Allows to activate the manual mode
     *
     * @param manualMode The mode we want to set
     * @param activity   The activity the user wants to start
     */
    public void setManualMode(Boolean manualMode, DetectedActivity activity) {
        this.manualMode = manualMode;
        this.myCurrentActivity = activity;

        if (manualMode) {
            this.userTimeline.manualStartNewSegment(this.myCurrentLocation, new Date(), activity);
        } else {
            this.userTimeline.manualEndSegment(new Date(), this.myCurrentLocation);
        }
    }
}
