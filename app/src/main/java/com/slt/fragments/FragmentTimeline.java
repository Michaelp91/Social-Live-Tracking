package com.slt.fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
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
import com.slt.restapi.TrackingSimulator;
import com.slt.restapi.UsefulMethods;
import com.slt.restapi.data.Image;
import com.slt.restapi.data.REST_LocationEntry;
import com.slt.restapi.data.REST_TimelineDay;
import com.slt.restapi.data.REST_TimelineSegment;
import com.slt.utils.Constants;

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
    private LinkedList<Bitmap> downloadedImages = new LinkedList<>();
    private LinkedList<Integer> randomPositions = new LinkedList<>();
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    ImageView tmpImageView;


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


        LayoutInflater inflater = LayoutInflater.from(this.getActivity());


        timeLineDays = t.getTimelineDays();
        counter_timelinedays = 0;
        //view_timelineDays.removeAllViews();
        for(TimelineDay t_d: timeLineDays) {

            if(timelinedayIsNotViewed(t_d.getID())) {
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


        for(final TimelineSegment tSegment: timeLineSegments) {

            if (timelinesegmentIsNotViewed(tSegment.getID())) {
                isAdded = true;
                h_viewedTimelineSegments.put(tSegment.getID(), tSegment);
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
                        LinearLayout ll_pictures = (LinearLayout) view_segment.findViewById(R.id.ll_pictures);

                        ll_pictures = AddPictures(tSegment, ll_pictures, inflater);

                        ll_pictures.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectImage();
                            }
                        });





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
                                        ll_line.setBackgroundResource(R.color.md_amber_800);
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


                        if (finalView_segment != null) {
                            finalView_segment.setTag(tSegment);
                            finalView_segment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TimelineSegment tSegment = (TimelineSegment) view.getTag();
                                    SharedResources.getInstance().setOnClickedTimelineSegmentForDetails(tSegment);
                                    Intent intent = new Intent(getActivity(), TimelineDetailsActivity.class);
                                    startActivity(intent);
                                }
                            });

                            choosedChildren.addView(finalView_segment);
                        }

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

    private void selectImage() {
        try {
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                tmpImageView = new ImageView(getActivity());


                //compress the picture -> reduces the quality by 50%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] BYTE = bytes.toByteArray();

                this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                tmpImageView.setImageBitmap(bitmap);
                downloadedImages.add(bitmap);

                //TODO: The File is not uploaded after uploadImage()
                uploadImage();

                Log.e(TAG, "Pick from Camera::>>> ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ApplicationController.getContext().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //compress the picture -> reduces the quality by 50%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] BYTE = bytes.toByteArray();

                this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                downloadedImages.add(bitmap);
                Log.e(TAG, "Pick from Gallery::>>> ");

                //TODO: The File is not uploaded after uploadImage()
                uploadImage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer imageId = downloadedImages.size() + 1;
                Bitmap bmp = ((BitmapDrawable)tmpImageView.getDrawable()).getBitmap();


                final boolean uploaded = UsefulMethods.UploadImageView(bmp, "Dies.png");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uploaded)
                            Toast.makeText(getActivity(), "Image is uploaded successfully.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Image is not successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private LinearLayout AddPictures(TimelineSegment tSegment, final LinearLayout ll_pictures , final LayoutInflater inflater) {
        ArrayList<Integer> numbersInUse = new ArrayList<>();
        for(String image: tSegment.getMyImages()) {
            Bitmap bmp = UsefulMethods.LoadImage(image);
            downloadedImages.add(bmp);
        }

        int max = (downloadedImages.size() >= 3)?3: downloadedImages.size();
        for(int i = 0; i < max; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, downloadedImages.size());

            Bitmap bmp = downloadedImages.get(randomNum);

            final LinearLayout ll_picture = (LinearLayout) inflater.inflate(R.layout.timelinesegment_picture, null);
            ImageView picture = (ImageView) ll_picture.findViewById(R.id.picture);
            picture.setImageBitmap(bmp);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll_pictures.addView(ll_picture);
                }
            });
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

        return ll_pictures;

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

                if(h_alreadyChoosedDay.get(choosedTimelineDay.getID()) == null) {
                        h_alreadyChoosedDay = new HashMap<>();
                        h_alreadyChoosedDay.put(choosedTimelineDay.getID(), choosedTimelineDay);
                        updateTimelineView();
                } else {
                    h_alreadyChoosedDay = new HashMap<>();
                    downloadedImages = new LinkedList<>();
                    choosedTimelineDay = null;
                    choosedChildren = null;
                }


                break;

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

