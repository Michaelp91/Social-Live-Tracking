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

public class SegmentViewActivity extends AppCompatActivity {

    private LinkedList<TimelineSegment> choosedTimelineSegments;

    private static final String TAG = "FragmentTimeline";
    private static final LatLng DARMSTADT_NORD = new LatLng(50.0042304, 9.0658932);
    private static final LatLng WILLYBRANDTPLATZ = new LatLng(49.9806625, 9.1355554);
    public Handler handler = new Handler();
    //@BindView(R.id.toolbar)
    //public Toolbar toolBar;
    private ArrayList<LinearLayout> list_TimelineSegments;
    private LinearLayout choosedChildren;
    private int counter_timelinedays;
    private int counter_timelinechildren;
    private final String TAG_TIMELINESEGMENT = "timelinesegment";
    private View view;
    private Timeline t;
    private LinkedList<Integer> randomPositions = new LinkedList<>();
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;

    ImageView tmpImageView;
    private LinearLayout choosedPicView;
    private HashMap<String, LinearLayout> picViews = new HashMap<>();
    private HashMap<String, LinkedList<Bitmap>>  downloadedImagesByTSegmentId = new HashMap<>();

    private int loggerCounter = 0;
    private Activity context;
    private LocationEntry firstLocationEntry;

    private GoogleMap googleMap;
    MapView mMapView;
    private float zoomLevel = 17.0f;
    private LocationEntry start = null;
    private LocationEntry end = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_view);
        choosedChildren = (LinearLayout) findViewById(R.id.timeline_segments);
        this.choosedTimelineSegments = DataProvider.getInstance().getChoosedTimelineSegments();
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        boolean failed = false;


        try {
            start = choosedTimelineSegments.getFirst().getLocationPoints().getFirst();
            failed = true;
        }catch(Exception e) {
            failed = false;
        }

        try{
            end = choosedTimelineSegments.getLast().getLocationPoints().getLast();
            failed = true;
        }catch (Exception e) {
            failed = false;
        }

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
                // googleMap.getUiSettings().setZoomControlsEnabled(true);
                LocationEntry first = null;
                LocationEntry last = null;
                if (start != null && end != null) {
                    first = start;
                    last = end;

                    /*
                    LatLng start = new LatLng(first.getLatitude(), first.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(start).icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_start ) )
                            .title( "START" ));

                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(end).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_finish ))
                            .title("FINISH"));
                            */


                    LatLng start = new LatLng(first.getLatitude(), first.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    Marker m = googleMap.addMarker(new MarkerOptions().position(start).icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_start ) ).title("START"));
                    m.showInfoWindow();

                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(end).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_finish ))
                            .title("FINISH"));
                    ZoomCamera(start, end);

                    addLines(start, end);

                } else {
                    LatLng startLatLng = new LatLng(start.getLatitude(), start.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(startLatLng).icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_start ) ).title(""));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, zoomLevel));
                }
            }
        });
        context = this;
        //initTimelineView();
    }

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
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -3.0);
                            googleMap.animateCamera(zout);
                        }
                    });
                }
            }
        });
    }

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
            int lastType = -1;
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
                            && detectedActivity.getType() != Constants.TIMELINEACTIVITY.UNKNOWN
                            && (lastType != detectedActivity.getType())  ) {
                        lastType = detectedActivity.getType();

                        FunctionalityLogger.getInstance().AddLog("\nTimeline Segment: ");
                        FunctionalityLogger.getInstance().AddLog("Number: " + loggerCounter);
                        FunctionalityLogger.getInstance().AddLog("ObjectId: " + tSegment.getID());
                        FunctionalityLogger.getInstance().AddLog("Activity(IN_VEHICLE, ON_BICYCLE, ON_FOOT, STILL, UNKNOWN, TILTING, WALKING, RUNNING): " + tSegment.getMyActivity().getType());
                        FunctionalityLogger.getInstance().AddLog("Start: " + tSegment.getAddress());

                        FunctionalityLogger.getInstance().AddLog("");
                        loggerCounter++;


                        isAdded = true;


                        LocationEntry fstPoint = tobeClustered(locationEntries.get(0), lastLocationEntry, tSegment);
                        lastLocationEntry = (fstPoint == null)? lastLocationEntry: fstPoint;



                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);
                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                        if(fstPoint == null) {
                            view_FirstPoint.setVisibility(View.GONE);
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                        String strDate = (fstPoint != null)? sdf.format(fstPoint.getMyEntryDate()): "";

                        FunctionalityLogger.getInstance().AddLog("Start: " + strDate);

                        if(fstPoint != null)
                          FunctionalityLogger.getInstance().AddLog("Location Point(Longitude, Lattitude): " + fstPoint.getLongitude() + ", " + fstPoint.getLatitude());


                        myEntryDate.setText(strDate + " Uhr");
                        placeAndaddress.setText(tSegment.getStartPlace());

                        if (timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint


                            view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                            final TextView activityInfo = (TextView) view_segment.findViewById(R.id.tv_activityinfo);

                            String informations = "";

                            switch (detectedActivity.getType()) {
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING:

                                    informations = "Type: Walking";
                                    informations += "\nDistance: " + Float.toString((float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + Float.toString((float) tSegment.getActiveTime()) + " min";
                                    informations += "\nUser Steps: " + tSegment.getUserSteps();

                                    break;
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING:

                                    informations = "Type: Running";
                                    informations += "\nDistance: " + Float.toString((float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + Float.toString( (float) tSegment.getActiveTime() ) + " min";

                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.IN_VEHICLE:
                                    informations = "Type: Vehicle";
                                    informations += "\nDistance: " + Float.toString((float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + Float.toString( (float) tSegment.getActiveTime() ) + " min";
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT:
                                    informations = "Type: On Foot";
                                    informations += "\nDistance: " + Float.toString((float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + Float.toString( (float) tSegment.getActiveTime() ) + " min";
                                    informations += "\nUser Steps: " + tSegment.getUserSteps();

                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_BICYCLE:
                                    informations = "Type: Bicycle";
                                    informations += "\nDistance: " + Float.toString((float) tSegment.getActiveDistance()) + " m";
                                    informations += "\nDuration: " + Float.toString( (float) tSegment.getActiveTime() ) + " min";
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



                        /*
                        view_LastPoint = (RelativeLayout)inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView placeAndaddress_endlocation = (TextView) view_LastPoint.findViewById(R.id.tv_placeAndaddress);
                        TextView myEntryDate_endlocation = (TextView) view_LastPoint.findViewById(R.id.tv_myEntryDate);

                        sdf = new SimpleDateFormat("dd.MM.yyyy");
                        strDate = sdf.format(sndPoint.myEntryDate);


                        myEntryDate_endlocation.setText(strDate);
                        placeAndaddress_endlocation.setText("");
                        */

                        } else {
                            //ll_shortLine.setVisibility(View.INVISIBLE);
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
                        placeAndaddress.setText(tSegment.getStartAddress());
                    } else if (timeLineSegments.indexOf(tSegment) == timeLineSegments.size() - 1
                            && (tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.STILL
                            || tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN)) {

                        Location l = new Location("");
                        l.setLatitude(0);
                        l.setLongitude(0);
                        LocationEntry fstPoint = (locationEntries.size() > 0)? locationEntries.get(0): new LocationEntry(l, null, null, null);

                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);

                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String strDate = (fstPoint.getMyEntryDate() != null)? sdf.format(fstPoint.getMyEntryDate()): "Unknown";


                        myEntryDate.setText(strDate);
                        placeAndaddress.setText(tSegment.getStartAddress());
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
                                SharedResources.getInstance().setEntryStart(firstLocationEntry);
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

    private Bitmap resizeImage(byte[] input, int width, int height) {
        Bitmap original = BitmapFactory.decodeByteArray(input , 0, input.length);
        Bitmap resized = Bitmap.createScaledBitmap(original, width, height, true);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        return resized;
    }

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

    public Drawable resizeImage(int imageResource) {// R.drawable.icon
        // Get device dimensions
        Display display = getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
                imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(this.getResources(),
                getResizedBitmap2(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    /************************ Resize Bitmap *********************************/
    public Bitmap getResizedBitmap2(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private void AddUserComments(LinkedList<String> strUserComments, LinearLayout ll_line, TextView tv_usercomments) {

        /*
        String comments = (strUserComments.size() > 0)? "": "Keine Kommentare vorhanden";


        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        int heightToAdd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        for(String u: strUserComments) {
            comments += u;
            heightToAdd += heightToAdd;
            boolean debug = true;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)  ll_line.getLayoutParams();
        params.height += heightToAdd;
        ll_line.setLayoutParams(params);


        tv_usercomments.setText(comments);
        */
    }

    private void selectImage() {
        try {
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();
                bitmap = resizeImage(BYTE, 100, 100);

                //this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                //this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);
                TimelineSegment timelineSegment = (TimelineSegment) choosedPicView.getTag();
                LinkedList<Bitmap> bmps = downloadedImagesByTSegmentId.get(timelineSegment.getID());
                bmps = (bmps == null)? new LinkedList<Bitmap>(): bmps;

                bmps.add(bitmap);
                downloadedImagesByTSegmentId.put(timelineSegment.getID(), bmps);

                uploadImage(bitmap);

                Log.e(TAG, "Pick from Camera::>>> ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ApplicationController.getContext().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();
                bitmap = resizeImage(BYTE, 100, 100);

                //this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                //this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);
                TimelineSegment timelineSegment = (TimelineSegment) choosedPicView.getTag();
                LinkedList<Bitmap> bmps = downloadedImagesByTSegmentId.get(timelineSegment.getID());
                bmps = (bmps == null)? new LinkedList<Bitmap>(): bmps;

                bmps.add(bitmap);
                downloadedImagesByTSegmentId.put(timelineSegment.getID(), bmps);

                uploadImage(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i( TAG, " orientation: " + orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap( bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void uploadImage(final Bitmap bitmap) {

        final TimelineSegment timelineSegment = (TimelineSegment) choosedPicView.getTag();
        AddPictures(choosedPicView, timelineSegment);
        Toast.makeText(this, "Image is uploaded successfully.", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                LinkedList<Bitmap> bmps = downloadedImagesByTSegmentId.get(timelineSegment.getID());
                Integer imageId = bmps.size() + 1;


                final boolean uploaded = UsefulMethods.UploadImageView(bitmap, imageId.toString() + ".png");

                timelineSegment.addImage(imageId.toString() + ".png");
                final boolean timelinesegmentUpdated = OtherRestCalls.updateTimelineSegmentForImages(timelineSegment);
            }
        }).start();
    }

    private void DownloadPictures(TimelineSegment tSegment) {

        //For Debug Purposes
        if(tSegment.getMyImages().size() != 0) {
            boolean debug = true;
        }

        ArrayList<Integer> numbersInUse = new ArrayList<>();
        for(String image: tSegment.getMyImages()) {
            Bitmap bmp = UsefulMethods.LoadImage(image);

            if(bmp != null) {
                LinkedList<Bitmap> bmps = downloadedImagesByTSegmentId.get(tSegment.getID());

                bmps = (bmps == null)? new LinkedList<Bitmap>():bmps;
                bmps.add(bmp);
                downloadedImagesByTSegmentId.put(tSegment.getID(), bmps);
            }
        }

        /*
        if(max == 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final LinearLayout ll_nonpicture = (LinearLayout) inflater.inflate(R.layout.timelinesegment_nopictures, null);
                    ll_pictures.addView(ll_nonpicture);
                }
            });
        }
        */


    }

    private LinearLayout AddPictures(LinearLayout ll_pictures, TimelineSegment tSegment) {
        /*
        LinkedList<Bitmap> downloadedImages =  downloadedImagesByTSegmentId.get(tSegment.getID());

        ImageView iv_pic1 = ll_pictures.findViewById(R.id.iv_pic1);
        ImageView iv_pic2 = ll_pictures.findViewById(R.id.iv_pic2);
        ImageView iv_pic3 = ll_pictures.findViewById(R.id.iv_pic3);
        TextView tv_noPicAvailable = ll_pictures.findViewById(R.id.tv_noPicAvailable);

        iv_pic1.setVisibility(View.GONE);
        iv_pic2.setVisibility(View.GONE);
        iv_pic3.setVisibility(View.GONE);
        tv_noPicAvailable.setVisibility(View.VISIBLE);

        if (downloadedImages != null) {
            int max = (downloadedImages.size() >= 3) ? 3 : downloadedImages.size();


            for (int i = 0; i < max; i++) {

                Bitmap bmp = downloadedImages.get(i);


                switch (i) {
                    case 0:
                        tv_noPicAvailable.setVisibility(View.GONE);
                        iv_pic1.setVisibility(View.VISIBLE);
                        iv_pic1.setImageBitmap(bmp);
                        break;
                    case 1:
                        tv_noPicAvailable.setVisibility(View.GONE);
                        iv_pic2.setVisibility(View.VISIBLE);
                        iv_pic2.setImageBitmap(bmp);
                        break;
                    case 2:
                        tv_noPicAvailable.setVisibility(View.GONE);
                        iv_pic3.setVisibility(View.VISIBLE);
                        iv_pic3.setImageBitmap(bmp);
                        break;
                }
            }
        }

*/

        return ll_pictures;
    }

    @Override
    protected void onResume() {
        super.onResume();
        choosedChildren.removeAllViews();
        initTimelineView();
    }
}
