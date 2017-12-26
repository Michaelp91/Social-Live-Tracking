package com.slt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.slt.data.User;
import com.slt.rest_trackingtimeline.RetrieveOperations;
import com.slt.rest_trackingtimeline.Singleton;
import com.slt.rest_trackingtimeline.UpdateOperations;
import com.slt.rest_trackingtimeline.data.Achievement;
import com.slt.rest_trackingtimeline.data.Location;
import com.slt.rest_trackingtimeline.data.LocationEntry;
import com.slt.rest_trackingtimeline.data.Test;
import com.slt.rest_trackingtimeline.data.TimeLine;
import com.slt.rest_trackingtimeline.data.TimeLineDay;
import com.slt.rest_trackingtimeline.data.TimeLineSegment;
import com.slt.timelineres.ExpandableListAdapter_Timeline;
import com.slt.timelineres.Node;
import com.slt.timelineres.Route;
import com.slt.timelineres.TimelineHeader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.slt.rest_trackingtimeline.UpdateOperations.createTimeLine;

public class TimelineActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    ExpandableListAdapter_Timeline listAdapter;
    ExpandableListView expListView;
    List<TimelineHeader> listDataHeader;
    HashMap<TimelineHeader, List<Route>> listDataChild;
    private static final LatLng DARMSTADT_NORD = new LatLng(50.0042304, 9.0658932);
    private static final LatLng WILLYBRANDTPLATZ = new LatLng(49.9806625, 9.1355554);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter_Timeline(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(this);


            Test t = new Test("Hello World");
            Achievement newAchievement = new Achievement(0);
            ArrayList<Achievement> myAchievements = new ArrayList<>();
            myAchievements.add(newAchievement);
            TimeLine obj = new TimeLine("5a196bf8d17b7926882f5413", myAchievements);
            String json = new Gson().toJson(obj);

            //UpdateOperations.createTimeLine(obj);
            //TimeLine timeline = Singleton.getInstance().getResponse_timeLine();
            //TimeLineDay timelineday = new TimeLineDay(new Date(), myAchievements, "5a429f34ef946d3b30d71358");
            //json = new Gson().toJson(timelineday);
            //UpdateOperations.createTimeLineDay(timelineday);
            //RetrieveOperations.getTestData2(t);
          //  TimeLineSegment timeLineSegment = new TimeLineSegment
          //          ("Teststr. 84, 6442 Testhausen", "3", 2.4 ,
          //                  2.0, "5a42b1e52026a72bd8632eb0", myAchievements);

          //UpdateOperations.createTimeLineSegment(timeLineSegment);

        Location location = new Location(2.3, 4.2);
        LocationEntry locationentry = new LocationEntry(new Date(), 2.1, 2.0,
                location, "5a42c40b6deb772de0c9f67e");

        UpdateOperations.createLocationEntry(locationentry);

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<TimelineHeader>();
        listDataChild = new HashMap<TimelineHeader, List<Route>>();

        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(1);
        tmp.add(2);

        TimelineHeader th = new TimelineHeader("Mo, 06.11.2017", tmp);

        Node start = new Node(1, 1.f , 3.f, "Frohsinnstraße 8", "63739 Aschaffenburg");
        Node end = new Node(2, 2.f , 3.f, "City Gallerie", "Goldbacherstr. 8, 63739 Aschaffenburg");
        Route r1 = new Route(1, start, end, 2.3f, 30.4f, th);
        Route r2 = new Route(2, end, start, 2.3f, 30.4f, th);

        r1.setNextRoute(r2);
        r1.setPreviousRoute(null);
        r2.setNextRoute(null);
        r2.setPreviousRoute(r1);

        // Adding child data
        listDataHeader.add(th);

        // Adding child data
        List<Route> content = new ArrayList<Route>();

        //TODO: Alle Routes durch nach der routeid aus dem TMP filtern

        content.add(r1);
        content.add(r2);

        listDataChild.put(listDataHeader.get(0), content); // Header, Child data
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

        Intent intent = new Intent(this, TimelineDetailsActivity.class);

        startActivity(intent);

        /*
        TimelineHeader key = listDataHeader.get(groupPosition);

        List<Route> contents = listDataChild.get(key);
        Node c = contents.get(childPosition);



        Intent i = new Intent(this, Activity_TimelineDetails.class);
        startActivity(i);
        */

        return false;
    }
}
