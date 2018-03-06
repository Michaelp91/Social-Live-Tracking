package com.slt;

import android.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.android.gms.maps.model.LatLng;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.UsefulMethods;
import com.slt.utils.FunctionalityLogger;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class TimelineFriend extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FragmentTimeline";
    private static final LatLng DARMSTADT_NORD = new LatLng(50.0042304, 9.0658932);
    private static final LatLng WILLYBRANDTPLATZ = new LatLng(49.9806625, 9.1355554);
    public Handler handler = new Handler();
    //@BindView(R.id.toolbar)
    //public Toolbar toolBar;

    private TimelineDay choosedTimelineDay;
    private TimelineDay tmpTimelineDay = null;
    private ArrayList<LinearLayout> list_TimelineDays = new ArrayList<>();
    private ArrayList<LinearLayout> list_TimelineSegments;
    private LinearLayout view_timelineDays;
    private LinearLayout choosedChildren;
    private int counter_timelinedays;
    private int counter_timelinechildren;
    private final String TAG_TIMELINEDAY = "timelineday";
    private final String TAG_TIMELINESEGMENT = "timelinesegment";
    private LinkedList<TimelineDay> timeLineDays;
    private Timeline t;
    private HashMap<String, TimelineDay> h_viewedTimelineDays = new HashMap<>();
    private HashMap<String, TimelineSegment> h_viewedTimelineSegments = new HashMap<>();
    private HashMap<String, TimelineDay> h_alreadyChoosedDay = new HashMap<>();
    private LinkedList<Integer> randomPositions = new LinkedList<>();
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    ImageView tmpImageView;
    private LinearLayout choosedPicView;
    private HashMap<String, LinearLayout> picViews = new HashMap<>();
    private HashMap<String, LinkedList<Bitmap>>  downloadedImagesByTSegmentId = new HashMap<>();

    private int loggerCounter = 0;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_friend);
        view_timelineDays = findViewById(R.id.timeline_days);

        setTitle("Timeline By Friend");
        //Thread t = new Thread(new TrackingSimulator());
        //t.start();
        context = this;
        handler.postDelayed(runnable, 2000);
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = DataProvider.getInstance().getOwnUser();
                    if(user != null) {
                        t = RetrieveOperations.getInstance().getCompleteTimelineByFriend();
                        updateTimelineDays();
                    }
                    handler.postDelayed(runnable, 2000);
                }
            }).start();

      /* and here comes the "trick" */
        }
    };

    public boolean timelinedayIsNotViewed(String id) {
        return (h_viewedTimelineDays.get(id) == null)? true:false;
    }

    public boolean timelinesegmentIsNotViewed(String id) {
        return (h_viewedTimelineSegments.get(id) == null)? true:false;
    }

    public void updateTimelineDays() {
        boolean activity_runningIsDisplayed = false;
        boolean activity_walkingIsDisplayed = false;
        boolean activity_bikingIsDisplayed = false;
        boolean activity_onFootIsDisplayed = false;
        boolean activity_onVehicleIsDisplayed = false;
        String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        /*
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view_timelineDays.removeAllViews();
            }
        });
        */
        try {



            if (this != null) {
                LayoutInflater inflater = LayoutInflater.from(this);


                try {
                    timeLineDays = t.getTimelineDays();
                } catch (NullPointerException e) {
                    return;
                }
                counter_timelinedays = 0;
                //view_timelineDays.removeAllViews();
                for (TimelineDay t_d : timeLineDays) {
                    activity_runningIsDisplayed = false;
                    activity_walkingIsDisplayed = false;
                    activity_bikingIsDisplayed = false;
                    activity_onFootIsDisplayed = false;
                    activity_onVehicleIsDisplayed = false;

                    if (timelinedayIsNotViewed(t_d.getID())) {

                        h_viewedTimelineDays.put(t_d.getID(), t_d);
                        final LinearLayout row = (LinearLayout) inflater.inflate(R.layout.timelineview_day, null);
                        TextView tvDayOfWeek = (TextView) row.findViewById(R.id.tv_tday_dayofweek);
                        TextView tv_tday_date = (TextView) row.findViewById(R.id.tv_tday_date);
                        ImageView iv_arrow_forward = (ImageView) row.findViewById(R.id.iv_arrow_forward);
                        LinearLayout ll_tday_border = (LinearLayout) row.findViewById(R.id.ll_tday_border);

                        Date date = t_d.getMyDate();

                        Date today = new Date();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String strDate = sdf.format(date);

                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
                        String strToday = sdf1.format(today);

                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                        if(strDate.equals(strToday)) {
                            boolean debug = true;

                            debug = false;
                            int test = 2;
                            test = 4;

                            if(test == 4) {
                                Log.d("t", "Hello World");
                            }

                        }



                        //          TextView myDate = (TextView) row.findViewById(R.id.tv_myDate);
//            ImageView imageView = (ImageView) row.findViewById(R.id.iv_activity);
//            UsefulMethods.UploadImageView(imageView);

                        Set<Integer> activities =  t_d.getActivity_totalUsersteps().keySet();

                        //Filling Timeline Informations
                        for(Integer a: activities) {
                            switch (a) {
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_BICYCLE:
                                    activity_bikingIsDisplayed = true;

                                    TextView info = (TextView) row.findViewById(R.id.tv_tday_infoBicycle);
                                    ImageView iv_bicycle = (ImageView) row.findViewById(R.id.iv_bicycle);
                                    Bitmap bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_biking, 100, 100);
                                    iv_bicycle.setImageBitmap(bmp);
                                    DetectedActivity detectedActivity = new DetectedActivity(com.slt.definitions.Constants.TIMELINEACTIVITY.ON_BICYCLE, 100);
                                    double activeDistance = t_d.getActiveDistance(detectedActivity);
                                    double duration = t_d.getActiveTime(detectedActivity);

                                    String informations = "Type: Bicycle";
                                    informations += "\nDistance: " + Float.toString((float)activeDistance) + " m";
                                    informations += "\nDuration: " + Float.toString((float) duration) + " min";

                                    info.setText(informations);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT:
                                    activity_onFootIsDisplayed = true;
                                    info = (TextView) row.findViewById(R.id.tv_tday_infoOnfoot);
                                    ImageView iv = (ImageView) row.findViewById(R.id.iv_onfoot);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_walking, 100, 100);
                                    iv.setImageBitmap(bmp);
                                    detectedActivity = new DetectedActivity(com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT, 100);
                                    activeDistance = t_d.getActiveDistance(detectedActivity);
                                    duration = t_d.getActiveTime(detectedActivity);
                                    int userSteps = t_d.getUserSteps(detectedActivity);

                                    informations = "Type: On Foot";
                                    informations += "\nDistance: " + Float.toString((float) activeDistance) + " m";
                                    informations += "\nDuration: " + Float.toString((float) duration) + " min";
                                    informations += "\nUser Steps: " + userSteps;

                                    info.setText(informations);

                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING:
                                    activity_runningIsDisplayed = true;

                                    info = (TextView) row.findViewById(R.id.tv_tday_infoRunning);
                                    iv = (ImageView) row.findViewById(R.id.iv_running);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_running, 100, 100);
                                    iv.setImageBitmap(bmp);


                                    detectedActivity = new DetectedActivity(com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING, 100);
                                    activeDistance = t_d.getActiveDistance(detectedActivity);
                                    duration = t_d.getActiveTime(detectedActivity);
                                    userSteps = t_d.getUserSteps(detectedActivity);

                                    informations = "Type: Running";
                                    informations += "\nActive Distance: " + Float.toString((float)activeDistance) + " m";
                                    informations += "\nDuration: " + Float.toString((float)duration) + " min";

                                    info.setText(informations);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING:
                                    activity_walkingIsDisplayed = true;

                                    info = (TextView) row.findViewById(R.id.tv_tday_infoWalking);
                                    iv = (ImageView) row.findViewById(R.id.iv_walking);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_walking, 100, 100);
                                    iv.setImageBitmap(bmp);

                                    detectedActivity = new DetectedActivity(com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING, 100);
                                    activeDistance = t_d.getActiveDistance(detectedActivity);
                                    duration = t_d.getActiveTime(detectedActivity);
                                    userSteps = t_d.getUserSteps(detectedActivity);

                                    informations = "Type: Walking";
                                    informations += "\nActive Distance: " + Float.toString((float) activeDistance) + " m";
                                    informations += "\nDuration: " + Float.toString( (float) duration) + " min";
                                    informations += "\nUser Steps: " + userSteps;

                                    info.setText(informations);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.IN_VEHICLE:
                                    activity_onVehicleIsDisplayed = true;

                                    info = (TextView) row.findViewById(R.id.tv_tday_infoVehicle);
                                    iv = (ImageView) row.findViewById(R.id.iv_vehicle);
                                    bmp = decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_vehicle, 100, 100);
                                    iv.setImageBitmap(bmp);

                                    detectedActivity = new DetectedActivity(com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING, 100);
                                    activeDistance = t_d.getActiveDistance(detectedActivity);
                                    duration = t_d.getActiveTime(detectedActivity);

                                    informations = "Type: Vehicle";
                                    informations += "\nActive Distance: " + Float.toString((float) activeDistance) + " m";
                                    informations += "\nDuration: " + Float.toString( (float) duration) + " min";

                                    info.setText(informations);
                                    break;
                            }
                        }

                        //Invisible inrelevant Parts
                        if (!activity_bikingIsDisplayed) {
                            LinearLayout ll_tday_segment1Biking = (LinearLayout) row.findViewById(R.id.ll_tday_segment1OnBicycle);
                            LinearLayout ll_tday_segment2Biking = (LinearLayout) row.findViewById(R.id.ll_tday_segment2OnBicycle);
                            LinearLayout ll_tday_segment3Biking = (LinearLayout) row.findViewById(R.id.ll_tday_segment3OnBicycle);
                            LinearLayout ll_tday_boxbicycle = (LinearLayout) row.findViewById(R.id.ll_tday_boxbicycle);

                            ll_tday_segment1Biking.setVisibility(View.GONE);
                            ll_tday_segment2Biking.setVisibility(View.GONE);
                            ll_tday_segment3Biking.setVisibility(View.GONE);
                            ll_tday_boxbicycle.setVisibility(View.GONE);

                        }

                        if (!activity_onFootIsDisplayed) {
                            LinearLayout ll_tday_segment1OnFoot = (LinearLayout) row.findViewById(R.id.ll_tday_segment1OnFoot);
                            LinearLayout ll_tday_segment2OnFoot = (LinearLayout) row.findViewById(R.id.ll_tday_segment2OnFoot);
                            LinearLayout ll_tday_boxonfoot = (LinearLayout) row.findViewById(R.id.ll_tday_boxonfoot);

                            ll_tday_segment1OnFoot.setVisibility(View.GONE);
                            ll_tday_segment2OnFoot.setVisibility(View.GONE);
                            ll_tday_boxonfoot.setVisibility(View.GONE);

                        }

                        if (!activity_runningIsDisplayed) {
                            LinearLayout ll_tday_segment1Running = (LinearLayout) row.findViewById(R.id.ll_tday_segment1Running);
                            LinearLayout ll_tday_segment2Running = (LinearLayout) row.findViewById(R.id.ll_tday_segment2Running);
                            LinearLayout ll_tday_segment3Running = (LinearLayout) row.findViewById(R.id.ll_tday_segment3Running);
                            LinearLayout ll_tday_boxRunning = (LinearLayout) row.findViewById(R.id.ll_tday_boxRunning);

                            ll_tday_segment1Running.setVisibility(View.GONE);
                            ll_tday_segment2Running.setVisibility(View.GONE);
                            ll_tday_segment3Running.setVisibility(View.GONE);
                            ll_tday_boxRunning.setVisibility(View.GONE);

                        }

                        if (!activity_walkingIsDisplayed) {
                            LinearLayout ll_tday_segment1Walking = (LinearLayout) row.findViewById(R.id.ll_tday_segment1Walking);
                            LinearLayout ll_tday_segment2Walking = (LinearLayout) row.findViewById(R.id.ll_tday_segment2Walking);
                            LinearLayout ll_tday_segment3Walking = (LinearLayout) row.findViewById(R.id.ll_tday_segment3Walking);
                            LinearLayout ll_tday_boxwalking = (LinearLayout) row.findViewById(R.id.ll_tday_boxwalking);

                            ll_tday_segment1Walking.setVisibility(View.GONE);
                            ll_tday_segment2Walking.setVisibility(View.GONE);
                            ll_tday_segment3Walking.setVisibility(View.GONE);
                            ll_tday_boxwalking.setVisibility(View.GONE);
                        }

                        if (!activity_onVehicleIsDisplayed) {
                            LinearLayout ll_tday_segment1Vehicle = (LinearLayout) row.findViewById(R.id.ll_tday_segment1Vehicle);
                            LinearLayout ll_tday_segment2Vehicle = (LinearLayout) row.findViewById(R.id.ll_tday_segment2Vehicle);
                            LinearLayout ll_tday_segment3Vehicle = (LinearLayout) row.findViewById(R.id.ll_tday_segment3Vehicle);
                            LinearLayout ll_tday_boxvehicle = (LinearLayout) row.findViewById(R.id.ll_tday_boxVehicle);

                            ll_tday_segment1Vehicle.setVisibility(View.GONE);
                            ll_tday_segment2Vehicle.setVisibility(View.GONE);
                            ll_tday_segment3Vehicle.setVisibility(View.GONE);
                            ll_tday_boxvehicle.setVisibility(View.GONE);
                        }

                        if(!activity_bikingIsDisplayed && !activity_onFootIsDisplayed && !activity_runningIsDisplayed
                                && !activity_walkingIsDisplayed && !activity_onVehicleIsDisplayed) {
                            iv_arrow_forward.setVisibility(View.GONE);
                            ll_tday_border.setVisibility(View.GONE);
                        }






                        tv_tday_date.setText(strDate);

                        if(strDate.equals(strToday))
                            tvDayOfWeek.setText("Today");
                        else
                            tvDayOfWeek.setText(weekdays[dayOfWeek - 1]);

                        // myDate.setText(strDate);

                        row.setTag(TAG_TIMELINEDAY);
                        row.setId(counter_timelinedays);
                        row.setOnClickListener(this);

                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view_timelineDays.addView(row);
                                list_TimelineDays.add(row);
                            }
                        });


                        counter_timelinedays++;
                    }

                    //TODO: Problem with updating TimelineView, look at this problem later
                    /*
                    else if (choosedTimelineDay != null) {

                        if(t_d.getID().equals(choosedTimelineDay.getID()) && t_d.getMySegments().size() != choosedTimelineDay.getMySegments().size()
                                && t_d.getMySegments().getLast().getMyLocationPoints().size() != 0) {

                            picViews = new HashMap<>();
                            h_viewedTimelineSegments = new HashMap<>();
                            choosedTimelineDay = t_d;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    choosedChildren.removeAllViews();
                                }
                            });
                        }
                        */

                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                //TODO: Problem with updating TimelineView, look at this problem later
                    /*
                    if (choosedChildren != null && choosedTimelineDay != null) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                updateTimelineView();
                            }
                        });
                    }
                    */

            }


        }catch(Exception e) {
            FunctionalityLogger.getInstance().AddErrorLog("UpdateTimelineDay(): " + e.getMessage().toString());
        }

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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    @Override
    public void onClick(View v) {

        try {

            switch ((String) v.getTag()) {
                case TAG_TIMELINEDAY:
                    LinearLayout tday = list_TimelineDays.get(v.getId());
                    choosedChildren = (LinearLayout) tday.findViewById(R.id.ll_all_locations);
                    choosedTimelineDay = timeLineDays.get(v.getId());
                    DataProvider.getInstance().setChoosedTimelineSegments(choosedTimelineDay.getMySegments());
                    Intent i = new Intent(this, SegmentViewActivity.class);
                    startActivity(i);

                    /*
                    choosedChildren.removeAllViews();
                    h_viewedTimelineSegments = new HashMap<>();

                    if (h_alreadyChoosedDay.get(choosedTimelineDay.getID()) == null) {
                        h_alreadyChoosedDay = new HashMap<>();
                        h_alreadyChoosedDay.put(choosedTimelineDay.getID(), choosedTimelineDay);


                        final LinkedList<TimelineSegment> tSegments = choosedTimelineDay.getMySegments();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (TimelineSegment t : tSegments) {
                                    DownloadPictures(t);
                                }

                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
                                String strDate1 = sdf1.format(choosedTimelineDay.getMyDate());

                                FunctionalityLogger.getInstance().AddLog("Tag: " + strDate1 + "\n");
                                FunctionalityLogger.getInstance().AddLog("Timeline Daten: ");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateTimelineView();
                                    }
                                });

                            }
                        }).start();


                    } else {
                        h_alreadyChoosedDay = new HashMap<>();
                        downloadedImagesByTSegmentId = new HashMap<>();
                        choosedTimelineDay = null;
                        choosedChildren = null;
                    }

*/
                    break;

            }
        }catch (Exception e) {
            FunctionalityLogger.getInstance().AddErrorLog("OnClick on TimelineDay: " +  e.getMessage().toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
