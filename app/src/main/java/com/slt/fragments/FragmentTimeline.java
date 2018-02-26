package com.slt.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.MainProfile;
import com.slt.R;
import com.slt.SegmentViewActivity;
import com.slt.TimelineDetailsActivity;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
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
import com.slt.restapi.UsefulMethods;
import com.slt.restapi.data.Image;
import com.slt.restapi.data.REST_LocationEntry;
import com.slt.restapi.data.REST_TimelineDay;
import com.slt.restapi.data.REST_TimelineSegment;
import com.slt.utils.Constants;
import com.slt.utils.FunctionalityLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

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
import android.widget.Toast;


public class FragmentTimeline extends Fragment implements View.OnClickListener {

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
    private View view;
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

    public boolean timelinedayIsNotViewed(String id) {
        return (h_viewedTimelineDays.get(id) == null)? true:false;
    }

    public boolean timelinesegmentIsNotViewed(String id) {
        return (h_viewedTimelineSegments.get(id) == null)? true:false;
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
        try {

            if (getActivity() != null) {
                LayoutInflater inflater = LayoutInflater.from(this.getActivity());


                try {
                    timeLineDays = t.getTimelineDays();
                } catch (NullPointerException e) {
                    return;
                }
                counter_timelinedays = 0;
                //view_timelineDays.removeAllViews();
                for (TimelineDay t_d : timeLineDays) {

                    if (timelinedayIsNotViewed(t_d.getID())) {
                        h_viewedTimelineDays.put(t_d.getID(), t_d);
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

    @Override
    public void onClick(View v) {

        try {

            switch ((String) v.getTag()) {
                case TAG_TIMELINEDAY:
                    LinearLayout tday = list_TimelineDays.get(v.getId());
                    choosedChildren = (LinearLayout) tday.findViewById(R.id.ll_all_locations);
                    choosedTimelineDay = timeLineDays.get(v.getId());
                    DataProvider.getInstance().setChoosedTimelineSegments(choosedTimelineDay.getMySegments());
                    Intent i = new Intent(this.getActivity(), SegmentViewActivity.class);
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

