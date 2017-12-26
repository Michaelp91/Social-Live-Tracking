package com.slt.rest_trackingtimeline;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.Test;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class RetrieveOperations {

    public static void getTestData2(Test test) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getTest2(test);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                //Type listType = new TypeToken<List<Test>>() {}.getType();
                Singleton test = new Gson().fromJson(json, Singleton.class);
                boolean debug = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                boolean debug = true;
            }
        });
    }

    public static void getTestData(Test test) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getTest(test);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                Test t = new Gson().fromJson(json, Test.class);
                boolean debug = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                boolean debug = true;
            }
        });
    }

    public TimeLineDay getTimeLineDay(String userId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<TimeLineDay> call = api.getTimeLineDay(userId);
        TimeLineDay newTimeLineDay = call.execute().body();

        return newTimeLineDay;
    }

    public TimeLineSegment getTimelineSegment(String timelineDayId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<TimeLineSegment> call = api.getTimeLineSegment(timelineDayId);
        TimeLineSegment newTimeLineSegment = call.execute().body();

        return newTimeLineSegment;
    }

    public LocationEntry getLocationEntry(String timelineSegmentId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<LocationEntry> call = api.getLocationEntry(timelineSegmentId);
        LocationEntry newLocationEntry = call.execute().body();

        return newLocationEntry;
    }
}
