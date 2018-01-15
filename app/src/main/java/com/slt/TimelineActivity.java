package com.slt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.TemporaryDB;
import com.slt.restapi.TrackingSimulator;
import com.slt.restapi.UsefulMethods;
import com.slt.restapi.data.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {

    private static final LatLng DARMSTADT_NORD = new LatLng(50.0042304, 9.0658932);
    private static final LatLng WILLYBRANDTPLATZ = new LatLng(49.9806625, 9.1355554);
    public Handler handler = new Handler();
    //@BindView(R.id.toolbar)
    //public Toolbar toolBar;

    private REST_TimelineDay choosedTimelineDay;
    private ArrayList<LinearLayout> list_TimelineDays;
    private ArrayList<LinearLayout> list_TimelineSegments;
    private LinearLayout view_timelineDays;
    private LinearLayout choosedChildren;
    private int counter_timelinedays;
    private int counter_timelinechildren;
    private final String TAG_TIMELINEDAY = "timelineday";
    private final String TAG_TIMELINESEGMENT = "timelinesegment";
    private ArrayList<REST_TimelineDay> timeLineDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        view_timelineDays = (LinearLayout) findViewById(R.id.timeline_days);
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Constants.USERID);
        /*//Toolbar toolBar = null;
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        //DrawerUtil.getDrawer(this,toolBar);
        //toolBar.setTitle(getResources().getString(R.string.navigation_item_1));
        toolBar.setTitle("test");
        setSupportActionBar(toolBar);
        DrawerUtil.getDrawer(this,toolBar);*/
        /*
            Test t = new Test("Hello World");
            Achievement newAchievement = new Achievement(0);
            ArrayList<Achievement> myAchievements = new ArrayList<>();
            myAchievements.add(newAchievement);
            TimeLine obj = new TimeLine("5a196bf8d17b7926882f5413", myAchievements);
            TimeLineDay timelineday = new TimeLineDay(new Date(), myAchievements, obj);
            TimeLineSegment timeLineSegment = new TimeLineSegment
                 ("Teststr. 84, 6442 Testhausen", "3", 2.4 ,
                              2.0, myAchievements, timelineday);
            Location location = new Location(2.3, 4.2);
            LocationEntry locationentry = new LocationEntry(new Date(), 2.1, 2.0,
                location, timeLineSegment);

            DataUpdater.getInstance().setTimeline(obj);
            DataUpdater.getInstance().addTimelineDay(timelineday);
            DataUpdater.getInstance().addTimeLineSegment(timeLineSegment);
            DataUpdater.getInstance().addLocationEntry(locationentry);

            DataUpdater.getInstance().Start();


            //UpdateOperations.createTimeLine(obj);
            //TimeLine timeline = Singleton.getInstance().getResponse_timeLine();
            //TimeLineDay timelineday = new TimeLineDay(new Date(), myAchievements, "5a429f34ef946d3b30d71358");
            //json = new Gson().toJson(timelineday);
            //UpdateOperations.createTimeLineDay(timelineday);
            //RetrieveOperations.getTestData2(t);
          //  TimeLineSegment timeLineSegment = new TimeLineSegment
          //          ("Teststr. 84, 6442 Testhausen", "3", 2.4 ,
          //                  2.0, "5a42b1e52026a72bd8632eb0", myAchievements);

          //UpdateOperations.createTimeLineSegment(timeLineSegment);
          */


/*
        Location location = new Location(2.3, 4.2);
        LocationEntry locationentry = new LocationEntry(new Date(), 2.1, 2.0,
                location, "5a42c40b6deb772de0c9f67e");

        Thread thread = new Thread(new UpdateOperations_Synchron());
        thread.start();


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            String test = "";
        }

        */


        /*
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            String test = "";
        }
        */

        //UpdateOperations_Synchron.createLocationEntry(locationentry);




        /*
        com.slt.rest_trackingtimeline.data.User user = new com.slt.rest_trackingtimeline.data.User();
        user._id = "5a196bf8d17b7926882f5413";
        RetrieveOperations.getInstance().context = this;
        RetrieveOperations.getInstance().getCompleteTimeline(user);
        */

                /*
        Thread t = new Thread(new TrackingSimulator());
        t.start();
*/

                /*

        RetrieveOperations.getInstance().context = this;

        handler.postDelayed(runnable, 2000);
        */



        //Thread t = new Thread(new TrackingSimulator());
        //t.start();

        //Thread t = new Thread(new TrackingSimulator());
        //t.start();

        Thread t = new Thread(new TrackingSimulator());
        t.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //ImageView imageView = (ImageView) findViewById(R.id.iv_running);

        /*
        Bitmap bitmap = ((BitmapDrawable) imageView.getBackground()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        String debug = "True";
        */

        //UsefulMethods.UploadImageView(imageView);


        //UsefulMethods.LoadImage(this);
    }

    public void loadPicture(byte[]image) {
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        ImageView imageView = (ImageView) findViewById(R.id.iv_running);

        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 400,
                400, false));
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            User user = new User("");
            RetrieveOperations.getInstance().getCompleteTimeline();
            OtherRestCalls.retrieveFriendsIncludingTimelines();
      /* and here comes the "trick" */
        }
    };


    public void initTimelineDays() {
        LayoutInflater inflater = LayoutInflater.from(this);

        list_TimelineDays = new ArrayList<>();
        timeLineDays = TemporaryDB.getInstance().getTimelineDays();
        counter_timelinedays = 0;
        view_timelineDays.removeAllViews();
        for(REST_TimelineDay t_d: timeLineDays) {
            LinearLayout row = (LinearLayout)inflater.inflate(R.layout.timeline_day, null);
            TextView myDate = (TextView) row.findViewById(R.id.tv_myDate);
//            ImageView imageView = (ImageView) row.findViewById(R.id.iv_activity);
//            UsefulMethods.UploadImageView(imageView);


            Date date = t_d.myDate;

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String strDate = sdf.format(date);

            myDate.setText(strDate);

            row.setTag(TAG_TIMELINEDAY);
            row.setId(counter_timelinedays);
            row.setOnClickListener(this);

            view_timelineDays.addView(row);
            list_TimelineDays.add(row);
            counter_timelinedays++;
        }

    }

    public void updateTimelineDays() {
        LayoutInflater inflater = LayoutInflater.from(this);

        list_TimelineDays = new ArrayList<>();
        timeLineDays = TemporaryDB.getInstance().getTimelineDays();
        counter_timelinedays = 0;
        //view_timelineDays.removeAllViews();
        for(REST_TimelineDay t_d: timeLineDays) {
            LinearLayout row = (LinearLayout)inflater.inflate(R.layout.timeline_day, null);
            TextView myDate = (TextView) row.findViewById(R.id.tv_myDate);
//            ImageView imageView = (ImageView) row.findViewById(R.id.iv_activity);
//            UsefulMethods.UploadImageView(imageView);


            Date date = t_d.myDate;

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String strDate = sdf.format(date);

            myDate.setText(strDate);

            row.setTag(TAG_TIMELINEDAY);
            row.setId(counter_timelinedays);
            row.setOnClickListener(this);

            view_timelineDays.addView(row);
            list_TimelineDays.add(row);
            counter_timelinedays++;
        }

        if(choosedChildren != null) {
            if(choosedChildren.getChildCount() != 0) {
                updateTimelineView();
            }
        }

    }

    public void updateTimelineView() {
        LayoutInflater inflater = LayoutInflater.from(this);

        ArrayList<REST_TimelineSegment> timeLineSegments = TemporaryDB.getInstance().
                findTimelineSegmentsByTDayId(choosedTimelineDay._id);
        boolean firstLoop = true;


        for(REST_TimelineSegment tSegment: timeLineSegments) {
            ArrayList<REST_LocationEntry> locationEntries = TemporaryDB.getInstance()
                    .findLocationEntriesByTSegmentId(tSegment._id);

            RelativeLayout view_FirstPoint = null;
            RelativeLayout view_segment = null;
            RelativeLayout view_LastPoint = null;

            if(!locationEntries.isEmpty()) {
                REST_LocationEntry fstPoint = locationEntries.get(0);

                view_FirstPoint = (RelativeLayout)inflater.inflate(R.layout.timeline_locationpoint, null);
                TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeAndaddress);
                TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String strDate = sdf.format(fstPoint.myEntryDate);


                myEntryDate.setText(strDate);
                placeAndaddress.setText(tSegment.startAddress);

                if(timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint

                    view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                    TextView activeTime = (TextView) view_segment.findViewById(R.id.tv_activeTime);
                    TextView activeDistance = (TextView) view_segment.findViewById(R.id.tv_activedistance);
                    activeTime.setText(Double.toString(tSegment.duration));
                    activeDistance.setText(Double.toString(tSegment.activeDistance));

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

            if(view_FirstPoint != null)
                choosedChildren.addView(view_FirstPoint);


            if(view_segment != null)
                choosedChildren.addView(view_segment);
        }

        LinearLayout whiteSpace = (LinearLayout) inflater.inflate(R.layout.timeline_whitespace, null);

        choosedChildren.addView(whiteSpace);

    }

    public void initTimelineView() {
        LayoutInflater inflater = LayoutInflater.from(this);

        ArrayList<REST_TimelineSegment> timeLineSegments = TemporaryDB.getInstance().
                                                findTimelineSegmentsByTDayId(choosedTimelineDay._id);
        boolean firstLoop = true;


        for(REST_TimelineSegment tSegment: timeLineSegments) {
            ArrayList<REST_LocationEntry> locationEntries = TemporaryDB.getInstance()
                                                        .findLocationEntriesByTSegmentId(tSegment._id);

            RelativeLayout view_FirstPoint = null;
            RelativeLayout view_segment = null;
            RelativeLayout view_LastPoint = null;

            if(!locationEntries.isEmpty()) {
                REST_LocationEntry fstPoint = locationEntries.get(0);

                    view_FirstPoint = (RelativeLayout)inflater.inflate(R.layout.timeline_locationpoint, null);
                    TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeAndaddress);
                    TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    String strDate = sdf.format(fstPoint.myEntryDate);


                    myEntryDate.setText(strDate);
                    placeAndaddress.setText(tSegment.startAddress);

                    if(timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint

                        view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                        TextView activeTime = (TextView) view_segment.findViewById(R.id.tv_activeTime);
                        TextView activeDistance = (TextView) view_segment.findViewById(R.id.tv_activedistance);
                        activeTime.setText(Double.toString(tSegment.duration));
                        activeDistance.setText(Double.toString(tSegment.activeDistance));

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

            if(view_FirstPoint != null)
              choosedChildren.addView(view_FirstPoint);


            if(view_segment != null)
              choosedChildren.addView(view_segment);
        }

        LinearLayout whiteSpace = (LinearLayout) inflater.inflate(R.layout.timeline_whitespace, null);

        choosedChildren.addView(whiteSpace);

    }
/*
    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

        Intent intent = new Intent(this, TimelineDetailsActivity.class);

        startActivity(intent);

        /*
        TimelineHeader key = listDataHeader.get(groupPosition);

        List<Route> contents = listDataChild.get(key);
        Node c = contents.get(childPosition);



        Intent i = new Intent(this, Activity_TimelineDetails.class);
        startActivity(i);
        */

       //return false;
   // }


    @Override
    public void onClick(View view) {
        switch ((String)view.getTag()) {
            case TAG_TIMELINEDAY:
                LinearLayout tday = list_TimelineDays.get(view.getId());
                choosedChildren = (LinearLayout) tday.findViewById(R.id.ll_all_locations);
                choosedTimelineDay = timeLineDays.get(view.getId());


                if(choosedChildren.getChildCount() == 0) {
                    choosedChildren.removeAllViews();
                    list_TimelineSegments = new ArrayList<>();
                    updateTimelineView();
                }
                else {
                    choosedChildren.removeAllViews();
                }
                break;

        }
    }
}
