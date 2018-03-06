package com.slt.restapi;

import com.google.gson.JsonObject;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.restapi.data.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Usman Ahmad on 20.12.2017.
 * contains rest interfaces
 */

public interface Endpoints {

    /**
     * for adding new user
     * @param user_functionalities
     * @return
     */
    @POST("user_functionalities/new")
    Call<JsonObject> createUser_Functionalities(@Body REST_User_Functionalities user_functionalities);


    /**
     * for updating user
     * @param user_functionalities
     * @return
     */
    @POST("user_functionalities/update")
    Call<JsonObject> updateUser_Functionalities(@Body REST_User_Functionalities user_functionalities);


    /**
     * for getting all the users
     * @param user_functionalities
     * @return
     */
    @POST("user_functionalities/")
    Call<JsonObject> getUser_Functionalities(@Body REST_User_Functionalities user_functionalities);

    /**
     * for getting all friends
     * @param user_functionalities
     * @return
     */
    @POST("friends/")
    Call<JsonObject> getFriends(@Body REST_User_Functionalities user_functionalities);


    /**
     * for downloading picture
     * @param image
     * @return
     */
    @POST("downloadPicture/")
    Call<JsonObject> downloadPicture(@Body Image image);


    /**
     * for uploading picture
     * @param image
     * @return
     */
    @POST("uploadPicture/")
    Call<JsonObject> uploadPicture(@Body Image image);

    /**
     * for updating timeline segment
     * @param timeLineSegment
     * @return
     */
    @POST("timelinesegment/update")
    Call<JsonObject> updateTimelineSegment(@Body REST_TimelineSegment timeLineSegment);

    /**
     * for deleting timeline segment
     * @param timeLineSegment
     * @return
     */
    @POST("timelinesegment/delete")
    Call<JsonObject> deleteTimelineSegment(@Body REST_TimelineSegment timeLineSegment);

    /**
     * for getting complete timeline
     * @param user
     * @return
     */
    @POST("timeline/")
    Call<JsonObject> getCompleteTimeLine(@Body REST_User_Functionalities user);

    /**
     * for getting all the users
     * @param user
     * @return
     */
    @POST("allusers/")
    Call<JsonObject> getAllUsers(@Body REST_User_Functionalities user);

    /**
     * for getting complete timelines of friends
     * @param user
     * @return
     */
    @POST("timelines/")
    Call<JsonObject> getCompleteTimeLines(@Body REST_User_Functionalities user);

    /**
     * for creating location entry
     * @param locationEntry
     * @return
     */
    @POST("locationentry/new")
    Call<JsonObject> createLocationEntry(@Body REST_LocationEntry locationEntry);

    /**
     * for deleting the location entry
     * @param locationEntry
     * @return
     */
    @POST("locationentry/delete")
    Call<JsonObject> deleteLocationEntry(@Body REST_LocationEntry locationEntry);

    /**
     * for creating new timeline segment
     * @param timeLineSegment
     * @return
     */
    @POST("timelinesegment/new")
    Call<JsonObject> createTimeLineSegment(@Body REST_TimelineSegment timeLineSegment);

    /**
     * for creating new timeline day
     * @param timeLineDay
     * @return
     */
    @POST("timelineday/new")
    Call<JsonObject> createTimeLineDay(@Body REST_TimelineDay timeLineDay);

    /**
     * for updating timeline day
     * @param timelineDay
     * @return
     */
    @POST("timelineday/update")
    Call<JsonObject> updateTimelineDay(@Body REST_TimelineDay timelineDay);

    /**
     * for creating new timeline
     * @param timeLine
     * @return
     */
    @POST("timeline/new")
    Call<JsonObject> createTimeLine(@Body REST_Timeline timeLine);

    /**
     * for deleting all locationentry from specific timeline segment
     * @param timelineSegment
     * @return
     */

    @POST("deleteLocationEntriesByTimelineSegment")
    Call<JsonObject> deleteLocationEntriesByTimelineSegment(@Body REST_TimelineSegment timelineSegment);


    /**
     * for deleting all the timeline segments from specific timeline day
     * @param timelineDay
     * @return
     */
    @POST("deleteTimelineSegmentByTimelineDay")
    Call<JsonObject> deleteTimelineSegmentByTimelineDay(@Body REST_TimelineDay timelineDay);


    /**
     * delete all timeline days from specific timeline
     * @param timeline
     * @return
     */
    @POST("deleteTimelineDayByTimeline")
    Call<JsonObject> deleteTimelineDayByTimeline(@Body REST_Timeline timeline);


    /**
     * for deleting the timeline
     * @param user_functionalities
     * @return
     */
    @POST("timeline/delete")
    Call<JsonObject> deleteTimeline(@Body REST_User_Functionalities user_functionalities);

    /**
     * for deleting the user functionalities
     * @param user_functionalities
     * @return
     */
    @POST("user_functionalities/delete")
    Call<JsonObject> deleteUser_Functionalities(@Body REST_User_Functionalities user_functionalities);


    /**
     * for deleting user
     * @param user
     * @return
     */
    @POST("user/delete")
    Call<JsonObject> deleteUser(@Body REST_User_Functionalities user);
}
