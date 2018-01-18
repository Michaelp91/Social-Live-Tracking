package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.MainProfile;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.LocationEntry;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.model.Response;
import com.slt.network.NetworkUtil;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.TemporaryDB;
import com.slt.restapi.TrackingSimulator;
import com.slt.restapi.data.Image;
import com.slt.restapi.data.REST_LocationEntry;
import com.slt.restapi.data.REST_TimelineDay;
import com.slt.restapi.data.REST_TimelineSegment;
import com.slt.utils.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.slt.utils.Validation.validateEmail;
import static com.slt.utils.Validation.validateFields;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentTimeline extends Fragment implements View.OnClickListener {

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
    private View view;
    private Timeline t;
    private HashMap<Date, TimelineDay> h_viewedTimelineDays = new HashMap<>();
    private HashMap<Date, TimelineSegment> h_viewedTimelineSegments = new HashMap<>();
    private HashMap<Date, TimelineDay> h_alreadyChoosedDay = new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.timeline_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_timelineDays = (LinearLayout) view.findViewById(R.id.timeline_days);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Timeline");
        //Thread t = new Thread(new TrackingSimulator());
        //t.start();


        RetrieveOperations.getInstance().context = this;
        handler.postDelayed(runnable, 2000);

        this.view = view;
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
                  t = RetrieveOperations.getInstance().getCompleteTimeline();
                  updateTimelineDays();
              }
              handler.postDelayed(runnable, 2000);
          }
      }).start();

      /* and here comes the "trick" */
        }
    };

    public boolean timelinedayIsNotViewed(Date date) {
        return (h_viewedTimelineDays.get(date) == null)? true:false;
    }

    public boolean timelinesegmentIsNotViewed(Date date) {
        return (h_viewedTimelineSegments.get(date) == null)? true:false;
    }

    public void updateTimelineDays() {

        /*
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view_timelineDays.removeAllViews();
            }
        });
        */


        LayoutInflater inflater = LayoutInflater.from(this.getActivity());


        timeLineDays = t.getTimelineDays();
        counter_timelinedays = 0;
        //view_timelineDays.removeAllViews();
        for(TimelineDay t_d: timeLineDays) {

            if(timelinedayIsNotViewed(t_d.getMyDate())) {
                h_viewedTimelineDays.put(t_d.getMyDate(), t_d);
                final LinearLayout row = (LinearLayout) inflater.inflate(R.layout.timeline_day, null);
                TextView myDate = (TextView) row.findViewById(R.id.tv_myDate);
//            ImageView imageView = (ImageView) row.findViewById(R.id.iv_activity);
//            UsefulMethods.UploadImageView(imageView);


                Date date = t_d.getMyDate();

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String strDate = sdf.format(date);

                myDate.setText(strDate);

                row.setTag(TAG_TIMELINEDAY);
                row.setId(counter_timelinedays);
                row.setOnClickListener(this);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view_timelineDays.addView(row);
                        list_TimelineDays.add(row);
                    }
                });


                counter_timelinedays++;
            }

            if(choosedChildren != null) {
                updateTimelineView();
            }
        }

    }

    public void updateTimelineView() {

/*
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                choosedChildren.removeAllViews();
            }
        });

        */


        final LayoutInflater inflater = LayoutInflater.from(this.getActivity());

        LinkedList<TimelineSegment> timeLineSegments = choosedTimelineDay.getMySegments();
        boolean isAdded = false;


        for(TimelineSegment tSegment: timeLineSegments) {

            if (timelinesegmentIsNotViewed(tSegment.getStartTime())) {
                isAdded = true;
                h_viewedTimelineSegments.put(tSegment.getStartTime(), tSegment);
                LinkedList<LocationEntry> locationEntries = tSegment.getLocationPoints();

                RelativeLayout view_FirstPoint = null;
                RelativeLayout view_segment = null;
                RelativeLayout view_LastPoint = null;

                if (!locationEntries.isEmpty()) {
                    LocationEntry fstPoint = locationEntries.get(0);

                    view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                    TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeAndaddress);
                    TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    String strDate = sdf.format(fstPoint.getMyEntryDate());


                    myEntryDate.setText(strDate);
                    placeAndaddress.setText(tSegment.getStartAddress());

                    if (timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint



                        view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                        TextView activeTime = (TextView) view_segment.findViewById(R.id.tv_activeTime);
                        TextView activeDistance = (TextView) view_segment.findViewById(R.id.tv_activedistance);
                        final ImageView activity = (ImageView) view_segment.findViewById(R.id.iv_activity);
                        final LinearLayout ll_line = (LinearLayout) view_segment.findViewById(R.id.ll_line);

                        final DetectedActivity detectedActivity = tSegment.getMyActivity();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch(detectedActivity.getType()) {
                                    case com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING:
                                        activity.setImageResource(R.drawable.walking);
                                        ll_line.setBackgroundColor(Color.GREEN);
                                        break;
                                    case com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING:
                                        activity.setImageResource(R.drawable.running);
                                        ll_line.setBackgroundColor(R.color.md_amber_800);
                                        break;
                                    default:
                                        activity.setVisibility(View.GONE);
                                        break;
                                }
                            }
                        });



                        activeTime.setText(Double.toString(tSegment.getDuration()));
                        activeDistance.setText(Double.toString(tSegment.getActiveDistance()));



                        /*
                        view_LastPoint = (RelativeLayout)inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView placeAndaddress_endlocation = (TextView) view_LastPoint.findViewById(R.id.tv_placeAndaddress);
                        TextView myEntryDate_endlocation = (TextView) view_LastPoint.findViewById(R.id.tv_myEntryDate);

                        sdf = new SimpleDateFormat("dd.MM.yyyy");
                        strDate = sdf.format(sndPoint.myEntryDate);


                        myEntryDate_endlocation.setText(strDate);
                        placeAndaddress_endlocation.setText("");
                        */

                    }

                }


                final RelativeLayout finalView_FirstPoint = view_FirstPoint;
                final RelativeLayout finalView_segment = view_segment;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (finalView_FirstPoint != null)
                            choosedChildren.addView(finalView_FirstPoint);


                        if (finalView_segment != null)
                            choosedChildren.addView(finalView_segment);

                    }
                });

            }

        }


        if(isAdded) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout whiteSpace = (LinearLayout) inflater.inflate(R.layout.timeline_whitespace, null);

                    choosedChildren.addView(whiteSpace);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch ((String)v.getTag()) {
            case TAG_TIMELINEDAY:
                LinearLayout tday = list_TimelineDays.get(v.getId());
                choosedChildren = (LinearLayout) tday.findViewById(R.id.ll_all_locations);
                choosedTimelineDay = timeLineDays.get(v.getId());
                choosedChildren.removeAllViews();
                h_viewedTimelineSegments = new HashMap<>();

                if(h_alreadyChoosedDay.get(choosedTimelineDay.getMyDate()) == null) {
                        h_alreadyChoosedDay = new HashMap<>();
                        h_alreadyChoosedDay.put(choosedTimelineDay.getMyDate(), choosedTimelineDay);
                        updateTimelineView();
                } else {
                    h_alreadyChoosedDay = new HashMap<>();
                    choosedTimelineDay = null;
                    choosedChildren = null;
                }


                break;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}

