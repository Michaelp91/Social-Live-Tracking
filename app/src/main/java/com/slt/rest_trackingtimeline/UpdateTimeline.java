package com.slt.rest_trackingtimeline;



import android.location.Location;

import com.slt.data.Timeline;
import com.slt.data.User;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Usman Ahmad on 16.12.2017.
 */

public interface UpdateTimeline {

    @POST("locationpoint/new")
    Call<LocationEntry> createLocationPoint(@Body LocationEntry locationEntry);

    @POST("timelinesegment/new")
    Call<LocationEntry> createTimeLineSegment(@Body TimeLineSegment timeLineSegment);

    @POST("timelineday/new")
    Call<TimeLineDay> createTimeLineDay(@Body TimeLineSegment timeLineDay);

    //Maybe for merging Timeline Segments in Tracking
    @PUT("timelinesegment/{timelinesegmentId}")
    Call<TimeLineSegment> updateTimelineSegment(@Body String timelinesegmentId);

}
