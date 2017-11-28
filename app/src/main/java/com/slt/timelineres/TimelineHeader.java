package com.slt.timelineres;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usman Ahmad on 31.10.2017.
 */

public class TimelineHeader {
    private String date;
    private ArrayList<Integer> routeIds; //z.B. 1,2 in Sorted Order


    public String getDatum() {
        return date;
    }

    public TimelineHeader(String date, ArrayList<Integer> routeIds) {
        this.date = date;
        this.routeIds = routeIds;
    }

    public ArrayList<Integer> getRouteIds() {
        return routeIds;
    }
}
