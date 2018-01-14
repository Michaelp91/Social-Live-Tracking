package com.slt.control;

import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.slt.data.User;

import com.google.android.gms.location.DetectedActivity;
import com.slt.data.Timeline;
import com.slt.data.inferfaces.ServiceInterface;
import com.slt.definitions.Constants;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.data.REST_User;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Data Provider is the central instance for data management and activity detection
 */
public class DataProvider implements ServiceInterface{
    /*
     * Tag for the Logger
     */
    private static final String TAG = "DataProvider";

    /**
     * The instance of the DataProvider
     */
    private static final DataProvider ourInstance = new DataProvider();

    /**
     *  Used to retrieve the instance
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
     * The last time the activity has changed
     */
    private Date changeDate;

    /**
     * The next activity, activity might be already detected but is not the current activity until a
     * fixed time has passed
     */
    private DetectedActivity nextActivity;

    /**
     *  Defines the min. time a new activity has to be present before it gets active
     */
    private static final int MIN_CHANGE_ACTIVITY_INTERVAL = 10;

    /**
     * Defines the min. distance that has to be between two locations before it is accepted
     */
    private static final double MIN_CHANGE_LOCATION_DISTANCE = 10;

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
     * All users for which we also hold data, ao our friend list
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
        this.changeDate = new Date();
        this.manualMode = false;
        //TODO do we have to load data for the current position as well from the server?

        //TODO init the user timeline and the other users with stored data
        userTimeline = new Timeline();
        userList = new LinkedList<>();
        this.allUsers = new LinkedList<>();

        //TODO load real data
        this.ownUser = new User("DEFAULT", this.userTimeline, this.userList);
        REST_User user = new REST_User();
        RetrieveOperations.getInstance().getCompleteTimeline(user);
    }

    //TODO add function for getting other user data and calculation of users in the vicinity

    /**
     * The method used to react on a change of the activity
     * @param activity The activity that should be processed
     * @param timestamp The timestamp the activity was detected
     * @return 0 if nothing was changed, 1 if we initialized the data, 2 if a new activity was
     * detected but not used, 3 if the data was entered
     */
    public synchronized int updateActivity(DetectedActivity activity, Date timestamp){

        // if we are in manual mode ignore the change since the user now has the control
        if(manualMode){
            return 0;
        }

        //if we do not have a location yet, store the activity and do nothing
        if(myCurrentLocation == null){
            myCurrentActivity = activity;
            Log.i(TAG, "updateActivity, init current Activity, no location set.");
            return 1;
        }

        //if we do not have a current activity yet, store it and add it to the user data
        if(myCurrentActivity == null){
            myCurrentActivity = activity;
            userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
            Log.i(TAG, "updateActivity, init current Activity, location set, add point.");
            return 3;
        }

        //if we do have the first occurrence of another activity, store it and the timestamp
        if(myCurrentActivity.getType() != activity.getType() && nextActivity == null){
            nextActivity = activity;
            changeDate = timestamp;
            Log.i(TAG, "updateActivity, set next Activity and change timestamp.");
            return 2;
        }

        //if we do already have a next activity
        if(nextActivity != null) {
            //if the next activity is the same and enough time has passed
            if (nextActivity.getType() == activity.getType() && TimeUnit.MILLISECONDS.toSeconds(
                    timestamp.getTime() - changeDate.getTime()) > MIN_CHANGE_ACTIVITY_INTERVAL) {

                Log.i(TAG, "updateActivity, update User Activity.");
                myCurrentActivity = activity;
                userTimeline.addUserStatus(myCurrentLocation, changeDate, myCurrentActivity);
                nextActivity = null;
                changeDate = null;

                return 3;
            }
            //if not enough time has passed and the activity is not the same
            else if(nextActivity.getType() != activity.getType()){
                nextActivity = activity;
                changeDate = timestamp;
                Log.i(TAG, "updateActivity, change Activity, different activity");
                return 2;
            }
            //in all other cases
            else {
                Log.i(TAG, "updateActivity, change Activity, interval not yet passed: "
                        + TimeUnit.MILLISECONDS.toSeconds(timestamp.getTime() - changeDate.getTime()));
                return 2;
            }
        }

        //TODO Really do nothing, or update something to show a change in user data -> timestamp
        Log.i(TAG, "updateActivity, no change, nothing to do.");
        return 0;
    }

    /**
     * Used to add a user to the friends List
     * @param user The user to add
     */
     public void addUserAsFriend(User user) {

        this.userList.add(user);
         //Send intent to inform about update
         Intent intent = new Intent();
         intent.setAction(Constants.INTENT.DATA_PROVIDER_INTENT_FRIENDS_CHANGED);
         LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
     }

    /**
     * Used to remove a user from the friends list
     * @param user The user to remove
     */
    public void deleteUserAsFriend(User user) {
        this.userList.remove(user);

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.DATA_PROVIDER_INTENT_FRIENDS_CHANGED);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Method looks for users by their last name and returns them
     * @param name The last name of the user to look for
     * @return A list containing all users with that name, might be empty
     */
    public LinkedList<User> getUserByName(String name){
        LinkedList<User> result = new LinkedList<>();
        //TODO implement updating the userList

        //check for users with that name
        for(User check : this.allUsers){
            if(check.getLastName().compareTo(name) == 0){
                result.add(check);
            }
        }

        return result;
    }

    /**
     * Method to look for a specific user by his username
     * @param username The username we look for
     * @return The user with the username if he exists, null if not
     */
    public User getUserByUsername(String username){
        User result = null;
        //TODO implement updating the userList

        //check for user that has this username
        for(User check : this.allUsers){
            if(check.getUserName().compareTo(username) == 0){
                result = check;
                break;
            }
        }

        return result;
    }

    /**
     * Method checks for all users with a specific email address and returns them
     * @param email The email address we look for
     * @return A list containing all users with that email, might be empty
     */
    public LinkedList<User> getUserByEMail(String email){
        LinkedList<User> result = new LinkedList<>();
        //TODO implement updating the userList

        //check for users with that email address
        for(User check : this.allUsers){
            if(check.getEmail().compareTo(email) == 0){
                result.add(check);
            }
        }

        return result;
    }

    /**
     * Method used to find users in the nearby distance
     * @param distance Distance in which to look for users, if set to 0 the default distance will be
     *                 Used
     * @return A list containing all nearby users, might be empty
     */
    public LinkedList<User> getNearbyUsers(double distance){
        double checkDistance = DataProvider.NEARBY_PEOPLE_DISTANCE;

        //check if parameter is used
        if(distance > 0){
            checkDistance = distance;
        }
        LinkedList<User> result = new LinkedList<>();
        //TODO implement updating the userList

        //check for users in nearby distance
        for(User check : this.allUsers){
            if(check.getLastLocation().distanceTo(this.myCurrentLocation) <= checkDistance){
                result.add(check);
            }
        }

        return result;
    }

    /**
     * The method used to react on a change of the location
     * @param location The location that was detected
     * @param timestamp The timestamp the location was detected
     * @return 0 if do nothing with the data, 1 if we initialized the data, 2 if we added the
     * location
     */
    public synchronized  int updatePosition(Location location, Date timestamp){

        // if we are in manual mode update the values
        if(manualMode){
            // if location did not really change
            if(location.distanceTo(myCurrentLocation) < MIN_CHANGE_LOCATION_DISTANCE){
                Log.i(TAG, "updatePosition, manual mode, traveled distance < defined change value, ignore update: " + location.distanceTo(myCurrentLocation));
                return 0;
            }

            this.myCurrentLocation = location;
            this.userTimeline.manualAddLocation(timestamp,location);
            return 0;
        }


        //if we do not have an activity yet, store location
        if(myCurrentActivity == null){
            myCurrentLocation = location;
            this.ownUser.setLastLocation(location, timestamp);
            Log.i(TAG, "updatePosition, init current Activity, no location set.");
            return 1;
        }

        //if we already have a current location
        if(myCurrentLocation  != null){
            //check if we changed by the min. distance, if no ignore new location
            if(location.distanceTo(myCurrentLocation) < MIN_CHANGE_LOCATION_DISTANCE){
                Log.i(TAG, "updatePosition, traveled distance < defined change value, ignore update: " + location.distanceTo(myCurrentLocation));
                return 0;
            }
        }

        //add the location to the timeline
        myCurrentLocation = location;
        userTimeline.addUserStatus(myCurrentLocation, timestamp, myCurrentActivity);
        this.ownUser.setLastLocation(location, timestamp);
        Log.i(TAG, "updatePosition, add new location point.");
        return 2;
    }

    /**
     *  Used to retrieved the data for other users
     * @return A linked list containing all the users
     */
    public LinkedList<User> getUserList() {
        return userList;
    }

    /**
     *  Used to add the data for a user
     * @param user The user we want to add
     */
    public void addUser(User user) {
        this.userList.add(user);
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a week
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A linked list containing the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public HashMap<User, Integer> userWeekRanking(int method){
        UserRanker ranker = new UserRanker();
        return ranker.userWeekRanking(this.ownUser, this.userList, method);
    }

    /**
     * Get a list of all users in order of their rank in relation to all friends for a month
     * @param method Parameter  which values the user wants to compare, possible methods are defined
     *               in UserRanker.METHODS
     * @return A linked list containing the usernames of all users with the rank, the user itself is
     * shown as "Own"
     */
    public HashMap<User, Integer> userMonthRanking( int method){
        UserRanker ranker = new UserRanker();
        return ranker.userMonthRanking(this.ownUser, this.userList,  method);
    }

    /**
     * Allows to get if the user has added an activity manually
     * @return A Boolean showing if the manual mode is active or not
     */
    public Boolean getManualMode() {
        return manualMode;
    }

    /**
     * Allows to activate the manual mode
     * @param manualMode The mode we want to set
     * @param activity The activity the user wants to start
     */
    public void setManualMode(Boolean manualMode, DetectedActivity activity) {
        this.manualMode = manualMode;
        this.myCurrentActivity = activity;

        if(manualMode){
            this.userTimeline.manualStartNewSegment(this.myCurrentLocation, new Date(), activity);
        } else {
            this.userTimeline.manualEndSegment(new Date(), this.myCurrentLocation);
        }
    }
}
