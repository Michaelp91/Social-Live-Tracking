package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.MainProfile;
import com.slt.R;
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
import com.slt.restapi.data.REST_LocationEntry;
import com.slt.restapi.data.REST_TimelineDay;
import com.slt.restapi.data.REST_TimelineSegment;
import com.slt.utils.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private ArrayList<LinearLayout> list_TimelineDays;
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
        Thread t = new Thread(new TrackingSimulator());
        t.start();


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
              User user = OtherRestCalls.retrieveUser_Functionalities("max.mustermann@web.de");

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

    public void updateTimelineDays() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view_timelineDays.removeAllViews();
            }
        });


        LayoutInflater inflater = LayoutInflater.from(this.getActivity());

        list_TimelineDays = new ArrayList<>();
        timeLineDays = t.getTimelineDays();
        counter_timelinedays = 0;
        //view_timelineDays.removeAllViews();
        for(TimelineDay t_d: timeLineDays) {
            final LinearLayout row = (LinearLayout)inflater.inflate(R.layout.timeline_day, null);
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
            if(choosedChildren.getChildCount() != 0) {
                updateTimelineView();
            }
        }

    }

    public void updateTimelineView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                choosedChildren.removeAllViews();
            }
        });

        final LayoutInflater inflater = LayoutInflater.from(this.getActivity());

        LinkedList<TimelineSegment> timeLineSegments = choosedTimelineDay.getMySegments();
        boolean firstLoop = true;


        for(TimelineSegment tSegment: timeLineSegments) {
            LinkedList<LocationEntry> locationEntries = tSegment.getLocationPoints();

            RelativeLayout view_FirstPoint = null;
            RelativeLayout view_segment = null;
            RelativeLayout view_LastPoint = null;

            if(!locationEntries.isEmpty()) {
                LocationEntry fstPoint = locationEntries.get(0);

                view_FirstPoint = (RelativeLayout)inflater.inflate(R.layout.timeline_locationpoint, null);
                TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeAndaddress);
                TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String strDate = sdf.format(fstPoint.getMyEntryDate());


                myEntryDate.setText(strDate);
                placeAndaddress.setText(tSegment.getStartAddress());

                if(timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint

                    view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                    TextView activeTime = (TextView) view_segment.findViewById(R.id.tv_activeTime);
                    TextView activeDistance = (TextView) view_segment.findViewById(R.id.tv_activedistance);
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

                    if(finalView_FirstPoint != null)
                        choosedChildren.addView(finalView_FirstPoint);


                    if(finalView_segment != null)
                        choosedChildren.addView(finalView_segment);

                }
            });

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout whiteSpace = (LinearLayout) inflater.inflate(R.layout.timeline_whitespace, null);

                choosedChildren.addView(whiteSpace);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch ((String)v.getTag()) {
            case TAG_TIMELINEDAY:
                LinearLayout tday = list_TimelineDays.get(v.getId());
                choosedChildren = (LinearLayout) tday.findViewById(R.id.ll_all_locations);
                choosedTimelineDay = timeLineDays.get(v.getId());
                updateTimelineView();
                break;

        }
    }
}

