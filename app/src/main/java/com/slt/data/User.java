package com.slt.data;

import android.location.Location;
import android.media.Image;
import java.util.Date;

/**
 *
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
    private Image myImage;

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
     * Database ID
     */
    private String ID;

    /**
     * Constructor to initialize the minimal data
     * @param userName The username of the user
     */
    public User(String userName) {
        this.myTimeline = new Timeline();
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
     * @param userName The username of the user
     */
    public User(String userName, Timeline timeline) {
        this.myTimeline = timeline;
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
     * @param userName The username of the user
     * @param email The email address of the user
     * @param foreName The forename of the user
     * @param lastName The last name of the user
     * @param image The image the user has set
     */
    public User(String userName, String email, String foreName, String lastName, Image image, int age, String city, String ID) {
         this.myTimeline = new Timeline();
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
     * Get the last known location of the user
     * @return The last known location
     */
    public Location getLastLocation() {
        return lastLocation;
    }

    /**
     * Set the current location of the user
     * @param lastLocation The current location of the user
     */
    public void setLastLocation(Location lastLocation, Date timestamp) {
        this.lastLocation = lastLocation;
        this.lastLocationUpdateDate = timestamp;
    }

    /**
     * Get the date the last location update was performed
     * @return
     */
    public Date getLastLocationUpdateDate() {
        return lastLocationUpdateDate;
    }
    /**
     * Get the timeline of the user
     * @return The timeline
     */
    public Timeline getMyTimeline() {
        return myTimeline;
    }

    /**
     * Get the username of the user
     * @return The username of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the email address of the user
     * @return The emailaddress of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the forename of the user
     * @return The forename of the user
     */
    public String getForeName() {
        return foreName;
    }

    /**
     * Get the last name of the user
     * @return The last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the image the user has added
     * @return The image the user has added
     */
    public Image getMyImage() {
        return myImage;
    }

    /**
     * Set a new username for the user
     * @param userName The username that should be set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set a new email address for the user
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set a new forename for the user
     * @param foreName The forename to set
     */
    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    /**
     * Set a new last name for the user
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set a new image for the user
     * @param myImage The new image to set for the user
     */
    public void setMyImage(Image myImage) {
        this.myImage = myImage;
    }

    /**
     * Get the age of the user
     * @return The age of the user
     */
    public int getMyAge() {
        return myAge;
    }

    /**
     * Set the age of the user
     * @param myAge The new age of the user
     */
    public void setMyAge(int myAge) {
        this.myAge = myAge;
    }

    /**
     * Get the city the user has entered
     * @return The city
     */
    public String getMyCity() {
        return myCity;
    }

    /**
     * Set the city of the user
     * @param myCity The new city of the user
     */
    public void setMyCity(String myCity) {
        this.myCity = myCity;
    }

    /**
     * Retrieve the database ID
     * @return The database ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the Datatbase ID
     * @param ID The new Database ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
