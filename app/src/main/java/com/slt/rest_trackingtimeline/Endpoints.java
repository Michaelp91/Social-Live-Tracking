package com.slt.rest_trackingtimeline;

import com.slt.data.User;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Usman Ahmad on 20.12.2017.
 */

public interface Endpoints {
    @GET("timeline/{userid}")
    Call<User> getCompleteTimeLine(@Body String userid);

    @GET("locationpoint/{timelinesegmentId}")
    Call<LocationEntry> getLocationEntry(@Body String timelinesegmentId);

    @GET("timelinesegment/{timelinedayId}")
    Call<TimeLineSegment> getTimeLineSegment(@Body String timelinedayId);

    @GET("timelineday/{userid}")
    Call<TimeLineDay> getTimeLineDay(@Body String userId);

    @POST("locationpoint/new")
    Call<String> createLocationPoint(@Body String locationEntry);

    @POST("timelinesegment/new")
    Call<String> createTimeLineSegment(@Body String timeLineSegment);

    @POST("timelineday/new")
    Call<String> createTimeLineDay(@Body String timeLineDay);

    //Maybe for merging Timeline Segments in Tracking
    @PUT("timelinesegment/{timelinesegmentId}")
    Call<String> updateTimelineSegment(@Body String timelinesegmentId);
}
