package com.slt.restapi.data;

import java.util.ArrayList;

/**
 * FullTimeLine
 */

public class FullTimeLine {
    /**
     * array list of timelines
     */
    public ArrayList<REST_Timeline> timelines;

    /**
     * array list of timeline
     */
    public REST_Timeline timeline;

    /**
     * array list of timelinedays
     */
    public ArrayList<REST_TimelineDay> timelinedays;

    /**
     * array list of timeline segments
     */
    public ArrayList<REST_TimelineSegment> timelinesegments;

    /**
     * array list of location entries
     */
    public ArrayList<REST_LocationEntry> locationEntries;
}
