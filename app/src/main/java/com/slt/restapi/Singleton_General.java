package com.slt.restapi;

/**
 * Singleton_General
 */

public class Singleton_General {
    /**
     * ourInstance
     */
    private static final Singleton_General ourInstance = new Singleton_General();

    /**
     * counter
     */
    public int counter;

    /**
     * getter
     * @return singleton instance
     */
    public static Singleton_General getInstance() {
        return ourInstance;
    }

    /**
     * constructor
     */
    private Singleton_General() {
        counter = 0;
    }
}
