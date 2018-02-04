package com.slt;

import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.LocationEntry;
import com.slt.data.TimelineSegment;
import com.slt.restapi.data.Constants;

import java.util.LinkedList;

//TODO: Test Timeline Details
public class TimelineDetailsActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    MapView mMapView;

    private LinkedList<LocationEntry> locationEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelinedetails);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimelineSegment segment = SharedResources.getInstance().getOnClickedTimelineSegmentForDetails();

        locationEntries = segment.getLocationPoints();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if(locationEntries.size() >= 2) {
                    LatLng start = new LatLng(locationEntries.get(0).getLatitude(), locationEntries.get(0).getLongitude());
                    LatLng end = new LatLng(locationEntries.get(locationEntries.size() - 1).getLatitude(), locationEntries.get(locationEntries.size() - 1).getLongitude());


                    googleMap.addMarker(new MarkerOptions().position(start).title(""));
                    googleMap.addMarker(new MarkerOptions().position(end).title(""));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    addLines(start, end);
                } else {
                    Toast.makeText(getApplicationContext(), "Only one or no Locationpoints available!", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tvAktivitaet = (TextView) findViewById(R.id.tv_aktivitaet);
        TextView tvDauer = (TextView) findViewById(R.id.tv_dauer);
        TextView tvStrecke = (TextView) findViewById(R.id.tv_strecke);

        tvDauer.setText(segment.getDuration() + "min. ");
        String activity = "";
        if(segment.getMyActivity() != null) {
            switch (segment.getMyActivity().getType()) {
                case com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING:
                    activity = "Running";
                    break;

                case com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING:
                    activity = "Walking";
                    break;

                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT:
                    activity = "On Foot";
                    break;

                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_BICYCLE:
                    activity = "On Bicycle";
                    break;

                default:
                    activity = "Unknown";
                    break;
            }
        }

        tvAktivitaet.setText(activity);
        tvStrecke.setText(Double.toString(segment.getActiveDistance()) + "km" );
    }

    private void addLines(LatLng start, LatLng end) {
        googleMap.addPolyline((new PolylineOptions())
                .add(start, end).width(5).color(Color.BLUE)
                .geodesic(true));
        // move camera to zoom on map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,
                10));


        //Src: https://stackoverflow.com/questions/34357660/calculating-the-distance-between-two-markers-in-android
        Location markerLocation = new Location("");
        markerLocation.setLatitude(start.latitude);
        markerLocation.setLongitude(start.longitude);

        Location distanceLocation = new Location("");
        distanceLocation.setLatitude(end.latitude);
        distanceLocation.setLongitude(end.longitude);


    }

/*
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    */
}
