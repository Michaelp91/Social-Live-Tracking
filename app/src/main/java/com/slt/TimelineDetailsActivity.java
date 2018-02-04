package com.slt;

import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
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
                LocationEntry last = null;
                if (locationEntries.size() >= 2) {

                    for (LocationEntry entry : locationEntries) {
                        if(last != null){

                            LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                            LatLng end = new LatLng(last.getLatitude(), last.getLongitude());


                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(end));
                            addLines(start, end);
                        } else {
                            LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(start).title(""));
                        }
                        last = entry;
                    }

                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(end).title(""));

                } else {
                    LatLng start = new LatLng(locationEntries.get(0).getLatitude(), locationEntries.get(0).getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(start).title(""));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    Toast.makeText(getApplicationContext(), "Only one or no Locationpoints available!", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tvAktivitaet = (TextView) findViewById(R.id.tv_aktivitaet);
        TextView tvDauer = (TextView) findViewById(R.id.tv_dauer);
        TextView tvStrecke = (TextView) findViewById(R.id.tv_strecke);
        TextView tvPlace = (TextView) findViewById(R.id.tv_place);
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        TextView tvSteps = (TextView) findViewById(R.id.tv_steps);
        TextView tvStepsTitle = (TextView) findViewById(R.id.tv_steps_title);

        String duration = (segment.getActiveTime()/ (60*1000)) + "min / " +  (segment.getDuration()/ (60*1000)) + "min. ";

        tvDauer.setText(duration);
        tvPlace.setText(segment.getPlace());
        tvAddress.setText(segment.getAddress());

        String activity = "";
        if (segment.getMyActivity() != null) {
            switch (segment.getMyActivity().getType()) {
                case DetectedActivity.RUNNING:
                    activity = "Running";
                    tvSteps.setText(segment.getUserSteps() + " Steps");
                    break;

                case DetectedActivity.IN_VEHICLE:
                    activity = "In Vehicle";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
                case DetectedActivity.ON_BICYCLE:
                    activity = "On Bicycle";
                    break;
                case DetectedActivity.ON_FOOT:
                    activity = "On Foot";
                    tvSteps.setText(segment.getUserSteps() + " Steps");
                    break;

                case DetectedActivity.STILL:
                    activity = "Still";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
                case DetectedActivity.TILTING:
                    activity = "Tilting";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
                case DetectedActivity.UNKNOWN:
                    activity = "Unknown";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;


                case DetectedActivity.WALKING:
                    activity = "Walking";
                    tvSteps.setText(segment.getUserSteps() + " Steps");
                    break;

                default:
                    activity = "Undefined";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
            }
        }

        tvAktivitaet.setText(activity);

        String dist = String.format( "%.2f", segment.getActiveDistance()) + "m"
                + " / " + String.format( "%.2f", (segment.getActiveDistance()  +
                segment.getInactiveDistance()))    + "m";


        tvStrecke.setText( dist);






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
