package com.slt.timelineres;

/**
 * Created by Usman Ahmad on 31.10.2017.
 */

public class Singleton {
    private static Singleton singleton;
    private Node node;


    private Singleton() {

    }

    public static Singleton getInstance() {
        if(singleton == null) {
            singleton = new Singleton();
        }

        return singleton;
    }

    public void setTimelinecontent(Node node) {
        this.node = node;
    }

    public Node getTimelinecontent() {
        return node;
    }
}
