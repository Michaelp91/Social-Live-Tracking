package com.slt.restapi.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usman Ahmad on 13.01.2018.
 */

public class REST_User_Functionalities implements Model{
    public String TAG;
    public String _id;
    public String userName;
    public String email;
    public String foreName;
    public String lastName;
    public String myImage; //As Image name
    public int myAge;
    public String myCity;
    public Date lastLocationUpdateDate;
    public ArrayList<String> friends;
    public REST_Location lastLocation;
}
