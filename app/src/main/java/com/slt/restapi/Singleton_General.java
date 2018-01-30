package com.slt.restapi;

/**
 * Created by Usman Ahmad on 30.12.2017.
 */

public class Singleton_General {
    private static final Singleton_General ourInstance = new Singleton_General();
    public int counter;

    public static Singleton_General getInstance() {
        return ourInstance;
    }

    private Singleton_General() {
        counter = 0;
    }
}
