package com.slt;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.TemporaryDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Show the Timeline of the choosed friend
 */
public class TimelineFriend extends AppCompatActivity implements View.OnClickListener {



    /**
     *Handler for callbacks
     */
    public Handler handler = new Handler();
    //@BindView(R.id.toolbar)
    //public Toolbar toolBar;

    /**
     * ChoosedTimelineDay
     */
    private TimelineDay choosedTimelineDay;

    /**
     * list_TimelineDays
     */
    private ArrayList<LinearLayout> list_TimelineDays = new ArrayList<>();

    /**
     * view_timelineDays
     */
    private LinearLayout view_timelineDays;


    /**
     * counter_timelinedays
     */
    private int counter_timelinedays;

    /**
     * TAG_TIMELINEDAY
     */
    private final String TAG_TIMELINEDAY = "timelineday";

    /**
     * TAG_TIMELINESEGMENT
     */
    private final String TAG_TIMELINESEGMENT = "timelinesegment";

    /**
     * timeLine Days
     */
    private LinkedList<TimelineDay> timeLineDays;

    /**
     * Timeline from the logged in User
     */
    private Timeline t;

    /**
     * HashMap to check if the specific Timeline Day is already viewed.
     */
    private HashMap<String, TimelineDay> h_viewedTimelineDays = new HashMap<>();

    /**
     * overwritten onCreate Method
     * @param savedInstanceState the saved Instance State
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_friend);

        User friend = TemporaryDB.getInstance().getChoosedFriend();
        t = friend.getMyTimeline();

        view_timelineDays = (LinearLayout) findViewById(R.id.friend_timeline_days);
        TextView friendTitle = (TextView) findViewById(R.id.toolbar_friendEmail);
        friendTitle.setText("Friend: " + friend.getEmail());
        //Thread t = new Thread(new TrackingSimulator());
        //t.start();

        updateTimelineDays();
    }

    /**
     * check if the Timeline Day is not viewed.
     * @param id check the Timeline Day with this object id
     * @return true: if timeline day is not viewed, else: false
     */
    public boolean timelinedayIsNotViewed(String id) {
        return (h_viewedTimelineDays.get(id) == null)? true:false;
    }

    /**
     * Update the Timeline Day View
     */

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
                counter_timelinedays = timeLineDays.size() - 1;
                //view_timelineDays.removeAllViews();
                for (int i = timeLineDays.size() - 1; i >= 0; i--) {
                    TimelineDay t_d = timeLineDays.get(i);

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
                                    double duration = t_d.getDuration(detectedActivity.getType());

                                    String informations = "Type: Bicycle";
                                    informations += "\nDistance: " + String.format("%.2f", (float)activeDistance) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) duration) + " min";

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
                                    duration = t_d.getDuration(detectedActivity.getType());
                                    int userSteps = t_d.getUserSteps(detectedActivity);

                                    informations = "Type: On Foot";
                                    informations += "\nDistance: " + String.format("%.2f", (float) activeDistance) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) duration) + " min";
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
                                    duration = t_d.getDuration(detectedActivity.getType());
                                    userSteps = t_d.getUserSteps(detectedActivity);

                                    informations = "Type: Running";
                                    informations += "\nDistance: " + String.format("%.2f", (float)activeDistance) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float)duration) + " min";

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
                                    duration = t_d.getDuration(detectedActivity.getType());
                                    userSteps = t_d.getUserSteps(detectedActivity);

                                    informations = "Type: Walking";
                                    informations += "\nDistance: " + String.format("%.2f",(float) activeDistance) + " m";
                                    informations += "\nDuration: " + String.format("%.2f", (float) duration) + " min";
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
                                    duration = t_d.getDuration(detectedActivity.getType());

                                    informations = "Type: Vehicle";
                                    informations += "\nDistance: " + String.format("%.2f", (float) activeDistance) + " m";

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view_timelineDays.addView(row);
                                list_TimelineDays.add(row);
                            }
                        });


                        counter_timelinedays--;
                    }
                }

            }


        }catch(Exception e) {

        }

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

    /**
     * Check if the specific Timeline Day is clicked
     * @param v the Timeline Day View which is clicked
     */
    @Override
    public void onClick(View v) {

        try {

            switch ((String) v.getTag()) {
                case TAG_TIMELINEDAY:
                    choosedTimelineDay = timeLineDays.get(v.getId());
                    LinkedList<TimelineSegment> choosedSegments = choosedTimelineDay.getMySegments();
                    DataProvider.getInstance().setChoosedTimelineSegments(choosedTimelineDay.getMySegments());
                    Intent i = new Intent(this, SegmentViewActivity.class);
                    startActivity(i);
                    break;

            }
        }catch (Exception e) {
        }
    }

}
