package com.slt.restapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.data.TimelineSegment;
import com.slt.fragments.LoginFragment;
import com.slt.restapi.data.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class UpdateOperations extends Thread {
    private static Object lock = new Object();

    public static boolean updateTimelineSegmentManually(TimelineSegment t_s) {
        REST_TimelineSegment r_t_s = TemporaryDB.getInstance().h_timelineSegments.get(t_s);
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.updateTimelineSegment(r_t_s);
        JsonObject jsonObject = null;

        try{
            jsonObject = call.execute().body();
        } catch(Exception e) {
            return false;
        }

        TemporaryDB.getInstance().h_timelineSegments.put(t_s, r_t_s);

        return true;
    }

    public static void createUser_Functionalities(REST_User_Functionalities rest_user_functionalities, LoginFragment loginFragment) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createUser_Functionalities(rest_user_functionalities);

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
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public static void retrieveUser_Functionalities(REST_User_Functionalities rest_user_functionalities, LoginFragment loginFragment) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getUser_Functionalities(rest_user_functionalities);

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
                    TemporaryDB.getInstance().setAppUser(test.getResponse_user_functionalities());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void createTimeLine(REST_Timeline timeline) {

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

    public static void createTimeLineDay(REST_TimelineDay timeLineDay) {
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

    public static void createTimeLineSegment(REST_TimelineSegment timelineSegment) {


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

    public static void createLocationEntry(REST_LocationEntry locationEntry) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.createLocationEntry(locationEntry);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Singleton obj = null;
                        obj = new Gson().fromJson(response.body().toString(), Singleton.class);
                        Singleton.getInstance().setResponse_locationEntry(obj.getResponse_locationEntry());
                        notify();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


    }


}
