package com.slt.rest_trackingtimeline;

import com.google.gson.Gson;
import com.slt.rest_trackingtimeline.data.TimeLineDay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class UpdateOperations {

    public void createTimeLineDay(TimeLineDay timeLineDay) {
        String userJSON = new Gson().toJson(timeLineDay);


        Endpoints api = RetroClient.getApiService();
        String jsonBody = new Gson().toJson(timeLineDay);
        Call<String> call = api.createTimeLineDay(jsonBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void createTimeLineSegment(String timelineSegment) {


        Endpoints api = RetroClient.getApiService();
        String jsonBody = new Gson().toJson(timelineSegment);
        Call<String> call = api.createTimeLineDay(jsonBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void createLocationEntry(String locationEntry) {
        Endpoints api = RetroClient.getApiService();
        String jsonBody = new Gson().toJson(locationEntry);
        Call<String> call = api.createTimeLineDay(jsonBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
