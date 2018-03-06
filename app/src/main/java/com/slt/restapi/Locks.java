package com.slt.restapi;

/**
 * Singleton which holds the lock
 */

public class Locks {
    /**
     * ourInstance
     */
    private static final Locks ourInstance = new Locks();

    /**
     * lock
     */
    public Object lock;

    /**
     * getter
     * @return ourInstance
     */
    public static Locks getInstance() {
        return ourInstance;
    }

    /**
     * initialize lock object
     */
    private Locks() {
        lock = new Object();
    }
}
