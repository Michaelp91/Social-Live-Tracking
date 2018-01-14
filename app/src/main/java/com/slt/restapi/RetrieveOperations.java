package com.slt.restapi;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.TimelineActivity;
import com.slt.restapi.data.*;
import com.slt.restapi.data.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class RetrieveOperations {
    public TimelineActivity context;
    private static final RetrieveOperations ourInstance = new RetrieveOperations();
    public static RetrieveOperations getInstance() {
        return ourInstance;
    }

    public void getCompleteTimeline(REST_User user) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getCompleteTimeLine(user);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                boolean requestSuccessfull = true;
                Singleton test = null;
                //Type listType = new TypeToken<List<Test>>() {}.getType();
                try {
                    test = new Gson().fromJson(json, Singleton.class);
                }catch(Exception e) {
                    requestSuccessfull = false;
                }





                if(requestSuccessfull) {
                    FullTimeLine responses = test.getResponses();

                    TemporaryDB.getInstance().setLocationEntries(responses.locationEntries);
                    TemporaryDB.getInstance().setTimelineDays(responses.timelinedays);
                    TemporaryDB.getInstance().setTimeline(responses.timeline);
                    TemporaryDB.getInstance().setTimeLineSegments(responses.timelinesegments);

                    if (context != null) {
                        context.updateTimelineDays();
                    } else
                        Log.e("ERROR", "Application Context is null.");
                    boolean debug = true;
                }

                context.handler.postDelayed(context.runnable, 2000);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                boolean debug = true;
                context.handler.postDelayed(context.runnable, 2000);
            }
        });
    }

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

    public REST_TimelineDay getTimeLineDay(String userId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<REST_TimelineDay> call = api.getTimeLineDay(userId);
        REST_TimelineDay newTimeLineDay = call.execute().body();

        return newTimeLineDay;
    }

    public REST_TimelineSegment getTimelineSegment(String timelineDayId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<REST_TimelineSegment> call = api.getTimeLineSegment(timelineDayId);
        REST_TimelineSegment newTimeLineSegment = call.execute().body();

        return newTimeLineSegment;
    }

    public REST_LocationEntry getLocationEntry(String timelineSegmentId) throws IOException {

        Endpoints api = RetroClient.getApiService();
        Call<REST_LocationEntry> call = api.getLocationEntry(timelineSegmentId);
        REST_LocationEntry newLocationEntry = call.execute().body();

        return newLocationEntry;
    }
}
