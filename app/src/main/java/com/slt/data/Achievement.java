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
     * Constructor to initialize the Class
     * @param achievement The achievement that has to be stored
     * @param timestamp The Date the achievement was achieved
     */
    public Achievement(int achievement, Date timestamp) {
        this.achievement = achievement;
        this.timestamp = timestamp;
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

}
