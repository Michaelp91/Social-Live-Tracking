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
    private HashMap<String, TimelineSegment> h_viewedTimelineSegments = new HashMap<>();
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
        setContentView(R.layout.activity_segment_view);
        choosedChildren = (LinearLayout) findViewById(R.id.timeline_segments);
        this.choosedTimelineSegments = DataProvider.getInstance().getChoosedTimelineSegments();
        context = this;
        initTimelineView();
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

                    LinearLayout ll_shortLine = null;



                    if (!locationEntries.isEmpty() && detectedActivity.getType() !=
                            com.slt.definitions.Constants.TIMELINEACTIVITY.TILTING
                            && detectedActivity.getType() != com.slt.definitions.Constants.TIMELINEACTIVITY.STILL
                            && (!unknownSegmentAdded || detectedActivity.getType() != com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN)  ) {
                        unknownSegmentAdded = false;
                        FunctionalityLogger.getInstance().AddLog("\nTimeline Segment: ");
                        FunctionalityLogger.getInstance().AddLog("Number: " + loggerCounter);
                        FunctionalityLogger.getInstance().AddLog("ObjectId: " + tSegment.getID());
                        FunctionalityLogger.getInstance().AddLog("Activity(IN_VEHICLE, ON_BICYCLE, ON_FOOT, STILL, UNKNOWN, TILTING, WALKING, RUNNING): " + tSegment.getMyActivity().getType());
                        FunctionalityLogger.getInstance().AddLog("Start: " + tSegment.getAddress());

                        FunctionalityLogger.getInstance().AddLog("");
                        loggerCounter++;


                        isAdded = true;
                        h_viewedTimelineSegments.put(tSegment.getID(), tSegment);


                        LocationEntry fstPoint = locationEntries.get(0);

                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                        TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);
                        ll_shortLine = (LinearLayout) view_FirstPoint.findViewById(R.id.ll_line);
                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        String strDate = sdf.format(fstPoint.getMyEntryDate());

                        FunctionalityLogger.getInstance().AddLog("Start: " + strDate);
                        FunctionalityLogger.getInstance().AddLog("Location Point(Longitude, Lattitude): " + fstPoint.getLongitude() + ", " + fstPoint.getLatitude());


                        myEntryDate.setText(strDate + " Uhr");
                        placeAndaddress.setText(tSegment.getStartPlace());

                        if (timeLineSegments.indexOf(tSegment) < timeLineSegments.size() - 1) { //Draw Segment and the Endpoint


                            view_segment = (RelativeLayout) inflater.inflate(R.layout.timeline_segment, null);
                            TextView activeTime = (TextView) view_segment.findViewById(R.id.tv_activeTime);
                            final TextView activeDistance = (TextView) view_segment.findViewById(R.id.tv_activedistance);
                            final ImageView activity = (ImageView) view_segment.findViewById(R.id.iv_activity);
                            final LinearLayout ll_line = (LinearLayout) view_segment.findViewById(R.id.ll_line);
                            final LinearLayout ll_pictures = (LinearLayout) view_segment.findViewById(R.id.ll_pictures);
                            final ImageView iv_pictures = (ImageView) view_segment.findViewById(R.id.iv_addPicture);
                            final ImageView iv_comments = (ImageView) view_segment.findViewById(R.id.iv_addComments);
                            final ImageView iv_activity = (ImageView) view_segment.findViewById(R.id.iv_addActivity);
                            iv_details = (ImageView) view_segment.findViewById(R.id.iv_addDetails);
                            final TextView tv_usercomments = (TextView) view_segment.findViewById(R.id.tv_usercomments);
                            tv_usercomments.setTag(tSegment);



                            activeTime.setText(Double.toString(tSegment.getDuration()));
                            activeDistance.setText(Double.toString(tSegment.getActiveDistance()));

                            iv_activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_running, 100, 100)));
                            iv_pictures.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_pictures, 100, 100)));
                            iv_comments.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_comments, 100, 100)));
                            iv_details.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.timeline_details, 100, 100)));
                            AddPictures(ll_pictures, tSegment);

                            ll_pictures.setTag(tSegment);
                            iv_pictures.setTag(tSegment);

                            picViews.put(tSegment.getID(), ll_pictures);
                            AddUserComments(tSegment.getStrUserComments(), ll_line, tv_usercomments);


                            iv_pictures.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //choosedPicView = (LinearLayout) v;
                                    TimelineSegment tSegment = (TimelineSegment) v.getTag();
                                    choosedPicView = picViews.get(tSegment.getID());
                                    selectImage();
                                }
                            });

                            iv_activity.setTag(tSegment);
                            final LinearLayout finalLl_shortLine1 = ll_shortLine;
                            iv_activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog commentDialog = new Dialog(context);
                                    final TimelineSegment tSegment = (TimelineSegment) iv_activity.getTag();
                                    commentDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
                                    commentDialog.setContentView(R.layout.popup_activity);

                                    final Spinner spinner = (Spinner) commentDialog.findViewById(R.id.sp_activity);
                                    Button btnOk = (Button) commentDialog.findViewById(R.id.btn_ok);

                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String IN_VEHICLE = "In Vehicle";
                                            final String On_Bicycle = "On Bicycle";
                                            final String On_Foot = "On Foot";
                                            final String Walking = "Walking";
                                            final String Running = "Running";

                                            String strActivity = spinner.getSelectedItem().toString();
                                            int intActivity = tSegment.getMyActivity().getType();



                                            switch (strActivity) {
                                                case IN_VEHICLE:
                                                    intActivity = 0;
                                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.in_vehicle, 50, 50)));
                                                    ll_line.setBackgroundResource(R.color.md_red_500);
                                                    finalLl_shortLine1.setBackgroundResource(R.color.md_red_500);
                                                    break;
                                                case On_Bicycle:
                                                    intActivity = 1;
                                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.biking, 50, 50)));
                                                    ll_line.setBackgroundResource(R.color.md_light_green_600);
                                                    finalLl_shortLine1.setBackgroundResource(R.color.md_light_green_600);
                                                    break;

                                                case On_Foot:
                                                    intActivity = 2;
                                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.walking, 50, 50)));
                                                    ll_line.setBackgroundResource(R.color.md_blue_400);
                                                    finalLl_shortLine1.setBackgroundResource(R.color.md_blue_400);
                                                    break;

                                                case Walking:
                                                    intActivity = 7;
                                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.walking, 50, 50)));
                                                    ll_line.setBackgroundColor(Color.GREEN);
                                                    finalLl_shortLine1.setBackgroundColor(Color.GREEN);
                                                    break;

                                                case Running:
                                                    intActivity = 8;
                                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.running, 50, 50)));
                                                    ll_line.setBackgroundResource(R.color.md_amber_800);
                                                    finalLl_shortLine1.setBackgroundResource(R.color.md_amber_800);
                                                    break;

                                            }

                                            User user = DataProvider.getInstance().getOwnUser();
                                            DetectedActivity detectedActivity = new DetectedActivity(intActivity, 100);
                                            tSegment.setMyActivity(detectedActivity);


                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    OtherRestCalls.updateTimelineSegmentForActivity(tSegment);

                                                    context.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            commentDialog.hide();
                                                        }
                                                    });
                                                }
                                            }).start();

                                        }
                                    });

                                    commentDialog.show();
                                }
                            });

                            iv_comments.setTag(tSegment);

                            tv_usercomments.setTag(tSegment.getID());
                            iv_comments.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimelineSegment test_tSegment = (TimelineSegment) iv_comments.getTag();
                                    final Dialog commentDialog = new Dialog(context);
                                    commentDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
                                    commentDialog.setContentView(R.layout.popup_usercomment);

                                    final EditText editText = (EditText) commentDialog.findViewById(R.id.et_titel);
                                    Button btnOk = (Button) commentDialog.findViewById(R.id.btn_ok);

                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            User user = DataProvider.getInstance().getOwnUser();
                                            String email = user.getEmail();
                                            String comment = "- " + email + " : " + editText.getText().toString() + "\n";
                                            String comments = tv_usercomments.getText().toString();
                                            comments += comment;
                                            tSegment.addStrUserComment(comment);


                                            final String finalComments = comments;

                                            tv_usercomments.setText(finalComments);


                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    OtherRestCalls.updateTimelineSegmentForComments(tSegment);

                                                    context.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            commentDialog.hide();
                                                        }
                                                    });
                                                }
                                            }).start();

                                        }
                                    });

                                    commentDialog.show();

                                }
                            });


                            final LinearLayout finalLl_shortLine = ll_shortLine;

                            switch (detectedActivity.getType()) {
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.WALKING:
                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.walking, 50, 50)));
                                    ll_line.setBackgroundColor(Color.GREEN);
                                    finalLl_shortLine.setBackgroundColor(Color.GREEN);
                                    break;
                                case com.slt.definitions.Constants.TIMELINEACTIVITY.RUNNING:
                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.running, 50, 50)));
                                    ll_line.setBackgroundResource(R.color.md_amber_800);
                                    finalLl_shortLine.setBackgroundResource(R.color.md_amber_800);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.IN_VEHICLE:
                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.in_vehicle, 50, 50)));
                                    ll_line.setBackgroundResource(R.color.md_red_500);
                                    finalLl_shortLine.setBackgroundResource(R.color.md_red_500);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_FOOT:
                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.walking, 50, 50)));
                                    ll_line.setBackgroundResource(R.color.md_blue_400);
                                    finalLl_shortLine.setBackgroundResource(R.color.md_blue_400);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.ON_BICYCLE:
                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.biking, 50, 50)));
                                    ll_line.setBackgroundResource(R.color.md_light_green_600);
                                    finalLl_shortLine.setBackgroundResource(R.color.md_light_green_600);
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN:
                                    unknownSegmentAdded = true;
                                    activity.setImageDrawable(new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.unknown, 50, 50)));
                                    break;

                                case com.slt.definitions.Constants.TIMELINEACTIVITY.STILL:
                                    activity.setVisibility(View.GONE);
                                    break;

                                default:
                                    activity.setVisibility(View.GONE);
                                    break;
                            }



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
                            ll_shortLine.setVisibility(View.INVISIBLE);
                        }

                    } else if (timeLineSegments.indexOf(tSegment) == timeLineSegments.size() - 1 && !locationEntries.isEmpty()
                            && (tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.STILL
                            || tSegment.getMyActivity().getType() == com.slt.definitions.Constants.TIMELINEACTIVITY.UNKNOWN)) {
                        LocationEntry fstPoint = locationEntries.get(0);

                        view_FirstPoint = (RelativeLayout) inflater.inflate(R.layout.timeline_locationpoint, null);
                        ll_shortLine = (LinearLayout) view_FirstPoint.findViewById(R.id.ll_line);
                        TextView placeAndaddress = (TextView) view_FirstPoint.findViewById(R.id.tv_placeOraddress);

                        TextView myEntryDate = (TextView) view_FirstPoint.findViewById(R.id.tv_myEntryDate);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String strDate = sdf.format(fstPoint.getMyEntryDate());
                        ll_shortLine.setVisibility(View.INVISIBLE);


                        myEntryDate.setText(strDate);
                        placeAndaddress.setText(tSegment.getStartAddress());
                    }


                    final RelativeLayout finalView_FirstPoint = view_FirstPoint;
                    final RelativeLayout finalView_segment = view_segment;
                    final ImageView finalIv_details = iv_details;

                    if (finalView_FirstPoint != null)
                        choosedChildren.addView(finalView_FirstPoint);


                    if (finalView_segment != null) {
                        finalIv_details.setTag(tSegment);
                        finalIv_details.setOnClickListener(new View.OnClickListener() {
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


        return ll_pictures;
    }
}
