package com.slt.restapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.restapi.data.*;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * create and update requests
 */

public class UpdateOperations_Synchron {


    /**
     * create request: add timeline into the database
     * @param timeline
     * @return true if successfull, otherwise false
     */
    public static boolean createTimeLine(REST_Timeline timeline) {
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

    /**
     * create timeline segment manually
     * @param t
     * @return true if successfull, otherwise false
     */
    public static boolean createTimeLineManually(Timeline t) {
        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        REST_Timeline r_t = new REST_Timeline(r_u_f._id, new ArrayList<REST_Achievement>());
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLine(r_t);
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

    /**
     * create timeline day manually
     * @param t_d
     * @param timeLineDay
     * @return true if successfull, otherwise false
     */
    public static boolean createTimeLineDay(TimelineDay t_d, REST_TimelineDay timeLineDay)  {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineDay(timeLineDay);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            return false;
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        REST_TimelineDay response = test.getResponse_timelineDay();
        response.int_TAG = timeLineDay.int_TAG;

        Singleton.getInstance().setResponse_timelineDay(response);
        TemporaryDB.getInstance().addTimelineDay(response);
        TemporaryDB.getInstance().h_timelineDays.put(t_d, response);

        return true;
    }

    /**
     * update timeline day
     * @param t_d
     * @param timelineDay
     * @return true if successfull, otherwise not
     */
    public static boolean updateTimelineDay(TimelineDay t_d, REST_TimelineDay timelineDay) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.updateTimelineDay(timelineDay);
        JsonObject jsonObject = null;

        try{
            jsonObject = call.execute().body();
        } catch(Exception e) {
            return false;
        }

        TemporaryDB.getInstance().h_timelineDays.put(t_d, timelineDay);

        return true;
    }

    /**
     * update timelinesegment manually
     * @param t_s
     * @return true if successful, otherwise false
     */
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

    /**
     * update timeline segment
     * @param t_s
     * @param timelineSegment
     * @return true if successfull, otherwise false
     */
    public static boolean updateTimelineSegment(TimelineSegment t_s, REST_TimelineSegment timelineSegment) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.updateTimelineSegment(timelineSegment);
        JsonObject jsonObject = null;

        try{
            jsonObject = call.execute().body();
        } catch(Exception e) {
            return false;
        }

        TemporaryDB.getInstance().h_timelineSegments.put(t_s, timelineSegment);

        return true;
    }

    /**
     * delete timeline segment
     * @param t_s
     * @param timelineSegment
     * @return true if successful, otherwise false
     */
    public static boolean deleteTimelineSegment(TimelineSegment t_s, REST_TimelineSegment timelineSegment) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.deleteTimelineSegment(timelineSegment);
        JsonObject jsonObject = null;

        try{
            jsonObject = call.execute().body();
        } catch(Exception e) {
            return false;
        }

        String timeLineDay = timelineSegment.timeLineDay;
        //TemporaryDB.getInstance().removeTimelineSegmentsByTDayId(timeLineDay);
        TemporaryDB.getInstance().h_timelineSegments.remove(t_s);

        return true;
    }

    /**
     * create timeline segment
     * @param t_s
     * @param timelineSegment
     * @return true if successful, otherwise false
     */
    public static boolean createTimeLineSegment(TimelineSegment t_s, REST_TimelineSegment timelineSegment) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createTimeLineSegment(timelineSegment);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            return false;
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);

        REST_TimelineSegment response = test.getResponse_timelineSegment();
        response.int_TAG = timelineSegment.int_TAG;
        Singleton.getInstance().setResponse_timelineSegment(test.getResponse_timelineSegment());
        //TemporaryDB.getInstance().addTimeLineSegment(response);
        TemporaryDB.getInstance().addTimeLineSegment(response);
        TemporaryDB.getInstance().h_timelineSegments.put(t_s, response);

        return true;
    }

    /**
     * create location entry
     * @param l_e
     * @param locationEntry
     * @return true if successful, otherwise false
     */
    public static boolean createLocationEntry(LocationEntry l_e, REST_LocationEntry locationEntry) {

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createLocationEntry(locationEntry);
        JsonObject jsonObject = null;
        try {
            jsonObject = call.execute().body();
        } catch (Exception e) {
            return false;
        }

        Singleton test = new Gson().fromJson(jsonObject.toString(), Singleton.class);
        REST_LocationEntry response = test.getResponse_locationEntry();
        response.int_TAG = locationEntry.int_TAG;

        Singleton.getInstance().setResponse_locationEntry(test.getResponse_locationEntry());
        //TemporaryDB.getInstance().addLocationEntry(response);
        TemporaryDB.getInstance().h_locationEntries.put(l_e, response);

        return true;
    }

    /**
     * delete location entry
     * @param l_e
     * @param r_l_e
     * @return true if successful, otherwise false
     */
    public static boolean deleteLocationEntry(LocationEntry l_e, REST_LocationEntry r_l_e) {
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.deleteLocationEntry(r_l_e);
        JsonObject jsonObject = null;

        try{
            jsonObject = call.execute().body();
        } catch(Exception e) {
            return false;
        }

        String timelineSegment = r_l_e.timelinesegment;
        //TemporaryDB.getInstance().removeLocationEntriesByTSegmentId(timelineSegment);
        TemporaryDB.getInstance().h_locationEntries.remove(l_e);

        return true;
    }
}
