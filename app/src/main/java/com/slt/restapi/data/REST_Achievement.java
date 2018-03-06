package com.slt.restapi.data;

/**
 * REST_Achievment
 */

public class REST_Achievement {
    /**
     * achievment
     */
    public int achievement;

    /**
     * achievment id
     */
    public String _id;

    /**
     * save the achievment
     * @param achievement
     */
    public REST_Achievement(int achievement) {
        this.achievement = achievement;
    }
}
