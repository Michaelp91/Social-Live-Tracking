package com.slt.rest_trackingtimeline;

import com.google.gson.Gson;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 19.12.2017.
 */

public class RetrieveOperations {
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

        return new LocationEntry();
    }
}
