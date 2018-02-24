package com.slt.data;

import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.slt.control.ApplicationController;
import com.slt.definitions.Constants;

import java.util.Date;
import java.util.LinkedList;

/**
 * Class User represents a single User of our application
 */
public class User {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "User";

    /**
     * The timeline of the user
     */
    private Timeline myTimeline;

    /**
     * The username of the user
     */
    private String userName;

    /**
     * The email address of the user
     */
    private String email;

    /**
     * The forename of the user
     */
    private String foreName;

    /**
     * The last name of the user
     */
    private String lastName;

    /**
     * An image of the user
     */
    private Bitmap myImage;

    /**
     * The Image Name of the uploaded Bitmap
     */
    private String myImageName;

    /**
     * The age of the user
     */
    private int myAge;

    /**
     * The city of the user
     */
    private String myCity;

    /**
     * The last known location of the user
     */
    private Location lastLocation;

    /**
     * The last update date of the location
     */
    private Date lastLocationUpdateDate;

    /**
     * All users for which we also hold data, ao our friend list
     */
    private LinkedList<User> userList;

    /**
     * Database ID
     */
    private String ID;

    /**
     * Constructor to initialize the minimal data
     *
     * @param userName The username of the user
     */
    public User(String userName) {
        this.myTimeline = new Timeline();
        this.userList = new LinkedList<>();
        this.myImage = null;
        this.userName = userName;
        this.email = "";
        this.foreName = "";
        this.lastName = "";
        this.myAge = 0;
        this.myCity = "";
        this.lastLocationUpdateDate = null;
        this.lastLocation = null;
        this.ID = null;
    }

    /**
     * Constructor to initialize the data
     *
     * @param userName The username of the user
     */
    public User(String userName, Timeline timeline, LinkedList<User> userList) {
        this.myTimeline = timeline;
        this.userList = userList;
        this.myImage = null;
        this.userName = userName;
        this.email = "";
        this.foreName = "";
        this.lastName = "";
        this.myAge = 0;
        this.myCity = "";
        this.lastLocationUpdateDate = null;
        this.lastLocation = null;
        this.ID = null;
    }

    /**
     * Constructor to initialize all the data
     *
     * @param userName The username of the user
     * @param email    The email address of the user
     * @param foreName The forename of the user
     * @param lastName The last name of the user
     * @param image    The image the user has set
     */
    public User(String userName, String email, String foreName, String lastName, Bitmap image, int age, String city, String ID) {
        this.myTimeline = new Timeline();
        this.userList = new LinkedList<>();
        this.myImage = image;
        this.userName = userName;
        this.email = email;
        this.foreName = foreName;
        this.lastName = lastName;
        this.myAge = age;
        this.myCity = city;
        this.ID = ID;
    }

    /**
     * Set the timeline of the user
     * @param t The timeline of the user
     */
    public void setTimeline(Timeline t) {
        this.myTimeline = t;
    }

    /**
     * Update the username, used for a update from the DB
     * @param userName The username we want to set
     * @param userID The DB ID of the user
     */
    public void updateUserName(String userName, String userID) {
        this.userName = userName;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Update the email, used for a update from the DB
     * @param email The email we want to set
     * @param userID The DB ID of the user
     */
    public void updateEmail(String email, String userID) {
        this.email = email;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Update the forename, used for a update from the DB
     * @param foreName The forename we want to set
     * @param userID The DB ID of the user
     */
    public void updateForeName(String foreName, String userID) {
        this.foreName = foreName;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Update the lastname, used for a update from the DB
     * @param lastName The last name we want to set
     * @param userID The DB ID of the user
     */
    public void updateLastName(String lastName, String userID) {
        this.lastName = lastName;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Update the image, used for a update from the DB
     * @param image The image we want to set
     * @param userID The DB ID of the user
     */
    public void updateImage(Bitmap image, String userID) {
        this.myImage = image;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Update the age, used for a update from the DB
     * @param age
     * @param userID The DB ID of the user
     */
    public void updateAge(int age, String userID) {
        this.myAge = age;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Update the city used for a update from the DB
     * @param city
     * @param userID The DB ID of the user
     */
    public void updateCity(String city, String userID){
        this.myCity = city;

        //Send intent to inform about update
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT.USER_INTENT_OTHER_DATA_CHANGED);
        intent.putExtra(Constants.INTENT_EXTRAS.USERID, userID);
        LocalBroadcastManager.getInstance(ApplicationController.getContext()).sendBroadcast(intent);
    }

    /**
     * Get the friend list
     * @return A Linked List containing the friends
     */
    public LinkedList<User> getUserList() {
        return this.userList;
    }

    /**
     * Add a friend to the user list
     *
     * @param user The user we want to add
     */
    public void addFriend(User user) {
        this.userList.add(user);
    }

    /**
     * Delete a user from our friend list
     *
     * @param user The user we want to remove
     */
    public void deleteFriend(User user) {
        for(User usr : this.userList) {
            if (usr.getEmail().equals(user.getEmail())) {
                this.userList.remove(usr);
            }
        }
    }

    /**
     * Get the last known location of the user
     *
     * @return The last known location
     */
    public Location getLastLocation() {
        return lastLocation;
    }

    /**
     * Set the current location of the user
     *
     * @param lastLocation The current location of the user
     */
    public void setLastLocation(Location lastLocation, Date timestamp) {
        this.lastLocation = lastLocation;
        this.lastLocationUpdateDate = timestamp;
    }

    /**
     * Used to set multiple users from the DB
     * @param users A list containing the users to set
     */
    public void setfriends(LinkedList<User> users){
        this.userList.clear();
        this.userList.addAll(users);
    }

    /**
     * Get the date the last location update was performed
     *
     * @return
     */
    public Date getLastLocationUpdateDate() {
        return lastLocationUpdateDate;
    }

    /**
     * Get the timeline of the user
     *
     * @return The timeline
     */
    public Timeline getMyTimeline() {
        return myTimeline;
    }

    /**
     * Get the username of the user
     *
     * @return The username of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the email address of the user
     *
     * @return The emailaddress of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the forename of the user
     *
     * @return The forename of the user
     */
    public String getForeName() {
        return foreName;
    }

    /**
     * Get the last name of the user
     *
     * @return The last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the image the user has added
     *
     * @return The image the user has added
     */
    public Bitmap getMyImage() {
        return myImage;
    }

    /**
     * Get the name of the image
     * @return The name of the image
     */
    public String getMyImageName() {
        return myImageName;
    }

    /**
     * Set the name of the image
     * @param imageName The image name to set
     */
    public void setMyImageName(String imageName) {
        this.myImageName = imageName;
    }


    /**
     * Set a new username for the user
     *
     * @param userName The username that should be set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set a new email address for the user
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set a new forename for the user
     *
     * @param foreName The forename to set
     */
    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    /**
     * Set a new last name for the user
     *
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set a new image for the user
     *
     * @param myImage The new image to set for the user
     */
    public void setMyImage(Bitmap myImage) {
        this.myImage = myImage;
    }

    /**
     * Get the age of the user
     *
     * @return The age of the user
     */
    public int getMyAge() {
        return myAge;
    }

    /**
     * Set the age of the user
     *
     * @param myAge The new age of the user
     */
    public void setMyAge(int myAge) {
        this.myAge = myAge;
    }

    /**
     * Get the city the user has entered
     *
     * @return The city
     */
    public String getMyCity() {
        return myCity;
    }

    /**
     * Set the city of the user
     *
     * @param myCity The new city of the user
     */
    public void setMyCity(String myCity) {
        this.myCity = myCity;
    }

    /**
     * Retrieve the database ID
     *
     * @return The database ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the Datatbase ID
     *
     * @param ID The new Database ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
