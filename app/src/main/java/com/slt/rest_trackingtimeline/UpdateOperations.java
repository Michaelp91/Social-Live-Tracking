package com.slt.rest_trackingtimeline;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class UpdateOperations {

    public static void createTimeLine(TimeLine timeline) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLine(timeline);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Singleton test = null;
                    try {
                        test = new Gson().fromJson(response.body().toString(), Singleton.class);
                    }catch(Exception e) {
                        boolean debug = true;
                    }
                    Singleton.getInstance().setResponse_timeLine(test.getResponse_timeLine());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void createTimeLineDay(TimeLineDay timeLineDay) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineDay(timeLineDay);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Singleton obj = null;
                    obj = new Gson().fromJson(response.body().toString(), Singleton.class);
                    Singleton.getInstance().setResponse_timelineDay(obj.getResponse_timelineDay());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void createTimeLineSegment(TimeLineSegment timelineSegment) {


        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineSegment(timelineSegment);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Singleton obj = null;
                    obj = new Gson().fromJson(response.body().toString(), Singleton.class);
                    Singleton.getInstance().setResponse_timelineSegment(obj.getResponse_timelineSegment());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void createLocationEntry(LocationEntry locationEntry) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createLocationEntry(locationEntry);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Singleton obj = null;
                    obj = new Gson().fromJson(response.body().toString(), Singleton.class);
                    Singleton.getInstance().setResponse_locationEntry(obj.getResponse_locationEntry());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}
