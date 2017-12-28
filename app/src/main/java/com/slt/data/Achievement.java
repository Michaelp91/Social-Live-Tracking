package com.slt.data;

import java.util.Date;

/**
 * Class to store a achievement of the user
 */
public class Achievement {
    /**
     * Stores our achievement as an Integer
     */
    private int achievement;

    /**
     * The Date the achievement was achieved
     */
    private Date timestamp;

    /**
     * Database ID
     */
    private String ID;

    /**
     * Constructor to initialize the Class
     * @param achievement The achievement that has to be stored
     * @param timestamp The Date the achievement was achieved
     */
    public Achievement(int achievement, Date timestamp) {
        this.achievement = achievement;
        this.timestamp = timestamp;
        this.ID = null;
    }

    /**
     * Returns the our achievment
     * @return The achievement
     */
    public int getAchievement() {
        return achievement;
    }


    /**
     * Returns the timestamp for the achievement
     * @return The Date of the achievement
     */
    public Date getTimestamp() {
        return timestamp;
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
