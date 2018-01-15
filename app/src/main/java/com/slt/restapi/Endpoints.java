package com.slt.restapi;

import com.google.gson.JsonObject;
import com.slt.restapi.data.*;
import com.slt.restapi.data.Test;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Usman Ahmad on 20.12.2017.
 */

public interface Endpoints {

    @POST("user_functionalities/new")
    Call<JsonObject> createUser_Functionalities(@Body REST_User_Functionalities user_functionalities);


    @POST("user_functionalities/update")
    Call<JsonObject> updateUser_Functionalities(@Body REST_User_Functionalities user_functionalities);

    @POST("user_functionalities/delete")
    Call<JsonObject> deleteUser_Functionalities(@Body REST_User_Functionalities user_functionalities);

    @POST("user_functionalities/")
    Call<JsonObject> getUser_Functionalities(@Body REST_User_Functionalities user_functionalities);

    @POST("friends/")
    Call<JsonObject> getFriends(@Body REST_User_Functionalities user_functionalities);



    @POST("downloadPicture/")
    Call<JsonObject> downloadPicture(@Body Image image);

    @POST("uploadPicture/")
    Call<JsonObject> uploadPicture(@Body Image image);

    @POST("timelinesegment/update")
    Call<JsonObject> updateTimelineSegment(@Body REST_TimelineSegment timeLineSegment);

    @POST("achievement/update")
    Call<JsonObject> updateAchievement(@Body REST_Achievement achievement);

    @POST("timelinesegment/delete")
    Call<JsonObject> deleteTimelineSegment(@Body REST_TimelineSegment timeLineSegment);

    @POST("achievements/new")
    Call<JsonObject> createAchievments(@Body Singleton request);


    @POST("fetchTest")
    Call<JsonObject> getTest2(@Body Test json);

    @POST("tests")
    Call<JsonObject> getTest(@Body Test json);

    @POST("timeline/")
    Call<JsonObject> getCompleteTimeLine(@Body REST_User_Functionalities user);

    @POST("user_functionalities/")
    Call<JsonObject> getAllUsers();

    @POST("timelines/")
    Call<JsonObject> getCompleteTimeLines(@Body REST_User_Functionalities user);

    @GET("locationpoint/{timelinesegmentId}")
    Call<REST_LocationEntry> getLocationEntry(@Body String timelinesegmentId);

    @GET("timelinesegment/{timelinedayId}")
    Call<REST_TimelineSegment> getTimeLineSegment(@Body String timelinedayId);

    @GET("timelineday/{userid}")
    Call<REST_TimelineDay> getTimeLineDay(@Body String userId);

    @POST("locationentry/new")
    Call<JsonObject> createLocationEntry(@Body REST_LocationEntry locationEntry);

    @POST("locationentry/delete")
    Call<JsonObject> deleteLocationEntry(@Body REST_LocationEntry locationEntry);

    @POST("timelinesegment/new")
    Call<JsonObject> createTimeLineSegment(@Body REST_TimelineSegment timeLineSegment);

    @POST("timelineday/new")
    Call<JsonObject> createTimeLineDay(@Body REST_TimelineDay timeLineDay);

    @POST("timelineday/update")
    Call<JsonObject> updateTimelineDay(@Body REST_TimelineDay timelineDay);

    @POST("timeline/new")
    Call<JsonObject> createTimeLine(@Body REST_Timeline timeLine);

    //Maybe for merging Timeline Segments in Tracking
    @PUT("timelinesegment/{timelinesegmentId}")
    Call<String> updateTimelineSegment(@Body String timelinesegmentId);
}
