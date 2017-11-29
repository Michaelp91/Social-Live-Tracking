package com.slt.data;

import java.util.LinkedList;

/**
 * Created by Thorsten on 07.11.2017.
 */

public class User {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "User";

    private Timeline myTimeline;
    private String userName;
    private String email;
    private String foreName;
    private String lastName;

    public User(String userName, String email, String foreName, String lastName) {
         this.myTimeline = new Timeline();
        this.userName = userName;
        this.email = email;
        this.foreName = foreName;
        this.lastName = lastName;
    }

    public Timeline getMyTimeline() {
        return myTimeline;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getForeName() {
        return foreName;
    }

    public String getLastName() {
        return lastName;
    }
}
