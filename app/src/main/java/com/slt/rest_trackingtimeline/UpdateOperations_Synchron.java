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

    public static void createTimeLine(TimeLine timeline) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLine(timeline);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        Singleton.getInstance().setResponse_timeLine(test.getResponse_timeLine());
        TemporaryDB.getInstance().setTimeline(test.getResponse_timeLine());
    }

    public static void createTimeLineDay(TimeLineDay timeLineDay)  {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineDay(timeLineDay);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        TimeLineDay response = test.getResponse_timelineDay();
        response.TAG = timeLineDay.TAG;
        Singleton.getInstance().setResponse_timelineDay(response);
        TemporaryDB.getInstance().addTimelineDay(response);
    }

    public static void createTimeLineSegment(TimeLineSegment timelineSegment) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineSegment(timelineSegment);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);


        TimeLineSegment response = test.getResponse_timelineSegment();
        response.TAG = timelineSegment.TAG;

        Singleton.getInstance().setResponse_timelineSegment(test.getResponse_timelineSegment());
        TemporaryDB.getInstance().addTimeLineSegment(response);
    }

    public static void createLocationEntry(LocationEntry locationEntry) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createLocationEntry(locationEntry);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            String test = "";
            e.printStackTrace();
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        LocationEntry response = test.getResponse_locationEntry();
        response.TAG = locationEntry.TAG;

        Singleton.getInstance().setResponse_locationEntry(test.getResponse_locationEntry());
        TemporaryDB.getInstance().addLocationEntry(response);

    }
}
