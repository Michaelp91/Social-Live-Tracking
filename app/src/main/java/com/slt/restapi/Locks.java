package com.slt.restapi;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class Locks {
    private static final Locks ourInstance = new Locks();
    public Object lock;

    public static Locks getInstance() {
        return ourInstance;
    }

    private Locks() {
        lock = new Object();
    }
}
