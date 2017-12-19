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


    @GET("timeline/{userid}")
    Call<User> getCompleteTimeLine(@Body String userid);

    @GET("locationpoint/{timelinesegmentId}")
    Call<LocationEntry> getLocationEntry(@Body String timelinesegmentId);

    @GET("timelinesegment/{timelinedayId}")
    Call<TimeLineSegment> getTimeLineSegment(@Body String timelinedayId);

    @GET("timelineday/{userid}")
    Call<TimeLineSegment> getTimeLineDay(@Body String userId);

}
