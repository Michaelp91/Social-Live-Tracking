package com.slt;

import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
import com.slt.data.TimelineSegment;
import com.slt.restapi.data.Constants;


public class TimelineDetailsActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    MapView mMapView;

    private static final LatLng DARMSTADT_NORD = new LatLng(50.0042304, 9.0658932);
    private static final LatLng WILLYBRANDTPLATZ = new LatLng(49.9806625, 9.1355554);

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

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.addMarker(new MarkerOptions().position(DARMSTADT_NORD).title("Times Square"));
                googleMap.addMarker(new MarkerOptions().position(WILLYBRANDTPLATZ).title("Brooklyn Bridge"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(DARMSTADT_NORD));
                addLines();
            }
        });


        TimelineSegment segment = DataProvider.getInstance().getOnClickedTimelineSegmentForDetails();

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

                default:
                    activity = "Unknown";
                    break;
            }
        }

        tvAktivitaet.setText(activity);
        tvStrecke.setText(Double.toString(segment.getActiveDistance()) + "km" );
    }

    private void addLines() {

        googleMap.addPolyline((new PolylineOptions())
                .add(DARMSTADT_NORD, WILLYBRANDTPLATZ).width(5).color(Color.BLUE)
                .geodesic(true));
        // move camera to zoom on map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DARMSTADT_NORD,
                10));


        //Src: https://stackoverflow.com/questions/34357660/calculating-the-distance-between-two-markers-in-android
        Location markerLocation = new Location("");
        markerLocation.setLatitude(DARMSTADT_NORD.latitude);
        markerLocation.setLongitude(DARMSTADT_NORD.longitude);

        Location distanceLocation = new Location("");
        distanceLocation.setLatitude(WILLYBRANDTPLATZ.latitude);
        distanceLocation.setLongitude(WILLYBRANDTPLATZ.longitude);


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
