package com.slt.data;

import android.media.Image;

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
     * Constructor to initialize all the data
     * @param userName The username of the user
     * @param email The email address of the user
     * @param foreName The forename of the user
     * @param lastName The last name of the user
     * @param image The image the user has set
     */
    public User(String userName, String email, String foreName, String lastName, Image image) {
         this.myTimeline = new Timeline();
         this.myImage = image;
        this.userName = userName;
        this.email = email;
        this.foreName = foreName;
        this.lastName = lastName;
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
}
