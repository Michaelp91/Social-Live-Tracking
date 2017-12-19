package com.slt.rest_trackingtimeline;

import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class RetrieveOperations {
    public TimeLineDay getTimeLineDay(String userId) {

        return new TimeLineDay();
    }

    public TimeLineSegment getTimelineSegment(String timelineDayId) {
        return new TimeLineSegment();
    }

    public LocationEntry getLocationEntry(String timelineSegmentId) {
        return new LocationEntry();
    }
}
