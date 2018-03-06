package com.slt.restapi.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * User(without password data)
 */

public class REST_User_Functionalities implements Model{

    /**
     * TAG
     */
    public String TAG;

    /**
     * _id
     */
    public String _id;

    /**
     * user name
     */
    public String userName;

    /**
     * email
     */
    public String email;

    /**
     * fore name
     */
    public String foreName;

    /**
     * last name
     */
    public String lastName;

    /**
     * profile image
     */
    public String myImage; //As Image name

    /**
     * age
     */
    public int myAge;

    /**
     * city
     */
    public String myCity;

    /**
     * last location update
     */
    public Date lastLocationUpdateDate;

    /**
     * list of friends id
     */
    public ArrayList<String> friends;

    /**
     * last location
     */
    public REST_Location lastLocation;
}
