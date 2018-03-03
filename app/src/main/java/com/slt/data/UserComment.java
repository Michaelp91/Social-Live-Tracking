package com.slt.data;

import java.util.LinkedList;

/**
 * Class for storing the comments of a user
 */
public class UserComment {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "UserComment";

    /**
     * The name of the user that gave the comment
     */
    private String userName;

    /**
     * The comments that user made
     */
    private LinkedList<String> userComments;

    /**
     * Database ID
     */
    private String ID;

    /**
     * Constructor initializes the data
     * @param user The user that made the comment
     * @param comment The first comment the user made
     */
    UserComment(String user, String comment){
        this.userName = user;
        this.userComments = new LinkedList<>();
        this.userComments.add(comment);
        this.ID = null;
    }

    /**
     * Get the name of the user that made the user
     * @return The name of the user
     */
    protected String getUserName() {
        return userName;
    }

    /**
     * Update the user name in case it has changed
     * @param userName The user name we want to change to
      */
    protected void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get all user comments
     * @return A list containing the user comments
     */
    protected LinkedList<String> getUserComments() {
        return new LinkedList<>(userComments);
    }

    /**
     * Adds a user comment to the list
     * @param userComment The user comment to add
     */
    protected void addUserComment(String userComment) {
        this.userComments.add(userComment);
    }

    /**
     * Removes all user comments
     */
    protected void removeUserComments(){
        this.userComments = new LinkedList<>();
    }

    /**
     * Removes a specific user comment
     * @param index The index of the comment to remove
     * @throws Exception An exception is thrown if the index is out of bounds
     */
    protected void removeUserComment(int index) throws Exception{
        //if index out of bounds throw an exception
        if(index < 0 || index > this.userComments.size())
            throw new Exception("Index out of bounds");

        this.userComments.remove(index);
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
