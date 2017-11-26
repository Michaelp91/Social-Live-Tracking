package com.slt.data;

import java.security.spec.ECField;
import java.util.LinkedList;

/**
 * Class for storing the comments of a user
 */
public class UserComment {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "UserComment";

    private String userName;
    private LinkedList<String> userComments;

    UserComment(String user, String comment){
        this.userName = user;
        this.userComments = new LinkedList<>();

        this.userComments.add(comment);
    }

    protected String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    protected LinkedList<String> getUserComments() {
        return new LinkedList<>(userComments);
    }

    protected void setUserComment(String userComment) {
        this.userComments.add(userComment);
    }

    protected void removeUserComments(){
        this.userComments = new LinkedList<>();
    }

    protected void removeUserComment(int index) throws Exception{
        if(index < 0 || index > this.userComments.size())
            throw new Exception("Index out of bounds");

        this.userComments.remove(index);
    }
}
