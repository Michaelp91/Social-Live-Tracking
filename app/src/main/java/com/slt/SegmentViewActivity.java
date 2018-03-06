package com.slt;

import android.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.definitions.Constants;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.UsefulMethods;
import com.slt.utils.FunctionalityLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * SegmentViewActivity for viewing the Segments of The Timeline Day
 */
public class SegmentViewActivity extends AppCompatActivity {

    /**
     * Timeline Segments from the clicked Timeline Day
     */
    private LinkedList<TimelineSegment> choosedTimelineSegments;

    /**
     * TAG "FragmentTimeline"
     */
    private static final String TAG = "FragmentTimeline";



    private LinearLayout choosedChildren;


    /**
     * Context
     */
    private Activity context;

    /**
     * firstLocationEntry Object, useful for Clustering
     */
    private LocationEntry firstLocationEntry;


    /**
     * googleMap Object for Map View
     */
    private GoogleMap googleMap;

    /**
     * mMapView Object
     */
    MapView mMapView;

    /**
     * Overwritten onCreate Method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_view);
        choosedChildren = (LinearLayout) findViewById(R.id.timeline_segments);
        this.choosedTimelineSegments = DataProvider.getInstance().getChoosedTimelineSegments();
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        boolean failed = false;

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
                LocationEntry first = null;
                LocationEntry last = null;

                for(TimelineSegment t: choosedTimelineSegments) {

                    if (t.getMyActivity().getType() != Constants.TIMELINEACTIVITY.UNKNOWN &&
                            t.getMyActivity().getType() != Constants.TIMELINEACTIVITY.STILL &&
                            t.getMyActivity().getType() != Constants.TIMELINEACTIVITY.TILTING) {
                        LinkedList<LocationEntry> locationEntries = t.getLocationPoints();

                        if (locationEntries.size() >= 2) {
                            first = locationEntries.getFirst();

                            for (LocationEntry entry : locationEntries) {
                                if (last != null) {

                                    LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());

                                    addLines(start, end);
                                } else {
                                    LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                                }
                                last = entry;
                            }


                        } else if (locationEntries.size() > 0) {
                            last = locationEntries.get(0);
                        }
                    }
                }

                if (first != null && last != null) {
                    LatLng start = new LatLng(first.getLatitude(), first.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    Marker m = googleMap.addMarker(new MarkerOptions().position(start).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)).title("START"));
                    m.showInfoWindow();

                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(end).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_finish))
                            .title("FINISH"));
                    ZoomCamera(start, end);
                }
            }
        });
        context = this;
    }


    /**
     * Method for zooming in, Google Map
     * @param start start Location
     * @param end end Location
     */

    private void ZoomCamera(final LatLng start, final LatLng end) {

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                if(end != null) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(start);
                    builder.include(end);
                    LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    googleMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                        public void onCancel(){}
                        public void onFinish(){
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -4.0);
                            googleMap.animateCamera(zout);
                        }
                    });
                }
            }
        });
    }

    /**
     * Add lines in the Map
     * @param start start Location
     * @param end end Location
     */


    private void addLines(LatLng start, LatLng end) {
        googleMap.addPolyline((new PolylineOptions())
                .add(start, end).width(5).color(Color.GRAY)
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


    /**
     * view the timeline segments of the timeline day
     */
    public void initTimelineView() {

/*
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                choosedChildren.removeAllViews();
            }
        });

        */

        try {

            final LayoutInflater inflater = LayoutInflater.from(this);

            LinkedList<TimelineSegment> timeLineSegments = choosedTimelineSegments;
            boolean isAdded = false;
            boolean unknownSegmentAdded = false;
            LocationEntry lastLocationEntry = null;

            for (final TimelineSegment tSegment : timeLineSegments) {

                    LinkedList<LocationEntry> locationEntries = tSegment.getLocationPoints();

                    RelativeLayout view_FirstPoint = null;
                    RelativeLayout view_segment = null;
                    RelativeLayout view_LastPoint = null;
                    ImageView iv_details = null;
                    final DetectedActivity detectedActivity = tSegment.getMyActivity();

                    //For Debug Purpose:
                    if (detectedActivity.getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT) {
                        boolean debug = true;
                        int test = 2;
                    }



                    if (!locationEntries.isEmpty() && detectedActivity.getType() !=
                            com.slt.definitions.Constants.TIMELINEACTIVITY.TILTING
                            && detectedActivity.getType() != com.slt.definitions.Constants.TIMELINEACTIVITY.STILL
                            && detectedActivity.getType() != Constants.TIMELINEACTIVITY.UNKNOWN) {


                        isAdded = true;


                        LocationEntry fstPoint = tobeClustered(locationEntries.get(0), lastLocationEntry, tSegment);
                        lastLocationEntry = (fstPoint == null)? lastLocationEntry: fstPoint;



                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView address = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);
                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);
                        TextView startPlace = (TextView) view_FirstPoint.findViewById(R.id.tv_startPlace);


                        if(fstPoint == null) {
                            view_FirstPoint.setVisibility(View.GONE);
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                        String strDate = (fstPoint != null)? sdf.format(fstPoint.getMyEntryDate()): "";

                        FunctionalityLogger.getInstance().AddLog("Start: " + strDate);

                        if(fstPoint != null)
                          FunctionalityLogger.getInstance().AddLog("Location Point(Longitude, Lattitude): " + fstPoint.getLongitude() + ", " + fstPoint.getLatitude());

                        String[] strAddress = tSegment.getStartAddress().split(",");

                        myEntryDate.setText(strDate + " Uhr");

                        try {
                            address.setText(strAddress[0]);
                            startPlace.setText(tSegment.getStartPlace());
                        }catch(Exception e) {
                            address.setText("");
                        }

                        if (timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint


                            view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                            final TextView activityInfo = (TextView) view_segment.findViewById(R.id.tv_activityinfo);

                            String informations = "";

                            switch (detectedActivity.getType()) {
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING:

                                    LinearLayout ll = (LinearLayout) view_segment.findViewById(R.id.ll_activity_walking);
                                    ll.setVisibility(View.VISIBLE);
                                    ImageView iv = (ImageView) view_segment.findViewById(R.id.iv_activity_walking);
                                    Bitmap bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_walking, 100, 100);
                                    iv.setImageBitmap(bmp);

                                    informations = "Type: Walking";
                                    informations += "\nDistance: " + String.format("%.2f", (float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) tSegment.getDuration()) + " min";
                                    informations += "\nUser Steps: " + tSegment.getUserSteps();

                                    break;
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING:

                                    ll = (LinearLayout) view_segment.findViewById(R.id.ll_activity_running);
                                    ll.setVisibility(View.VISIBLE);
                                    iv = (ImageView) view_segment.findViewById(R.id.iv_activity_running);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_running, 100, 100);
                                    iv.setImageBitmap(bmp);

                                    informations = "Type: Running";
                                    informations += "\nDistance: " + String.format("%.2f", (float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) tSegment.getDuration()) + " min";

                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.IN_VEHICLE:

                                    ll = (LinearLayout) view_segment.findViewById(R.id.ll_activity_vehicle);
                                    ll.setVisibility(View.VISIBLE);
                                    iv = (ImageView) view_segment.findViewById(R.id.iv_activity_vehicle);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_vehicle, 100, 100);
                                    iv.setImageBitmap(bmp);

                                    informations = "Type: Vehicle";
                                    informations += "\nDistance: " + String.format("%.2f", (float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) tSegment.getDuration()) + " min";
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT:

                                    ll = (LinearLayout) view_segment.findViewById(R.id.ll_activity_onfoot);
                                    ll.setVisibility(View.VISIBLE);
                                    iv = (ImageView) view_segment.findViewById(R.id.iv_activity_onfoot);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_walking, 100, 100);
                                    iv.setImageBitmap(bmp);

                                    informations = "Type: On Foot";
                                    informations += "\nDistance: " + String.format("%.2f", (float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) tSegment.getDuration()) + " min";
                                    informations += "\nUser Steps: " + tSegment.getUserSteps();

                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_BICYCLE:

                                    ll = (LinearLayout) view_segment.findViewById(R.id.ll_activity_bicycle);
                                    ll.setVisibility(View.VISIBLE);

                                    ImageView iv_bicycle = (ImageView) view_segment.findViewById(R.id.iv_activity_bicycle);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_biking, 100, 100);
                                    iv_bicycle.setImageBitmap(bmp);

                                    informations = "Type: Bicycle";
                                    informations += "\nDistance: " + String.format("%.2f", (float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) tSegment.getDuration()) + " min";
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN:
                                    unknownSegmentAdded = true;
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.STILL:
                                    break;

                                default:
                                    break;
                            }

                            activityInfo.setText(informations);
                        }

                    } else if (timeLineSegments.indexOf(tSegment) == timeLineSegments.size() - 1 && !locationEntries.isEmpty()
                            && (tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.STILL
                            || tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN)) {
                        LocationEntry fstPoint = locationEntries.get(0);

                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);

                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String strDate = sdf.format(fstPoint.getMyEntryDate());


                        myEntryDate.setText(strDate);

                        TextView address = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);
                        TextView startPlace = (TextView) view_FirstPoint.findViewById(R.id.tv_startPlace);

                        myEntryDate.setText(strDate);
                        startPlace.setText(tSegment.getStartAddress());
                        String[] strAddress = tSegment.getStartAddress().split(",");

                        try {
                            address.setText(strAddress[0]);
                            startPlace.setText(tSegment.getStartPlace());
                        }catch(Exception e) {
                            address.setText("");
                            startPlace.setText("");
                        }


                    } else if (timeLineSegments.indexOf(tSegment) == timeLineSegments.size() - 1
                            && (tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.STILL
                            || tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN)) {

                        Location l = new Location("");
                        l.setLatitude(0);
                        l.setLongitude(0);
                        LocationEntry fstPoint = (locationEntries.size() > 0)? locationEntries.get(0): new LocationEntry(l, null, null, null);

                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);


                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String strDate = (fstPoint.getMyEntryDate() != null)? sdf.format(fstPoint.getMyEntryDate()): "Unknown";
                        String[] strAddress = tSegment.getStartAddress().split(",");

                        TextView address = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);
                        TextView startPlace = (TextView) view_FirstPoint.findViewById(R.id.tv_startPlace);

                        myEntryDate.setText(strDate);
                        startPlace.setText(tSegment.getStartAddress());


                        try {
                            address.setText(strAddress[0]);
                            startPlace.setText(tSegment.getStartPlace());
                        }catch(Exception e) {
                            address.setText("");
                            startPlace.setText("");
                        }
                    }


                    final RelativeLayout finalView_FirstPoint = view_FirstPoint;
                    final RelativeLayout finalView_segment = view_segment;
                    final ImageView finalIv_details = iv_details;

                    if (finalView_FirstPoint != null) {
                        choosedChildren.addView(finalView_FirstPoint);
                    }


                    if (finalView_segment != null) {
                        view_segment.setTag(tSegment);
                        view_segment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimelineSegment tSegment = (TimelineSegment) view.getTag();
                                SharedResources.getInstance().setOnClickedTimelineSegmentForDetails(tSegment);
                                Intent intent = new Intent(context, TimelineDetailsActivity.class);
                                startActivity(intent);
                            }
                        });

                        choosedChildren.addView(finalView_segment);
                    }


                }




            if (isAdded) {
                LinearLayout whiteSpace = (LinearLayout) inflater.inflate(R.layout.timeline_whitespace, null);
                choosedChildren.addView(whiteSpace);
            }


        }catch(Exception e) {
            FunctionalityLogger.getInstance().AddErrorLog("UpdateTimelineView(): " + e.getMessage().toString());
        }

    }

    /**
     * this Method is invoked for Clustering the Location Points which are near to the start Location.
     * Compare the distance between the current Location Point  and the last drawed location Point
     * Calculate the distance between current Location Point and the last location Point
     * if the distance exceeds the threshold of 850 m, the current Location Point will be drawed,
     * otherwise, the current location point will be clustered.
     *
     * @param locationEntry current location point
     * @param lastLocationEntry last drawed location point
     * @param currentSegment the current timeline segment
     * @return null, if the location point has to be clustered, otherwise the location point has to be drawed
     */
    private LocationEntry tobeClustered(LocationEntry locationEntry, LocationEntry lastLocationEntry, TimelineSegment currentSegment) {
        int TOLERANZ = 3;

        if(choosedTimelineSegments.indexOf(currentSegment) == 0) { //First Segment?
            firstLocationEntry = locationEntry;
            return locationEntry;
        } else if (choosedTimelineSegments.indexOf(currentSegment) == choosedTimelineSegments.size() - 1) { //Last Segment?
            return locationEntry;
        } else if(lastLocationEntry == null){ //last Location to Compare is null?
            return locationEntry;
        } else {
            Location startLocation = locationEntry.getMyLocation();
            Location lastLocation = lastLocationEntry.getMyLocation();

            double distance = startLocation.distanceTo(lastLocation);

            if(distance > 850 + TOLERANZ) {
                return locationEntry;
            }
        }

        return null;
    }
    /**
     * Resize Image from Drawable
     * @param res
     * @param resId Image Resource Id
     * @param reqWidth Required Image With
     * @param reqHeight Required Image Height
     * @return new scaled Bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Helper Method for resizing the Bitmap
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return the inSampleSize Value
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Overwritten onResume Method
     */
    @Override
    protected void onResume() {
        super.onResume();
        choosedChildren.removeAllViews();
        initTimelineView();
    }
}
