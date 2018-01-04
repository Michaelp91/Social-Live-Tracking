package com.slt.rest_trackingtimeline;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.Model;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 27.12.2017.
 */

public class UpdateOperations_Synchron {

    public static boolean createTimeLine(TimeLine timeline) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLine(timeline);
        JsonObject jsonObject = null;
        try {
            jsonObject =  call.execute().body();
        } catch (Exception e) {
            return false; //Request is not Successfull
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        Singleton.getInstance().setResponse_timeLine(test.getResponse_timeLine());
        TemporaryDB.getInstance().setTimeline(test.getResponse_timeLine());

        return true;
    }

    public static boolean createTimeLineDay(TimeLineDay timeLineDay)  {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineDay(timeLineDay);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            return false;
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        TimeLineDay response = test.getResponse_timelineDay();
        response.int_TAG = timeLineDay.int_TAG;
        Singleton.getInstance().setResponse_timelineDay(response);
        TemporaryDB.getInstance().addTimelineDay(response);

        return true;
    }

    public static boolean createTimeLineSegment(TimeLineSegment timelineSegment) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineSegment(timelineSegment);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            return false;
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);

        TimeLineSegment response = test.getResponse_timelineSegment();
        response.int_TAG = timelineSegment.int_TAG;
        Singleton.getInstance().setResponse_timelineSegment(test.getResponse_timelineSegment());
        TemporaryDB.getInstance().addTimeLineSegment(response);

        return true;
    }

    public static boolean createLocationEntry(LocationEntry locationEntry) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createLocationEntry(locationEntry);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            return false;
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        LocationEntry response = test.getResponse_locationEntry();
        response.int_TAG = locationEntry.int_TAG;

        Singleton.getInstance().setResponse_locationEntry(test.getResponse_locationEntry());
        TemporaryDB.getInstance().addLocationEntry(response);

        return true;
    }
}
