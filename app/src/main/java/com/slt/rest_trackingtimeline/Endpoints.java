package com.slt.rest_trackingtimeline;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.Test;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;
import com.slt.rest_trackingtimeline.data.User;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Usman Ahmad on 20.12.2017.
 */

public interface Endpoints {

    @POST("fetchTest")
    Call<JsonObject> getTest2(@Body Test json);

    @POST("tests")
    Call<JsonObject> getTest(@Body Test json);

    @POST("timeline/")
    Call<JsonObject> getCompleteTimeLine(@Body User user);

    @GET("locationpoint/{timelinesegmentId}")
    Call<LocationEntry> getLocationEntry(@Body String timelinesegmentId);

    @GET("timelinesegment/{timelinedayId}")
    Call<TimeLineSegment> getTimeLineSegment(@Body String timelinedayId);

    @GET("timelineday/{userid}")
    Call<TimeLineDay> getTimeLineDay(@Body String userId);

    @POST("locationentry/new")
    Call<JsonObject> createLocationEntry(@Body LocationEntry locationEntry);

    @POST("timelinesegment/new")
    Call<JsonObject> createTimeLineSegment(@Body TimeLineSegment timeLineSegment);

    @POST("timelineday/new")
    Call<JsonObject> createTimeLineDay(@Body TimeLineDay timeLineDay);

    @POST("timeline/new")
    Call<JsonObject> createTimeLine(@Body TimeLine timeLine);

    //Maybe for merging Timeline Segments in Tracking
    @PUT("timelinesegment/{timelinesegmentId}")
    Call<String> updateTimelineSegment(@Body String timelinesegmentId);
}
