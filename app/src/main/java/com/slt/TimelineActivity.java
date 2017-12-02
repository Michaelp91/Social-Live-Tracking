package com.slt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.slt.data.User;
import com.slt.timelineres.ExpandableListAdapter_Timeline;
import com.slt.timelineres.Node;
import com.slt.timelineres.Route;
import com.slt.timelineres.TimelineHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        GsonTest();
    }

    //Test: What If I serialize Location Object?
    private void GsonTest() {
        Gson gson = new Gson();
        User user = new User();

        String json = gson.toJson(user);
        String debug = "true";
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

        Singleton.getInstance().setTimelinecontent(c);

        Intent i = new Intent(this, Activity_TimelineDetails.class);
        startActivity(i);
        */

        return false;
    }
}
