package com.slt.rest_trackingtimeline;

import com.slt.data.User;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public interface RetrieveTimeline {

    @GET("timeline/{userid}")
    Call<User> getCompleteTimeLine(@Body String userid);

    @GET("locationpoint/{timelinesegmentId}")
    Call<LocationEntry> getLocationEntry(@Body String timelinesegmentId);

    @GET("timelinesegment/{timelinedayId}")
    Call<TimeLineSegment> getTimeLineSegment(@Body String timelinedayId);

    @GET("timelineday/{userid}")
    Call<TimeLineSegment> getTimeLineDay(@Body String userId);

}
