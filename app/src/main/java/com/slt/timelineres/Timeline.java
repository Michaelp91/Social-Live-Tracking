package com.slt.timelineres;

import com.slt.data.*;

import java.util.LinkedList;

/**
 * Created by Usman Ahmad on 03.12.2017.
 */

public class Timeline {
    private LinkedList<com.slt.data.TimelineSegment> mySegments;
    private com.slt.data.TimelineSegment myCurrentSegment;



    public Timeline(){
        mySegments = new LinkedList<com.slt.data.TimelineSegment>();
    }
}
