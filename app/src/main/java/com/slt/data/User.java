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
     *
     */
    private Timeline myTimeline;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private String foreName;

    /**
     *
     */
    private String lastName;

    /**
     *
     */
    private Image myImage;

    /**
     *
     * @param userName
     * @param email
     * @param foreName
     * @param lastName
     * @param image
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
     *
     * @return
     */
    public Timeline getMyTimeline() {
        return myTimeline;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getForeName() {
        return foreName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return
     */
    public Image getMyImage() {
        return myImage;
    }
}
